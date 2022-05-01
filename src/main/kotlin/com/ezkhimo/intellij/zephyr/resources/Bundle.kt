package com.ezkhimo.intellij.zephyr.resources
import java.util.*

object Bundle {
    fun get(): ResourceBundle = ResourceBundle.getBundle("CodeGenBundle", Locale.ENGLISH)
}

fun getString(key: String): String = Bundle.get().getString(key) ?: ""