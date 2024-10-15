package com.eitanliu.binding.state

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.eitanliu.binding.internal.JvmState

interface UiState<T> : JvmState<T> {

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>)

    override fun get(): T

    override fun set(value: T)

    override fun getValue(): T

    override fun setValue(value: T)

    override fun postValue(value: T)

    override operator fun invoke(value: T)

    override fun notifyChange()
}
