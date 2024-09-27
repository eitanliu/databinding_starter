package com.example.app.ui.navbar

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.eitanliu.binding.state.multipleState
import com.eitanliu.starter.binding.BindingViewModel
import com.example.app.binding.model.FragmentItem
import com.example.app.ui.navbar.home.HomeFragment
import com.example.app.ui.navbar.setting.SettingFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavBarVM @Inject constructor(
    stateHandle: SavedStateHandle,
) : BindingViewModel(stateHandle) {
    override val event = Event(this)
    override val state = State(this)

    val items = listOf(
        FragmentItem(HomeFragment::class.java, Bundle().apply {
            putInt("age", 222)
        }),
        FragmentItem(SettingFragment::class.java),
    )
    val itemIndex = multipleState(0)

    init {
        lightStatusBars.value = false
        fitSystemBars.value = true
    }

    inner class Event(
        vm: BindingViewModel
    ) : BindingViewModel.Event(vm) {
    }

    inner class State(
        vm: BindingViewModel
    ) : BindingViewModel.State(vm) {
    }
}