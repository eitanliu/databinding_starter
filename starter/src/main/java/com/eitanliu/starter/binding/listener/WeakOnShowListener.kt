package com.eitanliu.starter.binding.listener

import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import com.eitanliu.utils.refWeak

/**
 * OnShowListener包装类，用于处理内存泄露的问题
 */
class WeakOnShowListener(listener: OnShowListener?) : OnShowListener {

    private val refListener = listener.refWeak()

    override fun onShow(dialog: DialogInterface?) {
        refListener.get()?.onShow(dialog)
    }
}
