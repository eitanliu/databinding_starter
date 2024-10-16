package com.example.app.ui.navbar.setting

import androidx.lifecycle.SavedStateHandle
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.state.multipleState
import com.eitanliu.starter.binding.FragmentViewModel
import com.eitanliu.starter.binding.controller.startActivity
import com.example.app.extension.bundle
import com.example.app.ui.ExampleActivity
import javax.inject.Inject

class SettingVM @Inject constructor(
    stateHandle: SavedStateHandle
) : FragmentViewModel(stateHandle) {
    override val event = Event(this)

    val title = multipleState("Setting ${stateHandle.bundle}")


    inner class Event(
        vm: SettingVM
    ) : FragmentViewModel.Event(vm) {

        val exampleClick = bindingEvent {
            startActivity<ExampleActivity>()
        }
    }

}