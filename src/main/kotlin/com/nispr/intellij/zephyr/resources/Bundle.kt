package com.nispr.intellij.zephyr.resources
import java.util.*

object Bundle {
    fun get(): ResourceBundle = ResourceBundle.getBundle("ZephyrTranspilerBundle", Locale.ENGLISH)
}

fun getString(key: String): String = Bundle.get().getString(key) ?: ""