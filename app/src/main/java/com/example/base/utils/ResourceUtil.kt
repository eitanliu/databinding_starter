package com.example.base.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.BoolRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.example.base.BaseApplication
import com.example.base.extension.dp2px
import com.example.base.extension.px2dp
import com.example.base.extension.px2sp
import com.example.base.extension.sp2px
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

    fun @receiver:StringRes Int.string() = getString(this)

    fun @receiver:StringRes Int.string(vararg args: Any?) = getString(this).format(*args)

    fun @receiver:ColorRes Int.color() = getColor(this)

    fun @receiver:ColorRes Int.colorStateList() = getColorStateList(this)

    fun @receiver:DrawableRes Int.drawable() = getDrawable(this)

    fun Float.dp2px() = BaseApplication.instance.dp2px(this)

    fun Float.sp2px() = BaseApplication.instance.sp2px(this)

    fun Float.px2dp() = BaseApplication.instance.px2dp(this)

    fun Float.px2sp() = BaseApplication.instance.px2sp(this)

}




