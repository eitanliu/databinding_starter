@file:Suppress("unused", "ObsoleteSdkInt", "RestrictedApi")

package com.example.binding.adapter

import android.graphics.Typeface
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.example.base.utils.ResourceUtil
import com.example.binding.annotation.TextStyle
import com.example.binding.annotation.TypefaceStyle

class TextViewAdapter

@BindingAdapter("android:textStyle")
fun TextView.setTextStyle(@TextStyle style: String?) {
    typeface = when (style) {
        TextStyle.BOLD -> Typeface.defaultFromStyle(Typeface.BOLD)
        TextStyle.ITALIC -> Typeface.defaultFromStyle(Typeface.ITALIC)
        TextStyle.BOLD_ITALIC -> Typeface.defaultFromStyle(Typeface.BOLD_ITALIC)
        TextStyle.NORMAL -> Typeface.defaultFromStyle(Typeface.NORMAL)
        else -> Typeface.defaultFromStyle(Typeface.NORMAL)
    }
}

@BindingAdapter("android:textStyle")
fun TextView.setTextStyle(@TypefaceStyle style: Int) {
    typeface = Typeface.defaultFromStyle(style)
}

/**
 * textView格式化
 */
@BindingAdapter("format", "formatArgs", requireAll = true)
fun TextView.setFormatText(@StringRes format: Int, vararg args: String?) {
    this.text = ResourceUtil.getString(format, args)
}
