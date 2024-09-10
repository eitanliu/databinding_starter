package com.eitanliu.binding.state

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

typealias SingleLiveStateBoolean = SingleLiveState<Boolean>
typealias SingleLiveStateByte = SingleLiveState<Byte>
typealias SingleLiveStateChart = SingleLiveState<Char>
typealias SingleLiveStateShort = SingleLiveState<Short>
typealias SingleLiveStateInt = SingleLiveState<Int>
typealias SingleLiveStateLong = SingleLiveState<Long>
typealias SingleLiveStateFloat = SingleLiveState<Float>
typealias SingleLiveStateDouble = SingleLiveState<Double>
typealias SingleLiveStateEnum<T> = SingleLiveState<T>

typealias MultipleLiveStateBoolean = MultipleLiveState<Boolean>
typealias MultipleLiveStateByte = MultipleLiveState<Byte>
typealias MultipleLiveStateChart = MultipleLiveState<Char>
typealias MultipleLiveStateShort = MultipleLiveState<Short>
typealias MultipleLiveStateInt = MultipleLiveState<Int>
typealias MultipleLiveStateLong = MultipleLiveState<Long>
typealias MultipleLiveStateFloat = MultipleLiveState<Float>
typealias MultipleLiveStateDouble = MultipleLiveState<Double>
typealias MultipleLiveStateEnum<T> = MultipleLiveState<T>

open class SingleLiveState<T> : MultipleUiState<T> {

    private val pending = AtomicBoolean(false)
    private val observers = linkedMapOf<Observer<in T>, Observer<in T>>()
    val isObserved get() = !pending.get()

    constructor(value: T) : super(value)

    constructor() : super()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, wrapObserver(observer))
    }

    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(wrapObserver(observer))
    }

    private fun wrapObserver(observer: Observer<in T>) = Observer<T> {
        if (pending.compareAndSet(true, false)) {
            observer.onChanged(it)
        }
    }.also {
        observers[observer] = it
    }

    override fun removeObserver(observer: Observer<in T>) {
        val wrapper = observers.remove(observer)
        if (wrapper != null) {
            super.removeObserver(wrapper)
        } else {
            super.removeObserver(observer)
        }
    }

    override fun postValue(value: T) {
        pending.set(true)
        super.postValue(value)
    }

    @MainThread
    override fun setValue(t: T) {
        pending.set(true)
        super.setValue(t)
    }


    @MainThread
    override fun invoke(value: T) {
        pending.set(true)
        super.invoke(value)
    }

}

open class MultipleLiveState<T> : MutableLiveData<T>, UiState<T> {

    constructor(value: T) : super(value)

    constructor() : super()

    override fun postValue(value: T) {
        super.postValue(value)
    }

    @MainThread
    override fun invoke(value: T) {
        setValue(value)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(): T {
        return super.getValue() as T
    }

    override fun get(): T {
        return value
    }

    override fun set(value: T) {
        setValue(value)
    }

}
