package com.eitanliu.binding.utils

import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.eitanliu.binding.state.MultipleLiveState
import com.eitanliu.binding.state.MultipleObservableState
import com.eitanliu.binding.state.SingleLiveState
import com.eitanliu.binding.state.SingleObservableState
import com.eitanliu.binding.state.UiState

object StateUtil {
    @JvmStatic
    operator fun MutableLiveData<Unit>.invoke() {
        setValue(Unit)
    }

    @JvmStatic
    operator fun MultipleLiveState<Unit>.invoke() {
        setValue(Unit)
    }

    @JvmStatic
    operator fun SingleLiveState<Unit>.invoke() {
        setValue(Unit)
    }

    @JvmStatic
    operator fun ObservableField<Unit>.invoke() {
        set(Unit)
    }

    @JvmStatic
    operator fun MultipleObservableState<Unit>.invoke() {
        set(Unit)
    }

    @JvmStatic
    operator fun SingleObservableState<Unit>.invoke() {
        set(Unit)
    }

    @JvmStatic
    operator fun UiState<Unit>.invoke() = invoke(Unit)

    inline fun <T> UiState<T>.observe(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
        observe(owner) { observer(it) }
    }

}
