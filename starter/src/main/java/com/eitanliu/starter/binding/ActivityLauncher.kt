@file:Suppress("IntentWithNullActionLaunch")

package com.eitanliu.starter.binding

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import com.eitanliu.starter.binding.model.ActivityLauncherInfo
import com.eitanliu.starter.bundle.BundleDelegate

interface ActivityLauncher {
    fun startActivity(info: ActivityLauncherInfo)

    fun startActivity(
        intent: Intent, clz: Class<*>? = null,
        callback: ActivityResultCallback<ActivityResult>? = null,
    ) {
        startActivity(ActivityLauncherInfo(intent, clz, callback))
    }
}

inline fun <reified T> ActivityLauncher.startActivity(
    callback: ActivityResultCallback<ActivityResult>? = null,
) = startActivity<T>(Intent(), callback)

inline fun <reified T> ActivityLauncher.startActivity(
    bundle: BundleDelegate?,
    callback: ActivityResultCallback<ActivityResult>? = null,
) = startActivity<T>(bundle?.bundle, callback)

inline fun <reified T> ActivityLauncher.startActivity(
    bundle: Bundle?,
    callback: ActivityResultCallback<ActivityResult>? = null,
) = startActivity<T>(Intent().apply { if (bundle != null) putExtras(bundle) }, callback)

inline fun <reified T> ActivityLauncher.startActivity(
    intent: Intent,
    callback: ActivityResultCallback<ActivityResult>? = null,
) = startActivity(intent, T::class.java, callback)