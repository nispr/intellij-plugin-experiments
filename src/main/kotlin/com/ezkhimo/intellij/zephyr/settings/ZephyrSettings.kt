package com.ezkhimo.intellij.zephyr.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "org.intellij.sdk.settings.AppSettingsState", storages = [Storage("ZephyrPluginSettings.xml")])
class ZephyrSettings : PersistentStateComponent<ZephyrSettings> {

    var jiraApiBaseUrl: String? = "https://localhost/jira"
    var storyPrefix: String? = ""

    override fun getState(): ZephyrSettings = this

    override fun loadState(state: ZephyrSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance: ZephyrSettings by lazy {
            ApplicationManager.getApplication().getService(ZephyrSettings::class.java)
        }
    }
}