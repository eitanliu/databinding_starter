package com.eitanliu.starter.binding

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.eitanliu.binding.event.UiEvent
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.starter.binding.controller.ActivityController
import com.eitanliu.starter.binding.controller.IActivity
import com.eitanliu.starter.binding.controller.ISystemInsets
import com.eitanliu.starter.binding.controller.SystemInsetsController

open class BindingViewModel(
    val stateHandle: SavedStateHandle
) : ViewModel(), LifecycleViewModel,
    IActivity by ActivityController(),
    ISystemInsets by SystemInsetsController() {

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

