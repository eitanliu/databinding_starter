package com.example.base

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV

open class BaseApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var instance: BaseApplication

        val DEBUG get() = BuildConfig.DEBUG
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
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