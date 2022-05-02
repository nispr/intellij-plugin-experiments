package com.nispr.intellij.zephyr

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.nispr.intellij.zephyr.di.Dependencies

class ZephyrTranspilerStartupActivity : StartupActivity, DumbAware {

    override fun runActivity(project: Project) {
        Dependencies.initialize()
    }
}