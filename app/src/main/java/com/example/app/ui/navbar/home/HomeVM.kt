package com.example.app.ui.navbar.home

import android.view.View
import androidx.lifecycle.SavedStateHandle
import com.eitanliu.binding.event.bindingConsumer
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.extension.observe
import com.eitanliu.starter.binding.FragmentViewModel
import com.eitanliu.starter.binding.startActivity
import com.eitanliu.utils.Logcat
import com.eitanliu.utils.SavedStateHandleUtil.bundle
import com.eitanliu.utils.imeInsets
import com.eitanliu.utils.isShowSoftwareKeyboard
import com.eitanliu.utils.not
import com.eitanliu.utils.toggleSoftKeyboard
import com.example.app.ui.ExampleActivity
import javax.inject.Inject

class HomeVM @Inject constructor(
    stateHandle: SavedStateHandle
) : FragmentViewModel(stateHandle) {
    override val event = Event(this)

    init {
        fitSystemBars.value = true
        title.value = "Home ${stateHandle.bundle}"
        hidden.observe(this) {
            Logcat.msg("Home hidden ${hidden.value}")
        }
    }

    inner class Event(
        vm: HomeVM
    ) : FragmentViewModel.Event(vm) {

        val fitSystemBarsClick = bindingEvent {
            fitSystemBars.value = fitSystemBars.value.not()
            Logcat.msg("fitSystemBars ${fitSystemBars.value}")
        }

        val toggleSoftKeyboard = bindingConsumer<View> { view ->
            // view.toggleSoftKeyboard()
            view.rootView.apply {
                Logcat.msg("softKeyboard $isShowSoftwareKeyboard ${view.imeInsets}")
                toggleSoftKeyboard()
            }
        }

        val exampleClick = bindingEvent {
            startActivity<ExampleActivity>()
        }
    }

}