package com.ezkhimo.intellij.zephyr.action

import com.ezkhimo.intellij.zephyr.action.ui.StoryIdInputValidator
import com.ezkhimo.intellij.zephyr.resources.Icons
import com.ezkhimo.intellij.zephyr.api.TestCase
import com.ezkhimo.intellij.zephyr.api.TestStep
import com.ezkhimo.intellij.zephyr.resources.getString
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.actions.CreateTemplateInPackageAction
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.util.IncorrectOperationException
import kotlinx.coroutines.*
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes
import org.jetbrains.kotlin.psi.KtFile
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NewZephyrTestCaseAction : CreateFileFromTemplateAction(
    getString("zephyr.action.new.file.text"),
    getString("zephyr.action.new.file.description"),
    Icons.Action
), DumbAware {

    var storyId: String = ""

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabledAndVisible = true
    }

    private val FILE_SEPARATORS = charArrayOf('/', '\\')

    override fun createFileFromTemplate(name: String, template: FileTemplate, dir: PsiDirectory): PsiFile? {
        this.storyId = name
        val directorySeparators = FILE_SEPARATORS
        val (className, targetDir) = findOrCreateTarget(dir, sanitizeClassName(name), directorySeparators)
        val adjustedDir = CreateTemplateInPackageAction.adjustDirectory(targetDir, JavaModuleSourceRootTypes.SOURCES)
        return createFromTemplate(adjustedDir, className, template)
    }

    private fun sanitizeClassName(userInput: String) = userInput.filter { it.isLetter() || it.isDigit() }

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder
            .setTitle(getString("zephyr.action.new.file.dialog.title"))
            .addKind(
                getString("zephyr.action.new.file.label"),
                Icons.TestCase,
                "Kotlin Class" // TODO Define template, use here.
            )
            .setValidator(StoryIdInputValidator())
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return getString("zephyr.action.new.file.text")
    }

    private fun findOrCreateTarget(
        dir: PsiDirectory,
        name: String,
        directorySeparators: CharArray
    ): Pair<String, PsiDirectory> {
        var className = name
        var targetDir = dir

        for (splitChar in directorySeparators) {
            if (splitChar in className) {
                val names = className.trim().split(splitChar)

                for (dirName in names.dropLast(1)) {
                    targetDir = targetDir.findSubdirectory(dirName) ?: runWriteAction {
                        targetDir.createSubdirectory(dirName)
                    }
                }

                className = names.last()
                break
            }
        }
        return Pair(className, targetDir)
    }

    override fun postProcess(
        createdElement: PsiFile,
        templateName: String?,
        customProperties: MutableMap<String, String>?
    ) {
        super.postProcess(createdElement, templateName, customProperties)
        lateinit var kotlinFile: KtFile
        runBlocking {
            val testCase: TestCase = fetchTest(storyId)
            val packageName = if (createdElement is KtFile) {
                kotlinFile = createdElement
                createdElement.packageFqName.asString()
            } else {
                ""
            }
        }
    }

    private suspend fun fetchTest(storyId: String): TestCase {
        return suspendCoroutine { continuation ->
            continuation.resume(
                TestCase(
                    storyId, listOf(
                        TestStep(1, "Moin"),
                        TestStep(2, "Second")
                    )
                )
            )
        }
    }

    private fun createFromTemplate(dir: PsiDirectory, className: String, template: FileTemplate): PsiFile? {
        val project = dir.project
        val defaultProperties = FileTemplateManager.getInstance(project).defaultProperties

        val properties = Properties(defaultProperties)

        val element = try {
            val thread = Thread.currentThread()
            val originalLoader = thread.contextClassLoader
            val pluginClassLoader = javaClass.classLoader
            try {
                thread.contextClassLoader = pluginClassLoader
                FileTemplateUtil.createFromTemplate(
                    template, className, properties, dir, pluginClassLoader
                )
            } finally {
                thread.contextClassLoader = originalLoader
            }
        } catch (e: IncorrectOperationException) {
            throw e
        } catch (e: Exception) {
            LOG.error(e)
            return null
        }

        return element?.containingFile
    }
}