package com.example.base.mvvm

import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import com.example.base.mvvm.controller.ActivityController
import com.example.base.mvvm.controller.IActivity
import com.example.base.mvvm.controller.ISystemInsets
import com.example.base.mvvm.controller.SystemInsetsController

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