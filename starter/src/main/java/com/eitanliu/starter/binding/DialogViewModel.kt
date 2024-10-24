package com.eitanliu.starter.binding

import androidx.lifecycle.SavedStateHandle
import com.eitanliu.starter.binding.registry.DialogRegistry
import com.eitanliu.starter.binding.registry.IDialog

open class DialogViewModel(
    stateHandle: SavedStateHandle
) : BindingViewModel(stateHandle),
    IDialog by DialogRegistry() {

    override val event = Event(this)
    override val state = State(this)

    open class Event(
        vm: DialogViewModel
    ) : BindingViewModel.Event(vm),
        IDialog.Event by vm.dialog.event {
    }

    open class State(
        vm: DialogViewModel
    ) : BindingViewModel.State(vm),
        IDialog.State by vm.dialog.state {
    }
}