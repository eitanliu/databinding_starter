package com.eitanliu.starter.binding.handler

import androidx.activity.OnBackPressedCallback

class OnBackPressedHandler(
    enabled: Boolean,
    var onBackPressed: (() -> Unit)? = null,
) : OnBackPressedCallback(enabled) {
    override fun handleOnBackPressed() {
        onBackPressed?.invoke()
    }
}