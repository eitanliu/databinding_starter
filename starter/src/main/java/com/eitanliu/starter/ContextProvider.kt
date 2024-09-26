package com.eitanliu.starter

import android.content.Context

@Suppress("StaticFieldLeak")
object ContextProvider {

    internal lateinit var instance: Context
        private set

    fun register(context: Context) {
        this.instance = context
    }
}