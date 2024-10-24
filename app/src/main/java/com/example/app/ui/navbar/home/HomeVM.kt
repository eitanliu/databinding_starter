package com.example.app.ui.navbar.home

import androidx.lifecycle.SavedStateHandle
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.extension.observe
import com.eitanliu.starter.binding.FragmentViewModel
import com.eitanliu.starter.binding.startActivity
import com.eitanliu.utils.Logcat
import com.example.app.extension.bundle
import com.example.app.ui.ExampleActivity
import javax.inject.Inject

class HomeVM @Inject constructor(
    stateHandle: SavedStateHandle
) : FragmentViewModel(stateHandle) {
    override val event = Event(this)

    init {
        title.value = "Home ${stateHandle.bundle}"
        hidden.observe(this) {
            Logcat.msg("Home hidden ${hidden.value}")
        }
    }

    inner class Event(
        vm: HomeVM
    ) : FragmentViewModel.Event(vm) {

        val exampleClick = bindingEvent {
            startActivity<ExampleActivity>()
        }
    }

}