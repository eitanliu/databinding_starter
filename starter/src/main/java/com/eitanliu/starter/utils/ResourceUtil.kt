package com.eitanliu.starter.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.BoolRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.eitanliu.binding.annotation.ResourcesId
import com.eitanliu.binding.extension.dp2px
import com.eitanliu.binding.extension.px2dp
import com.eitanliu.binding.extension.px2sp
import com.eitanliu.binding.extension.sp2px
import com.eitanliu.starter.ApplicationProvider

/**
 * 资源文件工具类
 */
object ResourceUtil {

    fun getBoolean(@BoolRes resId: Int): Boolean {
        return ApplicationProvider.instance.resources.getBoolean(resId)
    }

    fun getString(@StringRes resId: Int): String {
        return ApplicationProvider.instance.resources.getString(resId)
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return ApplicationProvider.instance.resources.getString(resId, formatArgs)
    }

    fun getStringArray(@ArrayRes resId: Int): Array<String?> {
        return ApplicationProvider.instance.resources.getStringArray(resId)
    }

    fun getColor(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(ApplicationProvider.instance, resId)
    }

    fun getColorStateList(@ColorRes resId: Int): ColorStateList? {
        return ContextCompat.getColorStateList(ApplicationProvider.instance, resId)
    }

    fun getDrawable(@DrawableRes resId: Int): Drawable? {
        if (resId == ResourcesId.ID_NULL) return null
        return ContextCompat.getDrawable(ApplicationProvider.instance, resId)
    }

    fun @receiver:StringRes Int.string() = getString(this)

    fun @receiver:StringRes Int.string(vararg args: Any?) = getString(this).format(*args)

    fun @receiver:ColorRes Int.color() = getColor(this)

    fun @receiver:ColorRes Int.colorStateList() = getColorStateList(this)

    fun @receiver:DrawableRes Int.drawable() = getDrawable(this)

    fun Float.dp2px() = ApplicationProvider.instance.dp2px(this)

    fun Float.sp2px() = ApplicationProvider.instance.sp2px(this)

    fun Float.px2dp() = ApplicationProvider.instance.px2dp(this)

    fun Float.px2sp() = ApplicationProvider.instance.px2sp(this)

}




