package com.nispr.intellij.zephyr.di

import com.nispr.intellij.zephyr.api.ApiInjection.StaticApiModule
import com.nispr.intellij.zephyr.api.FetchTestCaseUseCase
import com.nispr.intellij.zephyr.settings.CredentialRepository
import com.nispr.intellij.zephyr.settings.CredentialRepositoryImpl
import com.nispr.intellij.zephyr.transpile.TestCaseCodeGenerator
import com.nispr.intellij.zephyr.transpile.TestCaseCodeGeneratorImpl
import com.nispr.intellij.zephyr.transpile.ZephyrTestCaseTranspiler
import com.nispr.intellij.zephyr.transpile.ZephyrTestCaseTranspilerImpl
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

object Dependencies {
    fun initialize() {
        GlobalContext.startKoin {
            modules(
                ZephyrModule,
                StaticApiModule,
            )
        }
    }

    private val ZephyrModule = module {
        single { FetchTestCaseUseCase(get(), get()) }
        single<ZephyrTestCaseTranspiler> { ZephyrTestCaseTranspilerImpl(get(), get()) }
        single<TestCaseCodeGenerator> { TestCaseCodeGeneratorImpl() }
        single<CredentialRepository> { CredentialRepositoryImpl() }
    }
}