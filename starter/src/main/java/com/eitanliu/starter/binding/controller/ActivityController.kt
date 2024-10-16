package com.eitanliu.starter.binding.controller

import com.eitanliu.binding.event.UiEvent
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.state.MultipleUiState
import com.eitanliu.binding.state.lateMultipleState
import com.eitanliu.binding.state.lateSingleState
import com.eitanliu.binding.state.multipleState
import com.eitanliu.binding.utils.StateUtil.invoke
import com.eitanliu.starter.binding.model.ActivityLaunchModel

class ActivityController : IActivity {
    override val event = Event()
    override val state = State()
    override val onBackPressedEnable = multipleState<Boolean>()

    override fun startActivity(model: ActivityLaunchModel) {
        state.startActivity(model)
    }

    override fun finish() {
        state.finish()
    }

    override fun onBackPressed() {
        state.onBackPressed()
    }

    override fun handleOnBackPressed(onBackPressed: () -> Unit) {
        state.handleOnBackPressed(onBackPressed)
    }

    inner class Event : IActivity.Event {

        override val onBackClick = bindingEvent {
            onBackPressed()
        }

        override val finishActivity = bindingEvent {
            finish()
        }
    }

    inner class State : IActivity.State {

        override val startActivity = lateSingleState<ActivityLaunchModel>()

        override val finish = lateSingleState<Unit>()

        override val onBackPressed = lateSingleState<Unit>()

        override val handleOnBackPressed = lateMultipleState<() -> Unit>()
    }
}

/**
 * Activity 操作
 */
interface IActivity : ActivityLauncher {
    val activity: IActivity get() = this
    val event: Event
    val state: State

    val onBackPressedEnable: MultipleUiState<Boolean?>

    fun finish()

    /**
     * 调用返回
     */
    fun onBackPressed()

    /**
     * 自定义返回操作
     */
    fun handleOnBackPressed(onBackPressed: () -> Unit)

    interface Event {
        val onBackClick: UiEvent

        val finishActivity: UiEvent
    }

    interface State {
        val startActivity: MultipleUiState<ActivityLaunchModel>

        val finish: MultipleUiState<Unit>

        val onBackPressed: MultipleUiState<Unit>

        val handleOnBackPressed: MultipleUiState<() -> Unit>
    }
}
