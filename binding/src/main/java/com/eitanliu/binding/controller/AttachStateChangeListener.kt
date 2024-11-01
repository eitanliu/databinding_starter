package com.eitanliu.binding.controller

import androidx.lifecycle.LifecycleOwner

interface AttachStateChangeListener {
    fun onAttachedState(owner: LifecycleOwner) {}

    fun onDetachedState(owner: LifecycleOwner) {}
}