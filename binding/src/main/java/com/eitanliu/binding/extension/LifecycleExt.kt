package com.eitanliu.binding.extension

import android.content.Context
import androidx.activity.ComponentDialog
import androidx.core.app.ComponentActivity
import androidx.fragment.app.asFragment
import androidx.fragment.app.isFragment
import androidx.fragment.app.isFragmentView
import androidx.lifecycle.LifecycleOwner

class LifecycleExt


@Suppress("RestrictedApi")
val LifecycleOwner.context: Context?
    get() {
        return when {
            this is ComponentActivity -> {
                this
            }

            isFragment() || isFragmentView() -> {
                asFragment()?.context
            }

            this is ComponentDialog -> {
                context
            }

            else -> null
        }
    }