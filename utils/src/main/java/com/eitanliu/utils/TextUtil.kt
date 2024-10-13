package com.eitanliu.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.ColorInt
import org.json.JSONObject
import java.security.MessageDigest

object TextUtil {
    /**
     * 获取字符串字节长度
     * cn = 2 byte, en = 1 byte
     */
    inline val String.byteLength get() = toByteArray(CharsetInstances.GB18030).size

    // 可点击文本
    fun SpannableStringBuilder.append(
        text: CharSequence, @ColorInt color: Int,
        click: (widget: View?) -> Unit
    ) {
        val start = length
        append(text)
        setSpan(object : ClickableSpan() {

            override fun onClick(widget: View) {
                click.invoke(widget)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = color
                ds.isUnderlineText = false
            }
        }, start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun CharSequence.toSpannable(
        @ColorInt color: Int, click: (widget: View?) -> Unit
    ) = SpannableString(this).apply {
        setSpan(object : ClickableSpan() {

            override fun onClick(widget: View) {
                click.invoke(widget)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = color
                ds.isUnderlineText = false
            }
        }, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun String.digest(
        instance: MessageDigest = MessageDigestInstances.SHA256
    ) = run {
        val digest: ByteArray = instance.digest(toByteArray())
        digest.joinToString("") { "%02x".format(it) }
    }

    fun formatJson(json: String?, indent: Int = 2): String {
        if (null == json || "" == json) return ""
        return JSONObject(json).toString(indent)
    }
}