package com.eitanliu.starter.binding.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.eitanliu.starter.binding.listener.DialogLifecycle
import com.eitanliu.starter.binding.listener.WeakOnCreateListener
import com.eitanliu.starter.binding.listener.WeakOnCancelListener
import com.eitanliu.starter.binding.listener.WeakOnDismissListener
import com.eitanliu.starter.binding.listener.WeakOnShowListener

/**
 * dialog的listener是用handler发送的，导致dialog被持有无法释放
 * 使用WeakReference持有listener解决dialog泄漏问题
 */
open class SafetyBottomDialog : BottomSheetDialog {

    private var onCreateListener: DialogLifecycle.OnCreateListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener
    ) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateListener?.onCreate(this, window!!)
    }

    open fun setOnCreateListener(listener: DialogLifecycle.OnCreateListener?) {
        onCreateListener = WeakOnCreateListener(listener)
    }

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        super.setOnCancelListener(WeakOnCancelListener(listener))
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(WeakOnDismissListener(listener))
    }

    override fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        super.setOnShowListener(WeakOnShowListener(listener))
    }
}