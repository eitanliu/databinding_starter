package com.eitanliu.starter

import android.app.Application

object ApplicationProvider {
    internal lateinit var instance: Application
        private set

    fun register(
        application: Application
    ) {
        this.instance = application
    }
}