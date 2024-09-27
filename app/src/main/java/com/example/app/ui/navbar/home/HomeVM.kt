package com.example.app.ui.navbar.home

import androidx.lifecycle.SavedStateHandle
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.state.multipleState
import com.eitanliu.starter.binding.BindingViewModel
import com.eitanliu.starter.binding.controller.startActivity
import com.example.app.ui.ExampleActivity
import com.example.app.ui.ExampleArgs
import javax.inject.Inject

class HomeVM @Inject constructor(
    stateHandle: SavedStateHandle
) : BindingViewModel(stateHandle) {
    override val event = Event(this)

    val title = multipleState("Home ${stateHandle.keys()}")


    inner class Event(
        vm: BindingViewModel
    ) : BindingViewModel.Event(vm) {

        val exampleClick = bindingEvent {
            startActivity<ExampleActivity>(ExampleArgs().apply {
                arg1 = "Example"
                arg2 = 888
            })
        }
    }

}