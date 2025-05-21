@file:Suppress("NOTHING_TO_INLINE")

package androidx.fragment.app

import androidx.lifecycle.LifecycleOwner

class BindingLifecycleOwnerExt

val LifecycleOwner.childFragmentManager: FragmentManager?
    get() {
        return when (this) {
            is FragmentActivity -> {
                supportFragmentManager
            }

            is Fragment -> {
                childFragmentManager
            }

            is FragmentViewLifecycleOwner -> {
                fragment?.childFragmentManager
            }

            else -> null
        }
    }

val LifecycleOwner.parentFragmentManager: FragmentManager?
    get() {
        return when (this) {
            is FragmentActivity -> {
                supportFragmentManager
            }

            is Fragment -> {
                parentFragmentManager
            }

            is FragmentViewLifecycleOwner -> {
                fragment?.parentFragmentManager
            }

            else -> null
        }
    }

inline fun LifecycleOwner.isFragment(): Boolean {
    return this is Fragment
}

inline fun LifecycleOwner.isFragmentView(): Boolean {
    return this is FragmentViewLifecycleOwner
}

fun LifecycleOwner.asFragment(): Fragment? {
    return when (this) {
        is Fragment -> this
        is FragmentViewLifecycleOwner -> fragment
        else -> null
    }
}

private val FragmentViewLifecycleOwner.fragment: Fragment?
    get() {
        val field = FragmentViewLifecycleOwner::class.java.getDeclaredField("mFragment")
        field.isAccessible = true
        return field.get(this) as? Fragment
    }
