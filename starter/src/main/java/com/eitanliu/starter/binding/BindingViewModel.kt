package com.eitanliu.starter.binding

import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import com.eitanliu.starter.binding.controller.ActivityController
import com.eitanliu.starter.binding.controller.IActivity
import com.eitanliu.starter.binding.controller.ISystemInsets
import com.eitanliu.starter.binding.controller.SystemInsetsController

open class BaseViewModel(
    val bundle: Bundle
) : ViewModel(), DefaultLifecycleObserver,
    IActivity by ActivityController(),
    ISystemInsets by SystemInsetsController() {
    val baseEvent = Event(this)
    val baseState = State(this)

    class Event(viewModel: BaseViewModel) : BaseEven,
        IActivity.Event by viewModel.activityEvent

    class State(viewModel: BaseViewModel) : BaseState,
        IActivity.State by viewModel.activityState

}

interface BaseEven : IActivity.Event

interface BaseState : IActivity.State