package com.eitanliu.starter.binding

import androidx.lifecycle.DefaultLifecycleObserver
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
) : ViewModel(), DefaultLifecycleObserver, IViewModel,
    IActivity by ActivityController(),
    ISystemInsets by SystemInsetsController() {
    override val event = Event(this)
    override val state = State(this)

    open class Event(viewModel: BindingViewModel) : IViewModel.Even,
        IActivity.Event by viewModel.activity.event {
        override val onMenuClick: UiEvent = bindingEvent { }
    }

    open class State(viewModel: BindingViewModel) : IViewModel.State,
        IActivity.State by viewModel.activity.state

}

interface IViewModel {
    val event: Even
    val state: State

    interface Even : IActivity.Event {
        val onMenuClick: UiEvent
    }

    interface State : IActivity.State
}