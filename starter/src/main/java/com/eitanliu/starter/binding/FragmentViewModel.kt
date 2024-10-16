package com.eitanliu.starter.binding

import androidx.lifecycle.SavedStateHandle
import com.eitanliu.starter.binding.controller.FragmentController
import com.eitanliu.starter.binding.controller.IFragment

open class FragmentViewModel(
    stateHandle: SavedStateHandle
) : BindingViewModel(stateHandle),
    IFragment by FragmentController() {

    override val event = Event(this)
    override val state = State(this)

    open class Event(
        vm: FragmentViewModel
    ) : BindingViewModel.Event(vm),
        IFragment.Event by vm.fragment.event {
    }

    open class State(
        vm: FragmentViewModel
    ) : BindingViewModel.State(vm),
        IFragment.State by vm.fragment.state {
    }
}