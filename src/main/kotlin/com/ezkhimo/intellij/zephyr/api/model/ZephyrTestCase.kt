package com.ezkhimo.intellij.zephyr.api.model

data class ZephyrTestCase(
    var id: Int,
    var testSteps: Array<ZephyrTestStep>? = null,
)

data class ZephyrTestStep(
    var id: Int ? = null,
    var action: String? = null
)
