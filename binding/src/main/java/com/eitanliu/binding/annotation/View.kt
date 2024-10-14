package com.eitanliu.binding.annotation

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.TypefaceCompat

@Retention(AnnotationRetention.SOURCE)
annotation class ResourcesId {
    companion object {
        const val ID_NULL = ResourcesCompat.ID_NULL
    }
}

@StringDef(TextStyle.BOLD, TextStyle.ITALIC, TextStyle.BOLD_ITALIC, TextStyle.NORMAL)
@Retention(AnnotationRetention.SOURCE)
annotation class TextStyle {
    companion object {
        const val BOLD = "bold"
        const val ITALIC = "italic"
        const val BOLD_ITALIC = "bold_italic"
        const val NORMAL = "normal"

        fun convert(
            @TextStyle style: String?,
            typeface: Typeface,
            context: Context,
        ) = when (style) {
            BOLD -> TypefaceCompat.create(context, typeface, Typeface.BOLD)
            ITALIC -> TypefaceCompat.create(context, typeface, Typeface.ITALIC)
            BOLD_ITALIC -> TypefaceCompat.create(context, typeface, Typeface.BOLD_ITALIC)
            NORMAL -> TypefaceCompat.create(context, typeface, Typeface.NORMAL)
            else -> TypefaceCompat.create(context, typeface, Typeface.NORMAL)
        }
    }
}

@IntDef(Typeface.NORMAL, Typeface.BOLD, Typeface.ITALIC, Typeface.BOLD_ITALIC)
@Retention(AnnotationRetention.SOURCE)
annotation class TypefaceStyle