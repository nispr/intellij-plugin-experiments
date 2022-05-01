package com.nispr.intellij.zephyr.settings.ui

import com.nispr.intellij.zephyr.resources.getString
import com.nispr.intellij.zephyr.settings.ZephyrTranspilerSettings
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class ZephyrSettingsComponent {
    private val settings get() = ZephyrTranspilerSettings.instance
    val panel: JPanel

    private val jiraApiBaseUrlInputText = JBTextField(settings.jiraApiBaseUrl)
    private val userNameText = JBTextField()
    private val passwordText = JBPasswordField().apply {
        setPasswordIsStored(true)
    }
    private val storyPrefixText = JBTextField(settings.storyPrefix)

    val preferredFocusedComponent: JComponent
        get() = storyPrefixText

    var jiraApiBaseUrlInput: String
        get() = jiraApiBaseUrlInputText.text ?: ""
        set(newText) {
            jiraApiBaseUrlInputText.text = newText
        }

    var userName: String
        get() = userNameText.text ?: ""
        set(newText) {
            userNameText.text = newText
        }

    var storyPrefix: String
        get() = storyPrefixText.text ?: ""
        set(newText) {
            storyPrefixText.text = newText
        }


    fun setPassword(newPassword: String) {
        passwordText.text = newPassword
    }

    fun getPassword(): CharArray? = passwordText.password

    init {
        panel = FormBuilder.createFormBuilder()

            .addLabeledComponent(JBLabel(getString("zephyr.settings.jira.baseurl.label")), jiraApiBaseUrlInputText, 1, false)
            .addTooltip(getString("zephyr.settings.jira.baseurl.description"))

            .addLabeledComponent(JBLabel(getString("zephyr.settings.jira.username.label")), userNameText, 1, false)
            .addTooltip(getString("zephyr.settings.jira.username.description"))

            .addLabeledComponent(JBLabel(getString("zephyr.settings.jira.password.label")), passwordText, 1, false)
            .addTooltip(getString("zephyr.settings.jira.password.description"))

            .addLabeledComponent(JBLabel(getString("zephyr.settings.storyprefix.label")), storyPrefixText, 1, false)
            .addTooltip(getString("zephyr.settings.jira.issueprefix.description"))

            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

}