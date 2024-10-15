package com.eitanliu.binding.annotation

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.core.graphics.TypefaceCompat

@StringDef(TextStyle.BOLD, TextStyle.ITALIC, TextStyle.BOLD_ITALIC, TextStyle.NORMAL)
@Retention(AnnotationRetention.SOURCE)
annotation class TextStyle {
    companion object {
        const val BOLD = "bold"
        const val ITALIC = "italic"
        const val BOLD_ITALIC = "bold_italic"
        const val NORMAL = "normal"

        fun convert(
            context: Context,
            family: Typeface? = null,
            @TextStyle style: String? = null,
        ) = when (style) {
            BOLD -> TypefaceCompat.create(context, family, Typeface.BOLD)
            ITALIC -> TypefaceCompat.create(context, family, Typeface.ITALIC)
            BOLD_ITALIC -> TypefaceCompat.create(context, family, Typeface.BOLD_ITALIC)
            NORMAL -> TypefaceCompat.create(context, family, Typeface.NORMAL)
            else -> TypefaceCompat.create(context, family, Typeface.NORMAL)
        }
    }
}

@IntDef(Typeface.NORMAL, Typeface.BOLD, Typeface.ITALIC, Typeface.BOLD_ITALIC)
@Retention(AnnotationRetention.SOURCE)
annotation class TypefaceStyle