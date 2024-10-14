package com.eitanliu.binding

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

interface ViewLifecycleOwner : LifecycleOwner {

    fun onAttachedLifecycle()

    fun onDetachedLifecycle()

    class Impl : ViewLifecycleOwner {
        private var _lifecycleRegistry: LifecycleRegistry? = null
        private val lifecycleRegistry: LifecycleRegistry
            get() = _lifecycleRegistry ?: LifecycleRegistry(this).also {
                _lifecycleRegistry = it
            }

        override val lifecycle: Lifecycle
            get() = lifecycleRegistry

        override fun onAttachedLifecycle() {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        }

        override fun onDetachedLifecycle() {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            _lifecycleRegistry = null
        }
    }

}