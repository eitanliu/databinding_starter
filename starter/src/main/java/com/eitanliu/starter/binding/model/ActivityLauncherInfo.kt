package com.eitanliu.starter.binding.model

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback

/**
 * activity 页面跳转参数
 * @param intent Intent? 跳转页面携带的参数Intent
 * @param clz Class<*> 目标activity
 * @param callback ActivityResultCallback<ActivityResult>? 目标页面回调
 */
data class ActivityLauncherInfo(
    val intent: Intent,
    val clz: Class<*>? = null,
    val callback: ActivityResultCallback<ActivityResult>? = null
)