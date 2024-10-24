package com.eitanliu.starter.binding.registry

import com.eitanliu.binding.event.UiEvent
import com.eitanliu.binding.state.MultipleUiState
import com.eitanliu.starter.binding.ActivityLauncher
import com.eitanliu.starter.binding.BindingOwner
import com.eitanliu.starter.binding.model.ActivityLauncherInfo

/**
 * Activity 操作
 */
interface IActivity : ActivityLauncher {
    val bindingOwner: BindingOwner?
    val activity: IActivity get() = this
    val event: Event
    val state: State

    val title: MultipleUiState<String?>
    val resTitle: MultipleUiState<Int?>
    val backVisible: MultipleUiState<Boolean?>
    val onBackPressedEnable: MultipleUiState<Boolean?>

    fun finish()

    /**
     * 调用返回
     */
    fun onBackPressed()

    /**
     * 自定义返回操作
     */
    fun handleOnBackPressed(onBackPressed: () -> Unit)

    interface Event {
        val onBackClick: UiEvent

        val finishActivity: UiEvent
    }

    interface State {
        val startActivity: MultipleUiState<ActivityLauncherInfo>

        val finish: MultipleUiState<Unit>

        val onBackPressed: MultipleUiState<Unit>

        val handleOnBackPressed: MultipleUiState<() -> Unit>
    }
}