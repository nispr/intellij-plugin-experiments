package com.ezkhimo.intellij.zephyr.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.ezkhimo.intellij.zephyr.api.FetchTestCaseUseCase
import com.ezkhimo.intellij.zephyr.api.model.ZephyrApi
import com.ezkhimo.intellij.zephyr.api.model.ZephyrTestCase
import com.ezkhimo.intellij.zephyr.api.model.ZephyrTestStep
import com.ezkhimo.intellij.zephyr.settings.ZephyrSettings
import com.ezkhimo.intellij.zephyr.transpile.TestCaseCodeGenerator
import com.ezkhimo.intellij.zephyr.transpile.TestCaseCodeGeneratorImpl
import com.ezkhimo.intellij.zephyr.transpile.ZephyrTestCaseTranspiler
import com.ezkhimo.intellij.zephyr.transpile.ZephyrTestCaseTranspilerImpl
import okhttp3.OkHttpClient

val ZephyrModule = module {
    single { FetchTestCaseUseCase(get()) }
    single<ZephyrTestCaseTranspiler> { ZephyrTestCaseTranspilerImpl(get(), get()) }
    single<TestCaseCodeGenerator> { TestCaseCodeGeneratorImpl() }
}

val StaticApiModule = module {
    single<ZephyrApi> {
        object : ZephyrApi {
            override suspend fun getTestCase(storyKey: String): ZephyrTestCase {
                return ZephyrTestCase(
                    235, arrayOf(
                        ZephyrTestStep(1, "Mach dies"),
                        ZephyrTestStep(2, "Mach das")
                    )
                )

            }
        }
    }
}

val ApiModule = module {
    single<ZephyrApi> {
        Retrofit
            .Builder()
            .baseUrl(ZephyrSettings.instance.jiraApiBaseUrl)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZephyrApi::class.java)
    }
}