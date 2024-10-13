package com.eitanliu.utils

import android.text.InputFilter
import android.text.Spanned
import java.nio.charset.Charset

/**
 * @param charset [CharsetInstances.GB18030] 中文 = 2字节
 */
@Suppress("MemberVisibilityCanBePrivate")
class ByteLengthFilter @JvmOverloads constructor(
    val maxLength: Int,
    val charset: Charset = CharsetInstances.GB18030,
) : InputFilter {

    private inline val String.byteLength get() = toByteArray(charset).size

    /**
     * @param source 输入内容
     * @param start 输入开始位置
     * @param end 输入结束位置
     * @param dest 原始内容
     * @param dstart 当前焦点开始位置 如果结束位置代表删除或替换
     * @param dend 当前焦点结束位置
     */
    override fun filter(
        source: CharSequence, start: Int, end: Int,
        dest: Spanned, dstart: Int, dend: Int,
    ): CharSequence? {
        // Logcat.msg("$source, $start, $end, $dest, $dstart, $dend")
        val destLength = dest.toString().apply {
            if (dstart < dend) removeRange(dstart, dend)
        }.byteLength
        val keep = maxLength - destLength

        val srcStr = source.toString().apply {
            if (start != 0) removeRange(0, start)
        }
        val srcLength = srcStr.byteLength

        // Logcat.msg("$destLength, $srcLength")
        if (keep <= 0) {
            return ""
        } else if (keep >= srcLength) {
            return null // keep original
        } else {
            var subLength = 0
            var subIndex = start

            for (ch in srcStr) {
                if (subLength < keep) {
                    // Logcat.msg(("${ch.code}, ${ch.toString().byteLength}")
                    // subLength += ch.toString().byteLength
                    subLength += if (ch.code < 128) 1 else 2
                    ++subIndex
                } else {
                    break
                }
            }
            if (subLength > keep) --subIndex

            if (subIndex > 0 && Character.isHighSurrogate(source[subIndex - 1])) {
                --subIndex
                if (subIndex == start) return ""
            }
            return source.subSequence(start, subIndex)
        }
    }
}