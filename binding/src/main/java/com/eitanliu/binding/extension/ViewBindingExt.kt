@file:Suppress("NOTHING_TO_INLINE")

package com.eitanliu.binding.extension

import android.util.SparseArray
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.viewbinding.ViewBinding
import com.eitanliu.binding.R

class ViewBindingExt

var View.dataBinding
    get() = findDataBinding<ViewDataBinding>()
    set(value) = setTag(androidx.databinding.library.R.id.dataBinding, value)

var View.viewBinding
    get() = findViewBinding<ViewBinding>()
    set(value) = setTag(androidx.databinding.library.R.id.dataBinding, value)

@Suppress("UNCHECKED_CAST")
val View.bindingTags: SparseArray<Any?>
    get() = run {
        getTag(R.id.bindingTags) as? SparseArray<Any?> ?: SparseArray<Any?>().also {
            setTag(R.id.bindingTags, it)
        }
    }

inline fun View.setBindingTag(key: Int, tag: Any?) = bindingTags.put(key, tag)

inline fun <reified T> View.getBindingTag(key: Int) = bindingTags.get(key) as? T

inline fun <reified T : ViewDataBinding> View.findDataBinding(): T? {
    var view: View? = this
    while (view != null) {
        val binding = DataBindingUtil.findBinding<ViewDataBinding>(view)
        if (binding is T) return binding
        view = binding?.root?.parent as? View
    }
    return null
}

inline fun <reified T : ViewBinding> View.findViewBinding(): T? {
    var view: View? = this
    while (view != null) {
        val binding = view.getTag(androidx.databinding.library.R.id.dataBinding) as? T
        if (binding != null) return binding
        view = view.parent as? View
    }
    return null
}

/**
 * 获取LifecycleOwner
 */
fun View.findLifecycleOwner(): LifecycleOwner? {
    val binding = findDataBinding<ViewDataBinding>()
    var lifecycleOwner = binding?.lifecycleOwner
    if (lifecycleOwner == null) {
        lifecycleOwner = findViewTreeLifecycleOwner()
    }
    if (lifecycleOwner == null) {
        lifecycleOwner = context.takeIf { it is LifecycleOwner } as? LifecycleOwner
    }
    return lifecycleOwner
}
