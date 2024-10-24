package com.eitanliu.starter.binding

import com.eitanliu.utils.refWeak
import java.lang.ref.Reference

interface BindingOwner {
    val delegate: BindingDelegate?

    fun bind(delegate: BindingDelegate)

    class Impl : BindingOwner {

        private var _ref: Reference<BindingDelegate>? = null

        override val delegate: BindingDelegate? get() = _ref?.get()

        override fun bind(delegate: BindingDelegate) {
            _ref = delegate.refWeak()
        }
    }
}