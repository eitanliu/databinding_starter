package com.eitanliu.binding

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.viewbinding.ViewBinding
import com.eitanliu.utils.baseIf
import java.lang.ref.Reference
import java.lang.ref.WeakReference

object ViewBindingUtil {

    /**
     * Retrieves a view binding handle in an Activity.
     *
     * ```
     *     private val binding by viewBindings(MainActivityBinding::bind)
     *
     *     override fun onCreate(savedInstanceState: Bundle?) {
     *         super.onCreate(savedInstanceState)
     *         binding.someView.someField = ...
     *     }
     * ```
     */
    inline fun <reified BindingT : ViewBinding> Activity.viewBindings(
        crossinline bind: (View) -> BindingT
    ) = object : Lazy<BindingT> {

        private var cached: BindingT? = null

        override val value: BindingT
            get() = cached ?: bind(
                findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
            ).also {
                cached = it
            }

        override fun isInitialized() = cached != null
    }

    /**
     * Retrieves a view binding handle in a Fragment. The field is available only after
     * [Fragment.onViewCreated].
     *
     * ```
     *     private val binding by viewBindings(HomeFragmentBinding::bind)
     *
     *     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
     *         binding.someView.someField = ...
     *     }
     * ```
     */
    inline fun <reified BindingT : ViewBinding> Fragment.viewBindings(
        crossinline bind: (View) -> BindingT
    ) = object : Lazy<BindingT> {

        private var cached: BindingT? = null

        private val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                cached = null
            }
        }

        override val value: BindingT
            get() = cached ?: bind(requireView()).also {
                viewLifecycleOwner.lifecycle.addObserver(observer)
                cached = it
            }

        override fun isInitialized() = cached != null
    }

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

    var ViewBinding.viewModelTag
        get() = (root.getTag(R.id.viewModel) as? Reference<*>)?.get() as? ViewModel
        set(value) {
            if (this is ViewDataBinding) setVariable(BR.viewModel, value)
            root.setTag(R.id.viewModel, WeakReference(value))
        }

    inline var ViewBinding.lifecycleOwnerExt: LifecycleOwner?
        get() = findLifecycleOwner()
        set(value) {
            lifecycleOwner = value
        }

    var ViewBinding.lifecycleOwner: LifecycleOwner?
        get() = when (this) {
            is ViewDataBinding -> lifecycleOwner
            else -> (root.getTag(R.id.lifecycleOwner) as? Reference<*>)?.get() as? LifecycleOwner
        }
        set(value) {
            when (this) {
                is ViewDataBinding -> lifecycleOwner = value
                else -> root.setTag(R.id.lifecycleOwner, WeakReference(value))
            }
        }

    fun ViewBinding.findLifecycleOwner(): LifecycleOwner? {
        return lifecycleOwner
            ?: root.findViewTreeLifecycleOwner()
            ?: root.context.baseIf { it is LifecycleOwner } as? LifecycleOwner
    }
}
