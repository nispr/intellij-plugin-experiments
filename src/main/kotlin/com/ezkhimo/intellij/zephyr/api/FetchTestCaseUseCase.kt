package com.ezkhimo.intellij.zephyr.api

import com.ezkhimo.intellij.zephyr.api.model.ZephyrApi
import com.ezkhimo.intellij.zephyr.api.model.ZephyrTestCase

// TODO Once API is known, add real mapping
class FetchTestCaseUseCase(private val api: ZephyrApi) {
    suspend fun fetchTestCase(testCaseId: String): TestCase {
        return mapToTestCase(api.getTestCase(testCaseId))
    }

    private fun mapToTestCase(zephyrTestCase: ZephyrTestCase): TestCase {
        return TestCase(
            storyId = "ABC-12345",
            steps = listOf(
                TestStep(
                    id = 1,
                    action = "Mach dies"
                ),
                TestStep(
                    id = 2,
                    action = "Mach das"
                ),
            )
        )
    }
}