package com.eitanliu.binding.model

import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import com.eitanliu.binding.annotation.FitInsetsMode

class FitSystemInsets(
    val status: Boolean,
    @FitInsetsMode val mode: Int,
    @InsetsType val type: Int,
    val insets: Insets,
)