@file:Suppress("NOTHING_TO_INLINE")

package com.eitanliu.binding.extension

import android.view.View
import androidx.core.app.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.asFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.viewbinding.ViewBinding
import com.eitanliu.binding.ViewBindingUtil.selfLifecycleOwner
import com.eitanliu.utils.baseIf

class ViewBindingExt

var View.dataBinding
    get() = findDataBinding<ViewDataBinding>()
    set(value) = setTag(androidx.databinding.library.R.id.dataBinding, value)

var View.viewBinding
    get() = findViewBinding<ViewBinding>()
    set(value) = setTag(androidx.databinding.library.R.id.dataBinding, value)

val View.bindingFragment get() = findFragment<Fragment>()

val View.bindingFragmentActivity get() = findActivity<FragmentActivity>()

val View.bindingLifecycleOwner get() = findLifecycleOwner<LifecycleOwner>()

inline fun <reified T : ViewDataBinding> View.findDataBinding(self: Boolean = true): T? {
    var view: View? = if (self) this else parent as? View
    while (view != null) {
        val binding = DataBindingUtil.findBinding<ViewDataBinding>(view)
        if (binding is T) return binding
        view = binding?.root?.parent as? View
    }
    return null
}

inline fun <reified T : ViewBinding> View.findViewBinding(self: Boolean = true): T? {
    var view: View? = if (self) this else parent as? View
    while (view != null) {
        val binding = view.getTag(androidx.databinding.library.R.id.dataBinding) as? T
        if (binding != null) return binding
        view = view.parent as? View
    }
    return null
}

inline fun <reified T : ComponentActivity> View.findActivity(
    noinline predicate: (owner: LifecycleOwner?) -> Boolean = { it is T }
): T? = findLifecycleOwner(predicate)

inline fun <reified T : Fragment> View.findFragment(
    noinline predicate: (owner: LifecycleOwner?) -> Boolean = { it?.asFragment() is T }
): T? = findLifecycleOwner<T>(predicate)?.asFragment() as? T

/**
 * 获取LifecycleOwner
 */
inline fun <reified T : LifecycleOwner> View.findLifecycleOwner(
    noinline predicate: (owner: LifecycleOwner?) -> Boolean = { it is T }
): T? {
    return findLifecycleOwner(T::class.java, predicate)
}

@Suppress("UNCHECKED_CAST")
fun <T : LifecycleOwner> View.findLifecycleOwner(
    clazz: Class<T>, predicate: (owner: LifecycleOwner?) -> Boolean = { clazz.isInstance(it) }
): T? {
    fun checkType(lifecycleOwner: LifecycleOwner?): T? {
        return lifecycleOwner.takeIf(predicate) as? T
    }

    var binding = findViewBinding<ViewBinding>()
    var lifecycleOwner: T? = checkType(binding?.selfLifecycleOwner)
    while (lifecycleOwner == null && binding != null) {
        val view = binding.root.parent as? View
        binding = view?.findViewBinding<ViewBinding>(false)
        lifecycleOwner = checkType(binding?.selfLifecycleOwner)
    }
    if (lifecycleOwner == null) {
        lifecycleOwner = checkType(findViewTreeLifecycleOwner())
    }
    if (lifecycleOwner == null) {
        lifecycleOwner = context.baseIf { checkType(it as? LifecycleOwner) != null } as? T
    }
    return lifecycleOwner
}
