package com.nispr.intellij.zephyr.api.model

import com.google.gson.annotations.SerializedName

data class ZephyrTestCase(
    @SerializedName("stepBeanCollection")
    var testSteps: Array<ZephyrTestStep>,
)

data class ZephyrTestStep(
    var id: Int,
    var orderId: Int,
    var step: String
)
