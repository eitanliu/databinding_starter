package com.eitanliu.starter.binding.registry

import com.eitanliu.binding.state.MultipleUiState

/**
 * Fragment 操作
 */
interface IFragment {
    val fragment: IFragment get() = this
    val event: Event
    val state: State

    val hidden: MultipleUiState<Boolean?>

    interface Event {
    }

    interface State {
    }
}