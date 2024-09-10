package com.example.base.mvvm

import android.os.Bundle

/**
 * 基础View层的接口
 */
interface IBaseView {

    /**
     * 布局Id
     */
    val initContentView: Int

    /**
     * DataBinding绑定的VariableId
     */
    val initVariableId: Int

    /**
     * 在创建布局和ViewModel之前调用
     */
    fun initParams(savedInstanceState: Bundle?) {}

    /**
     * viewModel和DataBinding创建完成后调用
     */
    fun initData() {}

    /**
     * [initData]之后调用初始化显示内容
     */
    fun initView() {}

    /**
     * [initView]之后调用，监听数据变化刷新UI
     */
    fun initObserve() {}

}