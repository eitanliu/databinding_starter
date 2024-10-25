package com.eitanliu.starter.binding.registry

import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.state.lateMultipleState
import com.eitanliu.binding.state.lateSingleState
import com.eitanliu.binding.state.multipleState
import com.eitanliu.binding.utils.StateUtil.invoke
import com.eitanliu.starter.binding.BindingOwner
import com.eitanliu.starter.binding.model.ActivityLauncherInfo

class ActivityRegistry(
    override val bindingOwner: BindingOwner = BindingOwner.Impl()
) : IActivity {
    override val event = Event()
    override val state = State()

    override val title = lateMultipleState<String?>()
    override val resTitle = lateMultipleState<Int?>()
    override val backVisible = multipleState<Boolean?>(true)
    override val onBackPressedEnable = lateMultipleState<Boolean?>()

    override fun startActivity(info: ActivityLauncherInfo) {
        state.startActivity(info)
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

        override val startActivity = lateSingleState<ActivityLauncherInfo>()

        override val finish = lateSingleState<Unit>()

        override val onBackPressed = lateSingleState<Unit>()

        override val handleOnBackPressed = lateMultipleState<() -> Unit>()
    }
}

