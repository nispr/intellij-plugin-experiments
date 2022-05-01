package com.nispr.intellij.zephyr.api.model

import retrofit2.http.GET
import retrofit2.http.Query

// TODO Fix API URL etc
interface ZephyrApi {

    @GET("zapi/latest/test/count?projectId=&versionId=&groupFld")
    suspend fun getTestCase(@Query("storyKey") storyKey: String): ZephyrTestCase
}