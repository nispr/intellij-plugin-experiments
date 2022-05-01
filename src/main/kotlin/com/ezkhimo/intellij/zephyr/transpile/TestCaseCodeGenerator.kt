package com.ezkhimo.intellij.zephyr.transpile

import com.squareup.kotlinpoet.FunSpec
import org.junit.Test
import com.ezkhimo.intellij.zephyr.api.TestCase

interface TestCaseCodeGenerator {
    fun generateTestCode(testCase: TestCase): String
}

class TestCaseCodeGeneratorImpl : TestCaseCodeGenerator {

    override fun generateTestCode(testCase: TestCase): String {
        return createTestCode(testCase)
    }

    private fun createTestCode(testCase: TestCase): String {
        return sanitizeTestCode(
            FunSpec.builder("test")
                .addAnnotation(Test::class)
                .addTestSteps(testCase)
                .build()
                .toString()
        )
    }

    private fun sanitizeTestCode(testCode: String): String {
        // KotlinPoet ensures compatibility with explicit API mode
        // which adds 'public' modifier and 'kotlin.Unit' as return value.
        // We don't need it.
        return testCode
            .removePrefix("$MODIFIER_PUBLIC ")
            .replace(RETURN_DECLARATION_UNIT, "")
    }

    private fun FunSpec.Builder.addTestSteps(testCase: TestCase): FunSpec.Builder = apply {
        for ((index, step) in testCase.steps.withIndex()) {
            addCode(
                """
                step(${step.id}) {
                    // ${step.action}
                }
                """
                    .trim()
                    .let { if (index == testCase.steps.lastIndex) it else "$it\n" }
            )
        }
    }

    companion object {
        private const val MODIFIER_PUBLIC = "public"
        private const val RETURN_DECLARATION_UNIT = ": kotlin.Unit"
    }
}
