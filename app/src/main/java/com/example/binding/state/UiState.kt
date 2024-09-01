package com.example.binding.state

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.binding.internal.JvmValue

interface UiState<T> : JvmValue<T> {
    //fun observe(owner: LifecycleOwner, observer: (T) -> Unit)
    fun observe(owner: LifecycleOwner, observer: Observer<in T>)

    fun postValue(value: T)

    operator fun invoke(value: T)
}

operator fun UiState<Unit>.invoke() = invoke(Unit)