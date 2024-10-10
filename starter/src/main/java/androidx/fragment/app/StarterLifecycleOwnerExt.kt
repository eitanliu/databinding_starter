package androidx.fragment.app

import android.content.Context
import androidx.activity.ComponentDialog
import androidx.core.app.ComponentActivity
import androidx.lifecycle.LifecycleOwner

class StarterLifecycleOwnerExt

@Suppress("RestrictedApi")
val LifecycleOwner.context: Context?
    get() {
        return when (this) {
            is ComponentActivity -> {
                this
            }

            is Fragment -> {
                context
            }

            is FragmentViewLifecycleOwner -> {
                fragment?.context
            }

            is ComponentDialog -> {
                context
            }

            else -> null
        }
    }

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
                fragment?.childFragmentManager
            }

            else -> null
        }
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
