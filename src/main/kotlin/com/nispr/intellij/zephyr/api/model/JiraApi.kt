package com.nispr.intellij.zephyr.api.model

import retrofit2.http.GET
import retrofit2.http.Path

interface JiraApi {

    @GET("/issue/{issueKey}")
    suspend fun getIssue(@Path("issueKey") issueKey: String): JiraIssue
}