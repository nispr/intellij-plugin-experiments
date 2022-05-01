package com.nispr.intellij.zephyr.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.nispr.intellij.zephyr.api.FetchTestCaseUseCase
import com.nispr.intellij.zephyr.api.model.ZephyrApi
import com.nispr.intellij.zephyr.api.model.ZephyrTestCase
import com.nispr.intellij.zephyr.api.model.ZephyrTestStep
import com.nispr.intellij.zephyr.settings.ZephyrTranspilerSettings
import com.nispr.intellij.zephyr.transpile.TestCaseCodeGenerator
import com.nispr.intellij.zephyr.transpile.TestCaseCodeGeneratorImpl
import com.nispr.intellij.zephyr.transpile.ZephyrTestCaseTranspiler
import com.nispr.intellij.zephyr.transpile.ZephyrTestCaseTranspilerImpl
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
            .baseUrl(ZephyrTranspilerSettings.instance.jiraApiBaseUrl)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZephyrApi::class.java)
    }
}