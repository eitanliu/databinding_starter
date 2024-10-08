package com.eitanliu.starter.binding.listener

import android.content.DialogInterface
import android.content.DialogInterface.OnCancelListener
import com.eitanliu.binding.extension.refWeak

/**
 * OnCancelListener包装类，用于处理内存泄露的问题
 */
class WeakOnCancelListener(listener: OnCancelListener?) : OnCancelListener {

    private val refListener = listener.refWeak()

    override fun onCancel(dialog: DialogInterface?) {
        refListener.get()?.onCancel(dialog)
    }
}
