package com.eitanliu.binding.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.eitanliu.binding.listener.CloseableObserver

class LiveDateExt

fun <T, V> LiveData<T>.observe(
    owner: LifecycleOwner, view: V,
    init: (V.() -> Unit)? = null,
    observer: V.(T) -> Unit,
) {
    observe(owner, object : Observer<T> {
        init {
            init?.invoke(view)
        }

        override fun onChanged(value: T) {
            observer.invoke(view, value)
        }
    })
}

fun <T, V> LiveData<T>.observe(
    owner: ViewModel, view: V,
    init: (V.() -> Unit)? = null,
    observer: V.(T) -> Unit,
) {
    owner.addCloseable(observeReceiver(view, init, observer))
}

fun <T> LiveData<T>.observe(
    owner: ViewModel,
    observer: Observer<in T>,
) {
    owner.addCloseable(observeCloseable(observer))
}

fun <T, V> LiveData<T>.observeReceiver(
    view: V,
    init: (V.() -> Unit)? = null,
    observer: V.(T) -> Unit,
): CloseableObserver<T> {
    return object : CloseableObserver<T>(this) {
        init {
            init?.invoke(view)
        }

        override fun onChanged(value: T) {
            view.observer(value)
        }
    }
}

fun <T> LiveData<T>.observeCloseable(
    observer: Observer<in T>
): CloseableObserver<in T> {
    return object : CloseableObserver<T>(this) {
        override fun onChanged(value: T) {
            observer.onChanged(value)
        }
    }
}