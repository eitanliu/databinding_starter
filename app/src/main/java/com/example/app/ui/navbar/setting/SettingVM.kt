package com.example.app.ui.navbar.setting

import android.view.View
import androidx.lifecycle.SavedStateHandle
import com.eitanliu.binding.event.bindingConsumer
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.starter.binding.FragmentViewModel
import com.eitanliu.starter.binding.startActivity
import com.eitanliu.utils.Logcat
import com.eitanliu.utils.SavedStateHandleUtil.bundle
import com.eitanliu.utils.imeInsets
import com.eitanliu.utils.isShowSoftwareKeyboard
import com.eitanliu.utils.toggleSoftKeyboard
import com.example.app.ui.ExampleActivity
import javax.inject.Inject

class SettingVM @Inject constructor(
    stateHandle: SavedStateHandle
) : FragmentViewModel(stateHandle) {
    override val event = Event(this)

    init {
        title.value = "Setting ${stateHandle.bundle}"
        fitSystemBars.value = false
    }


    inner class Event(
        vm: SettingVM
    ) : FragmentViewModel.Event(vm) {

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