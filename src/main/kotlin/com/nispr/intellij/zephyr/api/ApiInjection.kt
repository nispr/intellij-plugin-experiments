package com.nispr.intellij.zephyr.api

import com.nispr.intellij.zephyr.api.model.*
import com.nispr.intellij.zephyr.network.AuthenticationHeaderInterceptor
import com.nispr.intellij.zephyr.settings.ZephyrTranspilerSettings
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInjection {
    val StaticApiModule = module {
        single<ZephyrApi> {
            object : ZephyrApi {
                override suspend fun getTestCase(stepId: String): ZephyrTestCase {
                    return ZephyrTestCase(
                        arrayOf(
                            ZephyrTestStep(13253, 1, "Do this"),
                            ZephyrTestStep(22305, 2, "Do that")
                        )
                    )

                }
            }
        }

        single<JiraApi> {
            object : JiraApi {
                override suspend fun getIssue(issueKey: String) = JiraIssue(
                    "ABC-12345"
                )
            }
        }
    }

    val ApiModule = module {
        single<Retrofit> {
            Retrofit.Builder()
                .baseUrl(ZephyrTranspilerSettings.instance.jiraApiBaseUrl ?: "")
                .client(get())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        single<ZephyrApi> { get<Retrofit>().create(ZephyrApi::class.java) }

        single<JiraApi> { get<Retrofit>().create(JiraApi::class.java) }

        single {
            OkHttpClient
                .Builder()
                .addInterceptor(AuthenticationHeaderInterceptor(get()))
                .build()
        }
    }
}