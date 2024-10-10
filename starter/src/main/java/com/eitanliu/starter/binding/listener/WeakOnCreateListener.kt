package com.eitanliu.starter.binding.listener

import android.content.DialogInterface
import android.view.Window
import com.eitanliu.binding.extension.refWeak

class WeakOnCreateListener(listener: DialogLifecycle.OnCreateListener?) :
    DialogLifecycle.OnCreateListener {

    private val refListener = listener.refWeak()

    override fun onCreate(dialog: DialogInterface, window: Window) {
        refListener.get()?.onCreate(dialog, window)
    }
}
