package com.eitanliu.starter.binding.listener

import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import com.eitanliu.binding.extension.refSoft

/**
 * OnDismissListener包装类，用于处理内存泄露的问题
 */
class WeakOnDismissListener(listener: OnDismissListener?) : OnDismissListener {

    private val refListener = listener.refSoft()

    override fun onDismiss(dialog: DialogInterface?) {
        refListener.get()?.onDismiss(dialog)
    }
}
