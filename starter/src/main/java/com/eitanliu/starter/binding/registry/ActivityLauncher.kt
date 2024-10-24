package com.eitanliu.starter.binding.registry

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import com.eitanliu.starter.binding.model.ActivityLaunchModel
import com.eitanliu.starter.bundle.BundleDelegate

interface ActivityLauncher {
    fun startActivity(model: ActivityLaunchModel)

    fun startActivity(
        clz: Class<*>, bundle: Bundle? = null,
        callback: ActivityResultCallback<ActivityResult>? = null,
    ) {
        startActivity(ActivityLaunchModel(clz, bundle, callback))
    }
}

inline fun <reified T> ActivityLauncher.startActivity(
    callback: ActivityResultCallback<ActivityResult>? = null,
) = startActivity<T>(null as Bundle?, callback)

inline fun <reified T> ActivityLauncher.startActivity(
    bundle: BundleDelegate? = null,
    callback: ActivityResultCallback<ActivityResult>? = null,
) = startActivity<T>(bundle?.bundle, callback)

inline fun <reified T> ActivityLauncher.startActivity(
    bundle: Bundle? = null,
    callback: ActivityResultCallback<ActivityResult>? = null,
) = startActivity(T::class.java, bundle, callback)