package com.eitanliu.starter.binding.controller

import com.eitanliu.binding.state.MultipleUiState
import com.eitanliu.binding.state.multipleState

class SystemInsetsController : ISystemInsets {

    override val uiMode = multipleState<Int?>()
    override val lightStatusBars = multipleState<Boolean?>()
    override val lightNavigationBars = multipleState<Boolean?>()
    override val navigationBarsColor = multipleState<Int?>()
    override val fitSystemBars = multipleState<Boolean?>()
    override val fitStatusBars = multipleState<Boolean?>()
    override val fitNavigationBars = multipleState<Boolean?>()
    override val fitCaptionBar = multipleState<Boolean?>()
    override val fitDisplayCutout = multipleState<Boolean?>()
    override val fitHorizontal = multipleState<Boolean?>()
    override val fitMergeType = multipleState<Boolean?>()
    override val fitInsetsType = multipleState<Int?>()
    override val fitInsetsMode = multipleState<Int?>()

}

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
