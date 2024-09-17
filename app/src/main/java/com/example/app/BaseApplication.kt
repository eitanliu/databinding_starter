package com.example.app

import android.app.Application
import android.content.Context
import com.eitanliu.starter.ApplicationProvider
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class BaseApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var instance: BaseApplication

        val DEBUG get() = BuildConfig.DEBUG
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        ApplicationProvider.register(this)
        instance = this
    }

    override fun onCreate() {
        beforeCreate()
        super.onCreate()
    }

    open fun beforeCreate() {
        MMKV.initialize(this)
    }

}