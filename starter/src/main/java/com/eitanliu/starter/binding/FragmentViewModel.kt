package com.eitanliu.starter.binding

import androidx.lifecycle.SavedStateHandle
import com.eitanliu.starter.binding.registry.FragmentRegistry
import com.eitanliu.starter.binding.registry.IFragment

open class FragmentViewModel(
    stateHandle: SavedStateHandle
) : BindingViewModel(stateHandle),
    IFragment by FragmentRegistry() {

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