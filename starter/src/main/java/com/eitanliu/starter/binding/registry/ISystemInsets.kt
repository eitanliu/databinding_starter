package com.eitanliu.starter.binding.registry

import androidx.lifecycle.LifecycleOwner
import com.eitanliu.binding.event.UiEvent
import com.eitanliu.binding.state.MultipleUiState
import com.eitanliu.utils.refWeak

/**
 * 系统栏显示控制
 */
interface ISystemInsets {

    val uiMode: MultipleUiState<Int?>
    val lightStatusBars: MultipleUiState<Boolean?>
    val lightNavigationBars: MultipleUiState<Boolean?>
    val navigationBarsColor: MultipleUiState<Int?>
    val fitSystemBars: MultipleUiState<Boolean?>
    val fitStatusBars: MultipleUiState<Boolean?>
    val fitNavigationBars: MultipleUiState<Boolean?>
    val fitCaptionBar: MultipleUiState<Boolean?>
    val fitDisplayCutout: MultipleUiState<Boolean?>
    val fitHorizontal: MultipleUiState<Boolean?>
    val fitMergeType: MultipleUiState<Boolean?>
    val fitInsetsType: MultipleUiState<Int?>
    val fitInsetsMode: MultipleUiState<Int?>

    fun observeSystemInsets(
        owner: LifecycleOwner,
        observer: ISystemInsets?,
        beforeChange: UiEvent? = null
    ) {
        val ref = observer.refWeak()
        uiMode.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.uiMode?.value = it
        }
        lightStatusBars.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.lightStatusBars?.value = it
        }
        lightNavigationBars.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.lightNavigationBars?.value = it
        }
        navigationBarsColor.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.navigationBarsColor?.value = it
        }
        fitSystemBars.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.fitSystemBars?.value = it
        }
        fitStatusBars.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.fitStatusBars?.value = it
        }
        fitNavigationBars.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.fitNavigationBars?.value = it
        }
        fitCaptionBar.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.fitCaptionBar?.value = it
        }
        fitDisplayCutout.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.fitDisplayCutout?.value = it
        }
        fitHorizontal.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.fitHorizontal?.value = it
        }
        fitMergeType.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.fitMergeType?.value = it
        }
        fitInsetsType.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.fitInsetsType?.value = it
        }
        fitInsetsMode.observe(owner) {
            beforeChange?.invoke()
            ref.get()?.fitInsetsMode?.value = it
        }
    }

}