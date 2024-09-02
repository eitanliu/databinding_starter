package com.example.base.mvvm

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import com.example.base.bundle.BundleDelegate
import com.example.base.mvvm.controller.ActivityController
import com.example.base.mvvm.controller.IActivity

open class BaseViewModel<T : BundleDelegate?>(
    val args: T?
) : ViewModel(), DefaultLifecycleObserver,
    IActivity by ActivityController() {

    class Even(viewModel: BaseViewModel<*>) : BaseEven,
        IActivity.Event by viewModel.activityEvent

    class State(viewModel: BaseViewModel<*>) : BaseState,
        IActivity.State by viewModel.activityState
}

interface BaseEven : IActivity.Event

interface BaseState : IActivity.State