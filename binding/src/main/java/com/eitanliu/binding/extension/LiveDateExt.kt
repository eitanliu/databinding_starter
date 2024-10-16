package com.eitanliu.binding.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.eitanliu.binding.listener.CloseableObserver

class LiveDateExt

fun <T> LiveData<T>.observe(
    owner: ViewModel,
    observer: Observer<in T>,
) {
    owner.addCloseable(observe(observer))
}

fun <T> LiveData<T>.observe(
    observer: Observer<in T>
): CloseableObserver<in T> {
    return object : CloseableObserver<T>(this) {
        override fun onChanged(value: T) = observer.onChanged(value)
    }
}