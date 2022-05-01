package com.nispr.intellij.zephyr.action

import com.nispr.intellij.zephyr.action.ui.EnterIssueIdDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import org.koin.java.KoinJavaComponent.inject
import com.nispr.intellij.zephyr.transpile.ZephyrTestCaseTranspiler


class TranspileZephyrTestCaseAction : AnAction() {

    private val transpiler: ZephyrTestCaseTranspiler by inject(ZephyrTestCaseTranspiler::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE)
        val dialog = EnterIssueIdDialog(project)
        val issueId = dialog.awaitIssueId()
        issueId?.let {
            transpiler.transpileIntoEditor(project, editor, psiFile, it)
        }
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabledAndVisible = true
    }
}