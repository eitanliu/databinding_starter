package com.eitanliu.binding.utils

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.eitanliu.binding.state.MultipleLiveState
import com.eitanliu.binding.state.MultipleObservableState
import com.eitanliu.binding.state.SingleLiveState
import com.eitanliu.binding.state.SingleObservableState

object StateUtil {
    @JvmStatic
    fun invoke(state: MutableLiveData<Unit>) {
        state.value = Unit
    }

    @JvmStatic
    fun invoke(state: MultipleLiveState<Unit>) {
        state.value = Unit
    }

    @JvmStatic
    fun invoke(state: SingleLiveState<Unit>) {
        state.value = Unit
    }

    @JvmStatic
    fun invoke(state: ObservableField<Unit>) {
        state.set(Unit)
    }

    @JvmStatic
    fun invoke(state: MultipleObservableState<Unit>) {
        state.value = Unit
    }

    @JvmStatic
    fun invoke(state: SingleObservableState<Unit>) {
        state.value = Unit
    }
}
