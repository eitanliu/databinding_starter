package com.example.app

import androidx.lifecycle.SavedStateHandle
import com.blankj.utilcode.util.ToastUtils
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.event.debounceEvent
import com.eitanliu.binding.state.multipleState
import com.eitanliu.binding.state.singleState
import com.eitanliu.starter.binding.BindingViewModel
import com.eitanliu.starter.binding.controller.startActivity
import com.example.app.extension.bundle
import com.example.app.extension.not
import com.example.app.ui.ExampleActivity
import com.example.app.ui.ExampleArgs
import com.example.app.ui.navbar.NavBarActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    stateHandle: SavedStateHandle,
) : BindingViewModel(stateHandle) {
    private val args = MainArgs(stateHandle.bundle)
    override val event = Event(this)
    override val state = State(this)

    init {
        fitSystemBars.value = true
        handleOnBackPressed(event.onBackPressed)
    }

    val title = multipleState("${args.arg1} ${args.arg2}")

    inner class Event(
        vm: BindingViewModel
    ) : BindingViewModel.Event(vm) {

        // 二次返回退出
        val onBackPressed = debounceEvent(2000, {
            finish()
        }) {
            ToastUtils.showShort("back again to exit")
        }

        val exampleClick = bindingEvent {
            startActivity<ExampleActivity>(ExampleArgs().apply {
                arg1 = "Example"
                arg2 = 888
            })
        }

        val navBarClick = bindingEvent {
            startActivity<NavBarActivity>()
        }
    }

    inner class State(
        vm: BindingViewModel
    ) : BindingViewModel.State(vm) {
        val testState = singleState<Int>()
    }
}