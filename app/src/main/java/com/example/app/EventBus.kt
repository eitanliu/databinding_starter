package com.example.app

import androidx.annotation.MainThread
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

object EventBus {

    var autoRemove = true

    private val bus = hashMapOf<String, BusLiveData<*>>()

    const val DEFAULT_SINGLE = false

    inline fun <reified T> post(
        value: T, key: String = T::class.java.name,
    ) = get<T>(key).postValue(value)

    inline fun <reified T> post(
        value: T, delay: Long, key: String = T::class.java.name,
    ) = MainScope().launch {
        delay(delay)
        get<T>(key).value = value
    }

    @JvmStatic
    fun <T : Any> post(
        value: T, type: Class<T>, key: String = type.name,
    ) = get(type, key).postValue(value)

    inline fun <reified T> observe(
        owner: ViewModel, single: Boolean = DEFAULT_SINGLE, observer: Observer<T>,
    ) = observe(owner, T::class.java.name, single, observer)

    inline fun <reified T> observe(
        owner: ViewModel, key: String, single: Boolean = DEFAULT_SINGLE, observer: Observer<T>,
    ) = get<T>(key).observe(owner, single, observer)

    @JvmStatic
    fun <T> observe(
        owner: ViewModel, type: Class<T>,
        key: String, single: Boolean,
        observer: Observer<T>,
    ) = get(type, key).observe(owner, single, observer)

    inline fun <reified T> observe(
        owner: LifecycleOwner, single: Boolean = DEFAULT_SINGLE, observer: Observer<T>,
    ) = observe(owner, T::class.java.name, single, observer)

    inline fun <reified T> observe(
        owner: LifecycleOwner, key: String, single: Boolean = DEFAULT_SINGLE, observer: Observer<T>,
    ) = get<T>(key).observe(owner, single, observer)

    @JvmStatic
    fun <T> observe(
        owner: LifecycleOwner, type: Class<T>,
        key: String, single: Boolean,
        observer: Observer<T>,
    ) = get(type, key).observe(owner, single, observer)

    inline fun <reified T> get(
        key: String = T::class.java.name, autoRemove: Boolean = this.autoRemove
    ) = get(T::class.java, key, autoRemove)

    @Suppress("UNCHECKED_CAST")
    fun <T> get(
        type: Class<T>, key: String = type.name, autoRemove: Boolean = this.autoRemove
    ) = bus.getOrPut(key) {
        BusLiveData<T>().autoRemove(Consumer<BusLiveData<T>> {
            bus.remove(key)
        }.takeIf { autoRemove })
    } as BusLiveData<T>

}

open class BusLiveData<T> : MutableLiveData<T> {

    private val pending = AtomicBoolean(false)
    private val observers = linkedMapOf<Observer<in T>, Observer<in T>>()
    val isObserved get() = !pending.get()
    private var consumer: Consumer<BusLiveData<T>>? = null

    constructor(value: T) : super(value)

    constructor() : super()

    @MainThread
    fun observe(owner: ViewModel, single: Boolean, observer: Observer<in T>) {
        super.observeForever(wrapObserver(single, observer).also {
            owner.addCloseable(it)
        })
    }

    @MainThread
    fun observe(owner: LifecycleOwner, single: Boolean, observer: Observer<in T>) {
        if (single) {
            super.observe(owner, wrapObserver(single, observer))
        } else {
            super.observe(owner, observer)
        }

    }

    @MainThread
    fun observeForever(single: Boolean, observer: Observer<in T>) {
        if (single) {
            super.observeForever(wrapObserver(single, observer))
        } else {
            super.observeForever(observer)
        }

    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, wrapObserver(true, observer))
    }

    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(wrapObserver(true, observer))
    }

    private fun wrapObserver(
        single: Boolean, observer: Observer<in T>
    ) = object : Observer<T>, AutoCloseable {
        init {
            observers[observer] = this
        }

        override fun onChanged(value: T) {
            if (single) {
                val isUpdate = pending.compareAndSet(true, false)
                if (isUpdate) observer.onChanged(value)
            } else {
                observer.onChanged(value)
            }
        }

        override fun close() {
            removeObserver(this)
        }
    }

    fun autoRemove(consumer: Consumer<BusLiveData<T>>?): BusLiveData<T> {
        this.consumer = consumer
        return this
    }

    override fun removeObserver(observer: Observer<in T>) {
        val wrapper = observers.remove(observer)
        if (wrapper != null) {
            super.removeObserver(wrapper)
        } else {
            super.removeObserver(observer)
        }
        if (consumer != null && !hasObservers()) {
            consumer?.accept(this)
            consumer = null
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

    @Suppress("UNCHECKED_CAST")
    override fun getValue(): T {
        return super.getValue() as T
    }

}
