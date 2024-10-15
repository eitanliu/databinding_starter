@file:Suppress("unused", "ObsoleteSdkInt", "RestrictedApi")

package com.eitanliu.binding.adapter

import android.graphics.Typeface
import android.view.View
import android.widget.Checkable
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.TypefaceCompat
import androidx.databinding.BindingAdapter
import com.eitanliu.binding.R
import com.eitanliu.binding.annotation.TextStyle
import com.eitanliu.binding.annotation.TypefaceStyle
import com.eitanliu.binding.event.UiEvent
import com.eitanliu.binding.event.UiEventConsumer
import com.eitanliu.binding.listener.CLICK_DELAY_DEFAULT
import com.eitanliu.binding.listener.DebounceClickListener
import com.eitanliu.binding.model.FontFamilyTag
import com.eitanliu.utils.defaultTextColor
import com.eitanliu.utils.getBindingTag
import com.eitanliu.utils.setBindingTag

class TextViewAdapter

@BindingAdapter("textColor")
fun TextView.setTextColor(@ColorRes resId: Int?) {
    if (resId != null && resId != ResourcesCompat.ID_NULL) {
        setTextColor(ContextCompat.getColor(context, resId))
    } else {
        setTextColor(context.defaultTextColor)
    }
}

@BindingAdapter("android:textStyle")
fun TextView.setTextStyle(@TextStyle style: String?) {
    typeface = TextStyle.convert(context, typeface, style)
}

@BindingAdapter("android:textStyle")
fun TextView.setTextStyle(@TypefaceStyle style: Int) {
    typeface = TypefaceCompat.create(context, typeface, style)
}

@BindingAdapter(
    "fontFamily",
    "fontFamilyName",
    requireAll = false
)
fun TextView.setFontFamily(
    @FontRes id: Int,
    name: String? = null,
) {
    val tf = ResourcesCompat.getFont(context, id)
    val newTag = FontFamilyTag(id, name, tf)
    setFontFamily(newTag)
}

@BindingAdapter(
    "typeface",
    "fontFamilyName",
    requireAll = false
)
fun TextView.setFontTypeface(
    typeface: Typeface?,
    name: String? = null,
) {
    val newTag = FontFamilyTag(ResourcesCompat.ID_NULL, name, typeface)
    setFontFamily(newTag)
}

@BindingAdapter("fontFamilyTag", requireAll = false)
fun TextView.setFontFamily(
    tag: FontFamilyTag?,
) {
    if (tag == null) {
        fontFamilyTag = null
        this.typeface = null
        return
    }

    val oldTag = fontFamilyTag
    if (oldTag != null && oldTag.id != tag.id && oldTag.name == tag.name) return
    val newTag = tag.takeIf { it.typeface != null }
        ?: if (tag.id != null) tag.copy(
            typeface = ResourcesCompat.getFont(context, tag.id)
        ) else tag
    fontFamilyTag = newTag
    this.typeface = newTag.typeface
}

@BindingAdapter(
    "fontFamilyChecked",
    "fontFamilyResChecked",
    "fontFamilyNameChecked",
    requireAll = false
)
fun TextView.setFontChecked(
    tag: FontFamilyTag? = null,
    @FontRes resId: Int? = null,
    name: String? = null,
) {
    if (this !is Checkable) return
    val oldTag = fontFamilyTag

    if (tag != null) {
        isChecked = tag == oldTag
    } else if (resId != null || name != null) {
        val sameId = if (resId != null) oldTag?.id == resId else true
        val sameName = if (name != null) oldTag?.name == name else true
        isChecked = sameId && sameName
    } else {
        isChecked = null == oldTag
    }
}

/**
 * textView格式化
 */
@BindingAdapter("format", "formatArgs", requireAll = true)
fun TextView.setFormatText(@StringRes format: Int, vararg args: String?) {
    this.text = resources.getString(format, args)
}

var TextView.fontFamilyTag
    get() = getBindingTag(R.id.fontFamily) as? FontFamilyTag
    set(value) {
        setBindingTag(R.id.fontFamily, value)
    }

/**
 * 定义了 [TextView.onClickFontListener] DataBinding 找不到事件绑定
 * 需要强转指定到 [View.onClickListener]
 */
@BindingAdapter(
    "onClickDelay", "debounce",
    "onRepeatClickEvent", "onClickEvent",
    requireAll = false
)
fun TextView.onClickListener(
    delay: Long? = CLICK_DELAY_DEFAULT, debounce: Boolean? = true,
    onRepeatClick: UiEvent? = null, onClick: UiEvent?,
) = (this as View).onClickListener(delay, debounce, onRepeatClick, onClick)

@BindingAdapter(
    "onClickDelay", "debounce",
    "onRepeatClickEvent", "onClickEvent",
    requireAll = false
)
fun TextView.onClickFontListener(
    delay: Long? = CLICK_DELAY_DEFAULT, debounce: Boolean? = true,
    onRepeatClick: UiEventConsumer<FontFamilyTag?>? = null,
    onClick: UiEventConsumer<FontFamilyTag?>?,
) {
    if ((delay != null && delay <= 0) || debounce == false) {
        setOnClickListener {
            onClick?.invoke(fontFamilyTag)
        }
    } else {
        setOnClickListener(DebounceClickListener(delay ?: CLICK_DELAY_DEFAULT, {
            onRepeatClick?.invoke(fontFamilyTag)
        }) {
            onClick?.invoke(fontFamilyTag)
        })
    }
}
