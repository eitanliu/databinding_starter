package com.eitanliu.binding.controller

import android.view.View
import android.view.Window
import androidx.core.view.SoftwareKeyboardControllerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.eitanliu.binding.model.FitWindowInsets

class ViewExtController(
    override val view: View
) : ViewLifecycleController() {
    companion object Key : ViewController.Key<ViewExtController>

    override val key: ViewController.Key<*> = Key

    var fitWindowInsets: FitWindowInsets? = null

    var windowInsetsControllerCompat: WindowInsetsControllerCompat? = null
        private set

    val rootWindowInsetsCompat
        get() = ViewCompat.getRootWindowInsets(view)

    var viewWindowInsetsCompat: WindowInsetsCompat? = null

    var cacheWindowInsetsCompat: WindowInsetsCompat? = null

    private var _softwareKeyboardController: SoftwareKeyboardControllerCompat? = null
    val softwareKeyboardController
        get() = _softwareKeyboardController ?: SoftwareKeyboardControllerCompat(view).also {
            _softwareKeyboardController = it
        }


    init {
        initAttachLifecycle()
    }

    override fun onViewAttachedToWindow(v: View) {
        viewLifecycleOwner.dispatchAttachedLifecycle()
    }

    override fun onViewDetachedFromWindow(v: View) {
        viewLifecycleOwner.dispatchDetachedLifecycle()
    }

    fun getWindowInsetsController(
        window: Window
    ) = windowInsetsControllerCompat ?: WindowInsetsControllerCompat(window, view).also {
        windowInsetsControllerCompat = it
    }

}