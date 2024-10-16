package com.eitanliu.starter.binding.controller

import com.eitanliu.binding.event.UiEvent
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.state.MultipleUiState
import com.eitanliu.binding.state.UiState
import com.eitanliu.binding.state.lateSingleState
import com.eitanliu.binding.state.multipleState
import com.eitanliu.binding.utils.StateUtil.invoke


class DialogController : IDialog {
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

/**
 * Dialog 操作
 */
interface IDialog {
    val dialog: IDialog get() = this
    val event: Event
    val state: State

    val canceledOnTouchOutside: UiState<Boolean>

    fun dismiss()
    fun dismissNow()
    fun dismissAllowingStateLoss()

    interface Event {
        val dismiss: UiEvent
        val dismissNow: UiEvent
        val dismissAllowingStateLoss: UiEvent
    }

    interface State {
        val dismiss: MultipleUiState<Unit>
        val dismissNow: MultipleUiState<Unit>
        val dismissAllowingStateLoss: MultipleUiState<Unit>
    }
}
