package com.example.app

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.event.debounceEvent
import com.eitanliu.binding.state.singleState
import com.eitanliu.starter.binding.BindingViewModel
import com.eitanliu.starter.binding.startActivity
import com.example.app.extension.bundle
import com.example.app.ui.ExampleActivity
import com.example.app.ui.ExampleArgs
import com.example.app.ui.navbar.NavBarActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    stateHandle: SavedStateHandle,
) : BindingViewModel(stateHandle) {

    companion object {
        const val DELAY_BACK_TIME = 1500L
    }

    private val args = MainArgs(stateHandle.bundle)
    override val event = Event(this)
    override val state = State(this)

    init {
        fitSystemBars.value = true
        handleOnBackPressed(event.onBackPressed)
        title.value = "${args.arg1} ${args.arg2}"
        backVisible.value = false
    }

    fun onBackPressedEnableDelay(delay: Long) = viewModelScope.launch {
        onBackPressedEnable.value = false
        delay(delay)
        onBackPressedEnable.value = true
    }

    inner class Event(
        vm: BindingViewModel
    ) : BindingViewModel.Event(vm) {

        // 二次返回退出
        val onBackPressed = debounceEvent(DELAY_BACK_TIME) {
            onBackPressedEnableDelay(DELAY_BACK_TIME)
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