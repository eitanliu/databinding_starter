package com.eitanliu.binding.controller

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import java.util.concurrent.atomic.AtomicBoolean

interface AttachLifecycleOwner : LifecycleOwner {

    fun dispatchAttachedLifecycle()

    fun dispatchDetachedLifecycle()

    fun observe(observer: AttachStateChangeListener) {
        lifecycle.addObserver(object : LifecycleEventObserver {
            val isAttached = AtomicBoolean(false)
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                val state = source.lifecycle.currentState
                if (state > Lifecycle.State.CREATED) {
                    if (isAttached.compareAndSet(false, true)) {
                        observer.onAttachedState(source)
                    }
                } else if (state < Lifecycle.State.INITIALIZED) {
                    if (isAttached.compareAndSet(true, false)) {
                        observer.onDetachedState(source)
                    }
                    source.lifecycle.removeObserver(this)
                }
            }
        })
    }

    class Impl : AttachLifecycleOwner {
        private var _lifecycleRegistry: LifecycleRegistry? = null
        private val lifecycleRegistry: LifecycleRegistry
            get() = _lifecycleRegistry ?: LifecycleRegistry(this).also {
                _lifecycleRegistry = it
            }

        override val lifecycle: Lifecycle
            get() = lifecycleRegistry

        override fun dispatchAttachedLifecycle() {
            if (lifecycleRegistry.currentState < Lifecycle.State.STARTED) {
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            }
        }

        override fun dispatchDetachedLifecycle() {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            _lifecycleRegistry = null
        }
    }

}