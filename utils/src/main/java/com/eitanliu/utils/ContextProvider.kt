package com.eitanliu.utils

import android.content.Context
import com.blankj.utilcode.util.Utils

@Suppress("StaticFieldLeak")
object ContextProvider {

    private var _instance: Context? = null
    val instance: Context
        get() = _instance ?: Utils.getApp()

    fun register(context: Context) {
        _instance = context
    }
}