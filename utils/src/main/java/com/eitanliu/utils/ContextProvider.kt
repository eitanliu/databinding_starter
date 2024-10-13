package com.eitanliu.utils

import android.content.Context

@Suppress("StaticFieldLeak")
object ContextProvider {

    lateinit var instance: Context
        private set

    fun register(context: Context) {
        instance = context
    }
}