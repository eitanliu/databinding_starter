package com.eitanliu.binding.extension

import androidx.databinding.Observable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class ObservableExt

typealias ObserveSubscriber<T> = Pair<T, Observable.OnPropertyChangedCallback>

fun ObserveSubscriber<Observable>.clear() {
    first.removeOnPropertyChangedCallback(second)
}

inline fun <T : Observable> T.observe(
    crossinline observer: (sender: T) -> Unit,
) = observeId { sender: T, _: Int -> observer(sender) }

fun <T : Observable> T.observeId(
    observer: (sender: T, propertyId: Int) -> Unit,
): ObserveSubscriber<T> = run {
    this to object : Observable.OnPropertyChangedCallback() {

        init {
            addOnPropertyChangedCallback(this)
        }

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            observer(this@observeId, propertyId)
        }
    }
}

inline fun <T : Observable> T.observe(
    owner: LifecycleOwner,
    removeStatus: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    crossinline observer: (sender: T) -> Unit,
) = observeId(owner, removeStatus) { sender: T, _: Int -> observer(sender) }

fun <T : Observable> T.observeId(
    owner: LifecycleOwner,
    removeStatus: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    observer: (sender: T, propertyId: Int) -> Unit,
) {
    object : Observable.OnPropertyChangedCallback(), LifecycleEventObserver {
        init {
            if (owner.lifecycle.currentState > Lifecycle.State.DESTROYED) {
                addOnPropertyChangedCallback(this)
                owner.lifecycle.addObserver(this)
            }
        }

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (removeStatus == event) {
                removeOnPropertyChangedCallback(this)
                owner.lifecycle.removeObserver(this)
            }
        }

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            observer(this@observeId, propertyId)
        }
    }
}