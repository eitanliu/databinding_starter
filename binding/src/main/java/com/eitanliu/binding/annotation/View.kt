package com.eitanliu.binding.annotation

import android.graphics.Typeface
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.core.content.res.ResourcesCompat

@IntDef(FitInsetsMode.MARGIN, FitInsetsMode.PADDING)
@Retention(AnnotationRetention.SOURCE)
annotation class FitInsetsMode {
    companion object {
        const val MARGIN = 0
        const val PADDING = 1
    }
}

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
    }
}

@IntDef(Typeface.NORMAL, Typeface.BOLD, Typeface.ITALIC, Typeface.BOLD_ITALIC)
@Retention(AnnotationRetention.SOURCE)
annotation class TypefaceStyle