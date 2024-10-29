package com.eitanliu.starter.binding

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.eitanliu.binding.event.UiEvent
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.starter.binding.registry.ActivityRegistry
import com.eitanliu.starter.binding.registry.IActivity
import com.eitanliu.starter.binding.registry.ISystemInsets
import com.eitanliu.starter.binding.registry.SystemInsetsRegistry

open class BindingViewModel(
    val stateHandle: SavedStateHandle,
    override val bindingOwner: BindingOwner = BindingOwner.Impl()
) : ViewModel(), LifecycleViewModel, BindingDelegate,
    IActivity by ActivityRegistry(bindingOwner),
    ISystemInsets by SystemInsetsRegistry() {

    constructor(
        stateHandle: SavedStateHandle
    ) : this(stateHandle, BindingOwner.Impl())

    init {
        bindingOwner.bind(this)
    }

    override val event = Event(this)
    override val state = State(this)

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }

    open class Event(
        vm: BindingViewModel
    ) : LifecycleViewModel.Event,
        IActivity.Event by vm.activity.event {
        override val onMenuClick: UiEvent = bindingEvent { }
    }

    open class State(
        vm: BindingViewModel
    ) : LifecycleViewModel.State,
        IActivity.State by vm.activity.state {
    }

}

