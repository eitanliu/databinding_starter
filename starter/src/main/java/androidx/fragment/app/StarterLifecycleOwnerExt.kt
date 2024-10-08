package androidx.fragment.app

import android.content.Context
import androidx.activity.ComponentDialog
import androidx.core.app.ComponentActivity
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
                val field = FragmentViewLifecycleOwner::class.java.getDeclaredField("mFragment")
                field.isAccessible = true
                val f = field.get(this) as? Fragment
                f?.context
            }

            is ComponentDialog -> {
                context
            }

            else -> null
        }
    }
