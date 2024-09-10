@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.eitanliu.binding.extension

import android.content.res.Resources
import android.content.res.Resources.Theme
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.TypedValueCompat
import kotlin.math.roundToInt

class ResourcesExt

inline fun Resources.color(@ColorRes id: Int, theme: Theme? = null) =
    ResourcesCompat.getColor(this, id, theme)

inline fun Resources.drawable(@DrawableRes id: Int, theme: Theme? = null) =
    ResourcesCompat.getDrawable(this, id, theme)

inline fun Resources.idp(value: Number): Int = dp2px(value.toFloat()).roundToInt()

inline fun Resources.dp(value: Number): Float = dp2px(value.toFloat())

inline fun Resources.dp2px(value: Float) = applyDimension(TypedValue.COMPLEX_UNIT_DIP, value)

inline fun Resources.isp(value: Number): Int = sp2px(value.toFloat()).roundToInt()

inline fun Resources.sp(value: Number): Float = sp2px(value.toFloat())

inline fun Resources.sp2px(value: Float) = applyDimension(TypedValue.COMPLEX_UNIT_SP, value)

inline fun Resources.px2dp(value: Float) = deriveDimension(
    TypedValue.COMPLEX_UNIT_DIP, value
)

inline fun Resources.px2sp(value: Float) = deriveDimension(
    TypedValue.COMPLEX_UNIT_SP, value
)

inline fun Resources.applyDimension(
    @TypedValueCompat.ComplexDimensionUnit unit: Int, value: Float,
) = TypedValue.applyDimension(unit, value, displayMetrics)

inline fun Resources.deriveDimension(
    @TypedValueCompat.ComplexDimensionUnit unit: Int, pixelValue: Float,
) = TypedValueCompat.deriveDimension(unit, pixelValue, displayMetrics)

inline val Resources.scaledDensity
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1f, displayMetrics)

inline val Resources.density
    get() = displayMetrics.density