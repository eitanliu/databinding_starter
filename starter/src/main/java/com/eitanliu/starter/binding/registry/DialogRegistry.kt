package com.eitanliu.starter.binding.registry

import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.state.lateSingleState
import com.eitanliu.binding.state.multipleState
import com.eitanliu.utils.UiStateUtil.invoke


class DialogRegistry : IDialog {
    override val event = Event()
    override val state = State()

    override val canceledOnTouchOutside = multipleState(true)

    override fun dismiss() {
        state.dismiss()
    }

    override fun dismissNow() {
        state.dismissNow()
    }

    override fun dismissAllowingStateLoss() {
        state.dismissAllowingStateLoss()
    }

    inner class Event : IDialog.Event {
        override val dismiss = bindingEvent {
            dismiss()
        }
        override val dismissNow = bindingEvent {
            dismissNow()
        }
        override val dismissAllowingStateLoss = bindingEvent {
            dismissAllowingStateLoss()
        }
    }

    inner class State : IDialog.State {
        override val dismiss = lateSingleState<Unit>()
        override val dismissNow = lateSingleState<Unit>()
        override val dismissAllowingStateLoss = lateSingleState<Unit>()
    }
}

