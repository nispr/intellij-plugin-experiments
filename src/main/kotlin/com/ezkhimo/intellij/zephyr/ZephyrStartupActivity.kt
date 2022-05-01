package com.ezkhimo.intellij.zephyr

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.koin.core.context.GlobalContext
import com.ezkhimo.intellij.zephyr.di.StaticApiModule
import com.ezkhimo.intellij.zephyr.di.ZephyrModule

class ZephyrStartupActivity : StartupActivity, DumbAware {

    override fun runActivity(project: Project) {
        GlobalContext.startKoin {
            modules(StaticApiModule, ZephyrModule)
        }
    }
}