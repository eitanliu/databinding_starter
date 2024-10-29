package com.eitanliu.binding.annotation

import androidx.annotation.IntDef

@IntDef(FitInsetsMode.MARGIN, FitInsetsMode.PADDING)
@Retention(AnnotationRetention.SOURCE)
annotation class FitInsetsMode {
    companion object {
        const val MARGIN = 0
        const val PADDING = 1
    }
}