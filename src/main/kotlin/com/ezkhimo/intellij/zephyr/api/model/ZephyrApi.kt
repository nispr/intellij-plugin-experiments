package com.ezkhimo.intellij.zephyr.api.model

import retrofit2.http.GET
import retrofit2.http.Query

// TODO Once API is known, add real mapping
interface ZephyrApi {

    @GET("/jira/rest/zapi/latest/test/count?projectId=&versionId=&groupFld")
    suspend fun getTestCase(@Query("storyKey") storyKey: String): ZephyrTestCase
}