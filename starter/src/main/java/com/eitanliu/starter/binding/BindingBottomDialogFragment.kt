package com.eitanliu.starter.binding

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eitanliu.binding.adapter.fitWindowInsets
import com.eitanliu.starter.extension.asTypeOrNull
import com.eitanliu.binding.extension.isAppearanceLightStatusBars
import com.eitanliu.starter.utils.ReflectionUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.ref.Reference
import java.util.Random

abstract class BindingBottomDialogFragment<VB : ViewDataBinding, VM : BindingViewModel> :
    BottomSheetDialogFragment(),
    InitView {

    protected lateinit var binding: VB

    protected lateinit var viewModel: VM

    @Suppress("UNCHECKED_CAST")
    private val viewModelType: Class<VM> by lazy {
        ReflectionUtil.getViewModelGenericType(this) as Class<VM>
    }

    private val codeRandom = Random()
    private val requestCallbacks = SparseArray<Reference<ActivityResultCallback<ActivityResult>>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParams(savedInstanceState)
        initViewDataBinding()
        handleActivityUiState()
        initData()
        initView()
        initObserve()
        return binding.root
    }

    private fun initViewDataBinding(parent: ViewGroup? = null) {
        binding = DataBindingUtil.inflate(layoutInflater, initContentView, parent, false)
        viewModel = createViewModel()
        binding.setVariable(initVariableId, viewModel)
        binding.lifecycleOwner = this
        lifecycle.addObserver(viewModel)
    }

    open fun createViewModel() = ViewModelProvider(this)[viewModelType]

    protected fun handleActivityUiState() {
        viewModel.state.startActivity.observe(viewLifecycleOwner) {
            activity.asTypeOrNull<BindingActivity<*, *>>()?.startActivity(it)
        }
        viewModel.state.finish.observe(viewLifecycleOwner) {
            activity?.finish()
        }
        viewModel.state.onBackPressed.observe(viewLifecycleOwner) {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        // 系统栏显示控制
        viewModel.lightStatusBars.observe(viewLifecycleOwner) {
            activity?.isAppearanceLightStatusBars = it == true
        }
        viewModel.fitSystemBars.observe(viewLifecycleOwner, fitSystemInsetsObserver)
        viewModel.fitStatusBars.observe(viewLifecycleOwner, fitSystemInsetsObserver)
        viewModel.fitNavigationBars.observe(viewLifecycleOwner, fitSystemInsetsObserver)
        viewModel.fitCaptionBar.observe(viewLifecycleOwner, fitSystemInsetsObserver)
        viewModel.fitDisplayCutout.observe(viewLifecycleOwner, fitSystemInsetsObserver)
        viewModel.fitHorizontal.observe(viewLifecycleOwner, fitSystemInsetsObserver)
        viewModel.fitMergeType.observe(viewLifecycleOwner, fitSystemInsetsObserver)
        viewModel.fitInsetsType.observe(viewLifecycleOwner) { fitWindowInsets() }
        viewModel.fitInsetsMode.observe(viewLifecycleOwner) { fitWindowInsets() }
    }

    private val fitSystemInsetsObserver = Observer<Boolean?> {
        fitWindowInsets()
    }

    private fun fitWindowInsets() {
        binding.root.fitWindowInsets(
            viewModel.fitSystemBars.value,
            viewModel.fitStatusBars.value,
            viewModel.fitNavigationBars.value,
            viewModel.fitCaptionBar.value,
            viewModel.fitDisplayCutout.value,
            viewModel.fitHorizontal.value,
            viewModel.fitMergeType.value,
            viewModel.fitInsetsType.value,
            viewModel.fitInsetsMode.value,
        )
    }
}