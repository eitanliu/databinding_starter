package com.eitanliu.binding.controller

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.atomic.AtomicBoolean

abstract class ViewLifecycleController : ViewController, View.OnAttachStateChangeListener {

    val viewLifecycleOwner: AttachLifecycleOwner = AttachLifecycleOwner.Impl()

    val currentState get() = viewLifecycleOwner.lifecycle.currentState

    protected fun initAttachLifecycle() {
        view.addOnAttachStateChangeListener(this)
        if (view.isAttachedToWindow) {
            viewLifecycleOwner.dispatchAttachedLifecycle()
        }
    }

    override fun minusKey(key: ViewController.Key<*>): ViewController {
        if (this.key == key) {
            if (currentState > Lifecycle.State.CREATED) onViewDetachedFromWindow(view)
            view.removeOnAttachStateChangeListener(this)
        }
        return super.minusKey(key)
    }

    override fun onViewAttachedToWindow(v: View) {
        viewLifecycleOwner.dispatchAttachedLifecycle()
    }

    override fun onViewDetachedFromWindow(v: View) {
        viewLifecycleOwner.dispatchDetachedLifecycle()
    }

    fun observe(observer: View.OnAttachStateChangeListener) {
        val lifecycle = viewLifecycleOwner.lifecycle
        lifecycle.addObserver(object : LifecycleEventObserver {
            val isAttached = AtomicBoolean(false)
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                val state = source.lifecycle.currentState
                if (state > Lifecycle.State.CREATED) {
                    if (isAttached.compareAndSet(false, true)) {
                        observer.onViewAttachedToWindow(view)
                    }
                } else if (state < Lifecycle.State.INITIALIZED) {
                    if (isAttached.compareAndSet(true, false)) {
                        observer.onViewDetachedFromWindow(view)
                    }
                    source.lifecycle.removeObserver(this)
                }
            }
        })
    }

}