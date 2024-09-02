package com.example.base.mvvm.model

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback

/**
 * activity 页面跳转参数
 * @param clz Class<*> 目标activity
 * @param bundle Bundle? 跳转页面携带的参数Bundle
 * @param callback ActivityResultCallback<ActivityResult>? 目标页面回调
 */
data class ActivityLaunchModel(
    val clz: Class<*>,
    val bundle: Bundle? = null,
    val callback: ActivityResultCallback<ActivityResult>? = null
)