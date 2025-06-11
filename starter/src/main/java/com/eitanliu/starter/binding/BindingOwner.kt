package com.eitanliu.starter.binding

import com.eitanliu.utils.refWeak
import java.lang.ref.Reference

interface BindingOwner {
    val host: BindingHost?

    fun bind(host: BindingHost): BindingOwner

    class Impl : BindingOwner {

        constructor()

        constructor(host: BindingHost) {
            bind(host)
        }

        private var _ref: Reference<BindingHost>? = null

        override val host: BindingHost? get() = _ref?.get()

        override fun bind(host: BindingHost): BindingOwner {
            _ref = host.refWeak()
            return this
        }
    }
}