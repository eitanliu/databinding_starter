package androidx.fragment.app

import androidx.lifecycle.LifecycleOwner

class StarterLifecycleOwnerExt

val LifecycleOwner.fragmentManager: FragmentManager?
    get() {
        return when (this) {
            is FragmentActivity -> {
                supportFragmentManager
            }

            is Fragment -> {
                childFragmentManager
            }

            is FragmentViewLifecycleOwner -> {
                val field = FragmentViewLifecycleOwner::class.java.getDeclaredField("mFragment")
                field.isAccessible = true
                val f = field.get(this) as? Fragment
                f?.childFragmentManager
            }

            else -> null
        }
    }
