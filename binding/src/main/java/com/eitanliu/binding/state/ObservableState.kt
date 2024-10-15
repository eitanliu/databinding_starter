package com.eitanliu.binding.state

import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.eitanliu.binding.extension.observeId
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

open class SingleObservableState<T> : MultipleObservableState<T> {

    private val pending = AtomicBoolean(false)
    private val observers = linkedMapOf<OnPropertyChangedCallback, OnPropertyChangedCallback>()
    val isObserved get() = !pending.get()

    constructor(value: T) : super(value)

    constructor() : super()

    override fun addOnPropertyChangedCallback(callback: OnPropertyChangedCallback) {
        super.addOnPropertyChangedCallback(wrapCallback(callback))
    }

    private fun wrapCallback(
        callback: OnPropertyChangedCallback
    ) = object : OnPropertyChangedCallback() {
        init {
            observers[callback] = this
        }

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            if (pending.compareAndSet(true, false)) {
                callback.onPropertyChanged(sender, propertyId)
            }
        }
    }

    override fun removeOnPropertyChangedCallback(callback: OnPropertyChangedCallback) {
        val wrapper = observers.remove(callback)
        if (wrapper != null) {
            super.removeOnPropertyChangedCallback(wrapper)
        } else {
            super.removeOnPropertyChangedCallback(callback)
        }
    }

    override fun set(value: T) {
        pending.set(true)
        super.set(value)
    }

}

open class MultipleObservableState<T> : ObservableField<T>, UiState<T> {

    constructor(value: T) : super(value)

    constructor() : super()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        observeId(owner, Lifecycle.Event.ON_DESTROY) { _, _ ->
            observer.onChanged(value)
        }
    }

    override fun postValue(value: T) {
        MainScope().launch {
            set(value)
        }
    }

    override fun invoke(value: T) {
        set(value)
    }

    override fun notifyChange() {
        super.notifyChange()
    }

    override fun getValue(): T = get()

    override fun setValue(value: T) {
        set(value)
    }

    @Suppress("UNCHECKED_CAST")
    override fun get(): T {
        return super.get() as T
    }

}
