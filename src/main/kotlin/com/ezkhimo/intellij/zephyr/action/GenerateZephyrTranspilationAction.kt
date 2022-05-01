package com.ezkhimo.intellij.zephyr.action

import com.ezkhimo.intellij.zephyr.action.ui.EnterStoryIdDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import org.koin.java.KoinJavaComponent.inject
import com.ezkhimo.intellij.zephyr.transpile.ZephyrTestCaseTranspiler


class GenerateZephyrTranspilationAction : AnAction() {

    private val transpiler: ZephyrTestCaseTranspiler by inject(ZephyrTestCaseTranspiler::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE)
        val dialog = EnterStoryIdDialog(project)
        val storyId = dialog.awaitStoryId()
        storyId?.let {
            transpiler.transpileIntoEditor(project, editor, psiFile, it)
        }
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabledAndVisible = true
    }
}