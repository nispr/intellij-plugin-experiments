package com.nispr.intellij.zephyr.transpile

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jetbrains.rpc.LOG
import com.nispr.intellij.zephyr.api.FetchTestCaseUseCase
import com.nispr.intellij.zephyr.api.TestCase

interface ZephyrTestCaseTranspiler {
  fun transpileIntoEditor(project: Project, editor: Editor, file: PsiFile, issueId: String)
}

class ZephyrTestCaseTranspilerImpl(
    private val fetchTestCaseUseCase: FetchTestCaseUseCase,
    private val codeGenerator: TestCaseCodeGenerator
): ZephyrTestCaseTranspiler {

    private val coroutineContext = Dispatchers.IO + SupervisorJob()

    override fun transpileIntoEditor(project: Project, editor: Editor, file: PsiFile, issueId: String) {
        fetchTestCaseAndTranspile(project, editor, file, issueId)
    }

    private fun fetchTestCaseAndTranspile(project: Project, editor: Editor, file: PsiFile, issueId: String) {
        fetchTestCase(issueId = issueId) { testCase ->
            ApplicationManager.getApplication().invokeLater {
                WriteCommandAction.writeCommandAction(project).run<Throwable> {
                    insertTestCaseIntoFile(testCase, editor, file)
                }
            }
        }
    }

    private fun insertTestCaseIntoFile(testCase: TestCase, editor: Editor, file: PsiFile) {
        val testCode = codeGenerator.generateTestCode(testCase)
        editor.document.insertString(editor.caretModel.offset, testCode)
        formatFile(editor, file)
    }

    private fun formatFile(editor: Editor, file: PsiFile) {
        PsiDocumentManager.getInstance(file.project).commitDocument(editor.document)
        CodeStyleManager.getInstance(file.project).reformat(file)
    }

    private fun fetchTestCase(issueId: String, callback: (TestCase) -> Unit) {
        CoroutineScope(coroutineContext).launch {
            try {
                val testCase = fetchTestCaseUseCase.fetchTestCase(issueId)
                ApplicationManager.getApplication().invokeLater { callback(testCase) }
            } catch (e: Exception) {
                LOG.error("Failed to fetch test case", e)
            }
        }
    }
}