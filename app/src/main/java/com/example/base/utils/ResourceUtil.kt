package com.example.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.example.base.BaseApplication
import com.example.binding.annotation.ResourcesId

/**
 * 资源文件工具类
 */
object ResourceUtil {

    fun getBoolean(@BoolRes resId: Int): Boolean {
        return BaseApplication.instance.resources.getBoolean(resId)
    }

    fun getString(@StringRes resId: Int): String {
        return BaseApplication.instance.resources.getString(resId)
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return BaseApplication.instance.resources.getString(resId, formatArgs)
    }

    fun getStringArray(@ArrayRes resId: Int): Array<String?> {
        return BaseApplication.instance.resources.getStringArray(resId)
    }

    fun getColor(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(BaseApplication.instance, resId)
    }

    fun getColorStateList(@ColorRes resId: Int): ColorStateList? {
        return ContextCompat.getColorStateList(BaseApplication.instance, resId)
    }

    fun getDrawable(@DrawableRes resId: Int): Drawable? {
        if (resId == ResourcesId.ID_NULL) return null
        return ContextCompat.getDrawable(BaseApplication.instance, resId)
    }

    fun Int.string() = getString(this)

    fun Int.string(vararg args: Any?) = getString(this).format(*args)

    fun Int.color() = getColor(this)

    fun Int.drawable() = getDrawable(this)

}




