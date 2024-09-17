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
import com.example.app.ui.ExampleActivity
import com.example.app.ui.ExampleArgs
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
        lightStatusBars.value = false
        fitSystemBars.value = true
    }

    val title = multipleState("${args.arg1} ${args.arg2}")

    inner class Event(viewModel: BindingViewModel) : BindingViewModel.Event(viewModel) {

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
    }

    inner class State(viewModel: BindingViewModel) : BindingViewModel.State(viewModel) {
        val testState = singleState<Int>()
    }
}