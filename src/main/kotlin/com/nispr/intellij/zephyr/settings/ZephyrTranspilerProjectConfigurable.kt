package com.nispr.intellij.zephyr.settings

import com.nispr.intellij.zephyr.resources.getString
import com.nispr.intellij.zephyr.settings.ui.ZephyrSettingsComponent
import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.options.Configurable


class ZephyrTranspilerProjectConfigurable : Configurable {

    private var settingsComponent: ZephyrSettingsComponent? = null

    private val settings: ZephyrTranspilerSettings
        get() = ZephyrTranspilerSettings.instance

    private val credentialInput
        get() = settingsComponent?.let { Credentials(it.userName, it.getPassword()) }

    private val storyPrefixInput
        get() = settingsComponent?.let { it.storyPrefix }

    private val jiraApiBaseUrlInput
        get() = settingsComponent?.let { it.jiraApiBaseUrlInput }

    override fun getPreferredFocusedComponent() = settingsComponent?.preferredFocusedComponent

    override fun createComponent() = ZephyrSettingsComponent().also { settingsComponent = it }.panel

    override fun isModified(): Boolean {
        val currentCredentials = PasswordSafe.instance.get(createCredentialAttributes(KEY_JIRA_CREDENTIALS))
        return credentialInput != currentCredentials
                || jiraApiBaseUrlInput != settings.jiraApiBaseUrl
                || storyPrefixInput != settings.storyPrefix
    }

    override fun apply() {
        val credentials = credentialInput
        ZephyrTranspilerSettings.instance.storyPrefix = storyPrefixInput
        ZephyrTranspilerSettings.instance.jiraApiBaseUrl = jiraApiBaseUrlInput
        PasswordSafe.instance.set(createCredentialAttributes(KEY_JIRA_CREDENTIALS), credentials)
    }

    private fun createCredentialAttributes(key: String) = CredentialAttributes(
        generateServiceName("Zephyr", key)
    )

    override fun reset() {
        super.reset()
        val currentCredentials = PasswordSafe.instance.get(createCredentialAttributes(KEY_JIRA_CREDENTIALS))
        settingsComponent?.userName = currentCredentials?.userName ?: ""
        settingsComponent?.storyPrefix = settings.storyPrefix ?: ""
        settingsComponent?.jiraApiBaseUrlInput = settings.jiraApiBaseUrl ?: ""
        settingsComponent?.setPassword(currentCredentials?.password.toString())
    }

    override fun getDisplayName(): String {
        return getString("zephyr.settings.title")
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }

    companion object {
        private const val KEY_JIRA_CREDENTIALS = "JIRA_CREDENTIALS"
    }
}

