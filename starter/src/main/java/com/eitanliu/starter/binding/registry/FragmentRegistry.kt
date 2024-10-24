package com.eitanliu.starter.binding.registry

import com.eitanliu.binding.state.lateMultipleState

class FragmentRegistry : IFragment {
    override val event = Event()
    override val state = State()

    override val hidden = lateMultipleState<Boolean?>()

    inner class Event : IFragment.Event {
    }

    inner class State : IFragment.State {
    }
}

