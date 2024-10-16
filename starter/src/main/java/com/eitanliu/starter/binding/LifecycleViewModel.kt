package com.eitanliu.starter.binding

import androidx.lifecycle.DefaultLifecycleObserver
import com.eitanliu.binding.event.UiEvent

interface LifecycleViewModel : DefaultLifecycleObserver {
    val event: Event
    val state: State

    interface Event {
        val onMenuClick: UiEvent
    }

    interface State
}