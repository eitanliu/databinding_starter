package com.eitanliu.starter.binding

import androidx.lifecycle.SavedStateHandle
import com.eitanliu.starter.binding.controller.DialogController
import com.eitanliu.starter.binding.controller.IDialog

open class DialogViewModel(
    stateHandle: SavedStateHandle
) : BindingViewModel(stateHandle),
    IDialog by DialogController() {

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