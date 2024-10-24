package com.eitanliu.starter.binding

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.eitanliu.binding.BR

/**
 * 基础View层的接口
 */
interface BindingView {

    /**
     * 布局Id
     */
    val bindLayoutId: Int get() = ResourcesCompat.ID_NULL

    /**
     * DataBinding绑定的VariableId
     */
    val bindVariableId: Int get() = BR.viewModel

    /**
     * 在创建布局和ViewModel之前调用
     */
    fun initParams(savedInstanceState: Bundle?) {}

    /**
     * viewModel和DataBinding创建完成后调用
     */
    fun bindData() {}

    /**
     * [bindData]之后调用初始化显示内容
     */
    fun bindView() {}

    /**
     * [bindView]之后调用，监听数据变化刷新UI
     */
    fun bindObserve() {}

}