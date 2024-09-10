package com.example.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable

object EventBus {

    private val bus = hashMapOf<String, BusLiveData<*>>()

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
    fun <T> post(
        value: T, type: Class<T>, key: String = type.name,
    ) = get(type, key).postValue(value)

    inline fun <reified T> observe(
        owner: LifecycleOwner, observer: Observer<T>,
    ) = observe(owner, T::class.java.name, observer)

    inline fun <reified T> observe(
        owner: LifecycleOwner, key: String, observer: Observer<T>,
    ) = get<T>(key).observe(owner, observer)

    @JvmStatic
    fun <T> observe(
        owner: LifecycleOwner, type: Class<T>, key: String, observer: Observer<T>
    ) = get(type, key).observe(owner, observer)

    inline fun <reified T> get(
        key: String = T::class.java.name
    ) = get(T::class.java, key)

    @Suppress("UNCHECKED_CAST")
    fun <T> get(
        type: Class<T>, key: String = type.name
    ) = bus.getOrPut(key) { BusLiveData<T>() } as BusLiveData<T>

}

class BusLiveData<T> : MutableLiveData<T> {

    constructor(value: T?) : super(value)

    constructor() : super()

    @Suppress("UNCHECKED_CAST")
    override fun getValue(): T {
        return super.getValue() as T
    }
}

interface BusEvent : Serializable
