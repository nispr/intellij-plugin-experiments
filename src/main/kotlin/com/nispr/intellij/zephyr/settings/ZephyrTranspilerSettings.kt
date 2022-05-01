package com.nispr.intellij.zephyr.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "org.intellij.sdk.settings.AppSettingsState", storages = [Storage("ZephyrPluginSettings.xml")])
class ZephyrTranspilerSettings : PersistentStateComponent<ZephyrTranspilerSettings> {

    var jiraApiBaseUrl: String? = "https://localhost/jira/rest/api/2"
    var storyPrefix: String? = ""

    override fun getState(): ZephyrTranspilerSettings = this

    override fun loadState(state: ZephyrTranspilerSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance: ZephyrTranspilerSettings by lazy {
            ApplicationManager.getApplication().getService(ZephyrTranspilerSettings::class.java)
        }
    }
}