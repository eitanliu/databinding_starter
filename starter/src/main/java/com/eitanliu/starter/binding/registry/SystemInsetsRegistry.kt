package com.eitanliu.starter.binding.registry

import com.eitanliu.binding.state.multipleState

class SystemInsetsRegistry : ISystemInsets {

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

