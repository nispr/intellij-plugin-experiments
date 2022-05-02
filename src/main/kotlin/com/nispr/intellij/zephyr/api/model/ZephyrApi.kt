package com.nispr.intellij.zephyr.api.model

import retrofit2.http.GET
import retrofit2.http.Path

interface ZephyrApi {

    @GET("/zapi/latest/teststep/{jiraIssueId}")
    suspend fun getTestCase(@Path("jiraIssueId") stepId: String): ZephyrTestCase
}

