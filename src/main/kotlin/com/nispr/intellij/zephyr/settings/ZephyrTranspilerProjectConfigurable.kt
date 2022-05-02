package com.nispr.intellij.zephyr.settings

import com.intellij.credentialStore.Credentials
import com.intellij.openapi.options.Configurable
import com.nispr.intellij.zephyr.resources.getString
import com.nispr.intellij.zephyr.settings.ui.ZephyrSettingsComponent
import org.koin.java.KoinJavaComponent.inject


class ZephyrTranspilerProjectConfigurable : Configurable {

    private val credentialRepository: CredentialRepository by inject(CredentialRepository::class.java)

    private var settingsComponent: ZephyrSettingsComponent? = null

    private val settings: ZephyrTranspilerSettings
        get() = ZephyrTranspilerSettings.instance

    private val userNameInput: String?
        get() = settingsComponent?.let { it.userName }

    private val passwordInput: String?
        get() = settingsComponent?.let { it.getPassword().toString() }

    private val storyPrefixInput
        get() = settingsComponent?.let { it.storyPrefix }

    private val jiraApiBaseUrlInput
        get() = settingsComponent?.let { it.jiraApiBaseUrlInput }

    override fun getPreferredFocusedComponent() = settingsComponent?.preferredFocusedComponent

    override fun createComponent() = ZephyrSettingsComponent().also { settingsComponent = it }.panel

    override fun isModified(): Boolean {
        return Credentials(userNameInput, passwordInput) != credentialRepository.get()
                || jiraApiBaseUrlInput != settings.jiraApiBaseUrl
                || storyPrefixInput != settings.storyPrefix
    }

    override fun apply() {
        ZephyrTranspilerSettings.instance.storyPrefix = storyPrefixInput
        ZephyrTranspilerSettings.instance.jiraApiBaseUrl = jiraApiBaseUrlInput

    }

    override fun reset() {
        super.reset()
        val currentCredentials = credentialRepository.get()
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

}

