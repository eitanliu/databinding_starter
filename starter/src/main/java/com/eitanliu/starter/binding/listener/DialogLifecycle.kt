package com.eitanliu.starter.binding.listener

import android.content.DialogInterface
import android.view.Window

interface DialogLifecycle {
    fun interface OnCreateListener {
        fun onCreate(dialog: DialogInterface, window: Window)
    }
}
