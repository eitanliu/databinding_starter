package com.eitanliu.utils.model

import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import com.eitanliu.utils.annotation.FitInsetsMode

class FitWindowInsets(
    val status: Boolean,
    @FitInsetsMode val mode: Int,
    @InsetsType val type: Int,
    val insets: Insets,
)