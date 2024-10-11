package com.eitanliu.binding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.viewbinding.ViewBinding
import com.eitanliu.binding.extension.baseIf

object ViewBindingUtil {

    inline fun <reified T : ViewBinding> inflate(
        inflater: LayoutInflater, parent: ViewGroup? = null, attachToParent: Boolean = false
    ): T = inflate(T::class.java, inflater, parent, attachToParent)

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    @JvmOverloads
    fun <T : ViewBinding> inflate(
        clazz: Class<T>,
        inflater: LayoutInflater,
        parent: ViewGroup? = null,
        attachToParent: Boolean = false,
    ): T {
        val inflate = clazz.getMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        return inflate.invoke(null, inflater, parent, attachToParent) as T
    }

    inline fun <reified T : ViewBinding> bind(root: View) = bind(T::class.java, root)

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : ViewBinding> bind(clazz: Class<T>, root: View): T {
        val bind = clazz.getMethod("bind", View::class.java)
        return bind.invoke(null, root) as T
    }

    inline var ViewBinding.lifecycleOwnerExt: LifecycleOwner?
        get() = lifecycleOwner
        set(value) {
            lifecycleOwner = value
        }

    var ViewBinding.lifecycleOwner: LifecycleOwner?
        get() = selfLifecycleOwner
            ?: root.findViewTreeLifecycleOwner()
            ?: root.context.baseIf { it is LifecycleOwner } as? LifecycleOwner
        set(value) {
            when (this) {
                is ViewDataBinding -> lifecycleOwner = value
                else -> root.setTag(R.id.lifecycleOwner, value)
            }
        }

    val ViewBinding.selfLifecycleOwner: LifecycleOwner?
        get() = when (this) {
            is ViewDataBinding -> lifecycleOwner
            else -> root.getTag(R.id.lifecycleOwner) as? LifecycleOwner
        }
}
