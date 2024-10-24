package com.eitanliu.starter.binding.registry

import com.eitanliu.binding.state.MultipleUiState

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

}