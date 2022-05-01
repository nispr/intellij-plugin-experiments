package com.ezkhimo.intellij.zephyr.action.ui

import com.intellij.openapi.ui.InputValidatorEx
import com.ezkhimo.intellij.zephyr.resources.Bundle
import com.ezkhimo.intellij.zephyr.resources.getString
import java.util.regex.Pattern

class StoryIdInputValidator : InputValidatorEx {
    override fun canClose(inputString: String?)= Pattern.matches("[a-zA-Z]+-[0-9]+", inputString)

    override fun checkInput(inputString: String?) = Pattern.matches("[a-zA-Z]+-[0-9]+", inputString)

    override fun getErrorText(inputString: String?): String = getString("zephyr.action.new.file.error.story.format")
}