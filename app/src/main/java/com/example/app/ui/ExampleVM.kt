package com.example.app.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.blankj.utilcode.util.ToastUtils
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.event.debounceEvent
import com.eitanliu.binding.state.multipleState
import com.eitanliu.binding.state.singleState
import com.eitanliu.starter.binding.BindingViewModel
import com.example.app.extension.bundle
import com.example.app.extension.not
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExampleVM @Inject constructor(
    stateHandle: SavedStateHandle,
) : BindingViewModel(stateHandle) {
    private val args = ExampleArgs(stateHandle.bundle)
    override val event = Event(this)
    override val state = State(this)

    init {
        fitSystemBars.value = true
    }

    val title = multipleState("${args.arg1} ${args.arg2}")

    inner class Event(
        viewModel: BindingViewModel
    ) : BindingViewModel.Event(viewModel) {

        val changeSystemBars = bindingEvent {
            fitSystemBars.value = fitSystemBars.value.not()
            Log.e("updateSystemBar", "${fitSystemBars.value}")
        }

        val changeLightStatus = bindingEvent {
            lightStatusBars.value = lightStatusBars.value.not()
            Log.e("updateLightStatus", "${lightStatusBars.value}")
        }
    }

    inner class State(
        viewModel: BindingViewModel
    ) : BindingViewModel.State(viewModel) {
        val testState = singleState<Int>()
    }
}