@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.eitanliu.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.util.TypedValueCompat
import com.blankj.utilcode.util.AppUtils
import kotlin.math.roundToInt

class ContextExt

fun Context.contextTree(tree: (context: Context) -> Unit) {
    tree(this)
    if (this is ContextWrapper) baseContext.contextTree(tree)
}

fun Context.baseIf(predicate: (context: Context) -> Boolean): Context? {
    if (predicate(this)) {
        return this
    } else if (this is ContextWrapper) {
        baseContext.baseIf(predicate)
    }
    return null
}

val Context.isAppForeground: Boolean get() = AppUtils.isAppForeground()

inline fun Context.color(@ColorRes id: Int, theme: Resources.Theme? = null) =
    resources.color(id, theme)

inline fun Context.drawable(@DrawableRes id: Int, theme: Resources.Theme? = null) =
    resources.drawable(id, theme)

inline fun Context.idp(value: Number): Int = dp2px(value.toFloat()).roundToInt()

inline fun Context.dp(value: Number): Float = dp2px(value.toFloat())

inline fun Context.dp2px(value: Float) = applyDimension(TypedValue.COMPLEX_UNIT_DIP, value)

inline fun Context.isp(value: Number): Int = sp2px(value.toFloat()).roundToInt()

inline fun Context.sp(value: Number): Float = sp2px(value.toFloat())

inline fun Context.sp2px(value: Float) = applyDimension(TypedValue.COMPLEX_UNIT_SP, value)

inline fun Context.px2dp(value: Float) = deriveDimension(
    TypedValue.COMPLEX_UNIT_DIP, value
)

inline fun Context.px2sp(value: Float) = deriveDimension(
    TypedValue.COMPLEX_UNIT_SP, value
)

inline fun Context.applyDimension(
    @TypedValueCompat.ComplexDimensionUnit unit: Int, value: Float,
) = resources.applyDimension(unit, value)

inline fun Context.deriveDimension(
    @TypedValueCompat.ComplexDimensionUnit unit: Int, pixelValue: Float,
) = resources.deriveDimension(unit, pixelValue)

val Context.defaultTextColor
    get() : Int {
        val attrs = intArrayOf(android.R.attr.textColor)
        val ta = obtainStyledAttributes(android.R.style.TextAppearance, attrs)
        val defaultTextColor = ta.getColor(0, 0)
        ta.recycle()
        return defaultTextColor
    }

val Context.dividerHeight get() = listDivider?.intrinsicHeight ?: idp(1)

val Context.dividerWidth get() = listDivider?.intrinsicWidth ?: idp(1)

val Context.listDivider
    get() = run {
        var listDivider: Drawable? = null
        val dividerAttrs = intArrayOf(android.R.attr.listDivider)
        obtainStyledAttributes(dividerAttrs).apply {
            listDivider = getDrawable(0)
        }.recycle()
        listDivider
    }