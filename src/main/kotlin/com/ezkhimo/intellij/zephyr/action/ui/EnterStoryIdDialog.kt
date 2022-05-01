package com.ezkhimo.intellij.zephyr.action.ui

import com.ezkhimo.intellij.zephyr.resources.getString
import com.ezkhimo.intellij.zephyr.settings.ZephyrSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.util.ui.JBUI
import javax.swing.*

class EnterStoryIdDialog(project: Project) : DialogWrapper(project, true) {

    private lateinit var textField: JTextField

    private val inputValue: String
        get() = textField.text

    init {
        title = getString("zephyr.prompt.story.id.title")
        init()
        isResizable = false
    }

    override fun doValidate(): ValidationInfo? {
        val validator = StoryIdInputValidator()
        return if (!validator.checkInput(textField.text)) {
            ValidationInfo(validator.getErrorText(textField.text), textField)
        } else {
            null
        }
    }

    fun awaitStoryId(): String? = if (showAndGet()) {
        inputValue
    } else {
        null
    }

    override fun getButtonMap(): MutableMap<Action, JButton> {
        return mutableMapOf(
            cancelAction to JButton(getString("zephyr.dialog.cancel")),
            okAction to JButton(getString("zephyr.dialog.ok"))
        )
    }

    override fun createCenterPanel(): JComponent {
        val currentPrefix = ZephyrSettings.instance.storyPrefix
        textField = JTextField(currentPrefix)
        return JBUI.Panels.simplePanel()
            .addToCenter(textField)
            .addToTop(JLabel(getString("zephyr.prompt.story.id.message")))
    }

    override fun getPreferredFocusedComponent(): JComponent = textField
}