package com.eitanliu.starter.binding.registry

import com.eitanliu.binding.event.UiEvent
import com.eitanliu.binding.state.MultipleUiState
import com.eitanliu.binding.state.UiState

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