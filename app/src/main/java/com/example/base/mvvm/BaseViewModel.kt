package com.example.base.mvvm

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import com.example.base.bundle.BundleDelegate

open class BaseViewModel<T : BundleDelegate?>(
    val args: T?
) : ViewModel(), DefaultLifecycleObserver {
}