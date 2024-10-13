package com.eitanliu.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.BoolRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

/**
 * 资源文件工具类
 */
object ResourcesUtil {

    fun getBoolean(@BoolRes resId: Int): Boolean {
        return ContextProvider.instance.resources.getBoolean(resId)
    }

    fun getString(@StringRes resId: Int): String {
        return ContextProvider.instance.resources.getString(resId)
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return ContextProvider.instance.resources.getString(resId, formatArgs)
    }

    fun getStringArray(@ArrayRes resId: Int): Array<String?> {
        return ContextProvider.instance.resources.getStringArray(resId)
    }

    fun getColor(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(ContextProvider.instance, resId)
    }

    fun getColorStateList(@ColorRes resId: Int): ColorStateList? {
        return ContextCompat.getColorStateList(ContextProvider.instance, resId)
    }

    fun getDrawable(@DrawableRes resId: Int): Drawable? {
        if (resId == ResourcesCompat.ID_NULL) return null
        return ContextCompat.getDrawable(ContextProvider.instance, resId)
    }

    fun @receiver:StringRes Int.string() = getString(this)

    fun @receiver:StringRes Int.string(vararg args: Any?) = getString(this).format(*args)

    fun @receiver:ColorRes Int.color() = getColor(this)

    fun @receiver:ColorRes Int.colorStateList() = getColorStateList(this)

    fun @receiver:DrawableRes Int.drawable() = getDrawable(this)

    fun Float.dp2px() = ContextProvider.instance.dp2px(this)

    fun Float.sp2px() = ContextProvider.instance.sp2px(this)

    fun Float.px2dp() = ContextProvider.instance.px2dp(this)

    fun Float.px2sp() = ContextProvider.instance.px2sp(this)

}




