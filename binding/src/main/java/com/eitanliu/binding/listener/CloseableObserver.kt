package com.eitanliu.binding.listener

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

abstract class CloseableObserver<T>(
    private val observable: LiveData<T>,
) : Observer<T>, AutoCloseable {
    init {
        observable.observeForever(this)
    }

    override fun close() {
        observable.removeObserver(this)
    }
}