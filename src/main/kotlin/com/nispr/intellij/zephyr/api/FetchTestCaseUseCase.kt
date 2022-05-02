package com.nispr.intellij.zephyr.api

import com.nispr.intellij.zephyr.api.model.*
import com.nispr.intellij.zephyr.settings.ZephyrTranspilerSettings

class FetchTestCaseUseCase(
    private val zephyrApi: ZephyrApi,
    private val jiraApi: JiraApi
) {

    suspend fun fetchTestCase(issueKey: String): TestCase {
        checkPreconditions()
        val jiraIssue = jiraApi.getIssue(issueKey)
        val zephyrTestCase = zephyrApi.getTestCase(jiraIssue.id)
        return mapToTestCase(jiraIssue, zephyrTestCase)
    }

    private fun mapToTestCase(jiraIssue: JiraIssue, zephyrTestCase: ZephyrTestCase): TestCase {
        fun mapStep(step: ZephyrTestStep) = TestStep(
            step.orderId,
            step.step,
        )

        return TestCase(
            issueId = jiraIssue.id,
            steps = zephyrTestCase.testSteps.map(::mapStep)
        )
    }

    private fun checkPreconditions() {
        if (ZephyrTranspilerSettings.instance.jiraApiBaseUrl.isNullOrBlank()) {
            throw Exception("JIRA API base URL is not set. Please set it in Zephyr Transpiler settings.")
        }
    }
}
