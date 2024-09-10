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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eitanliu.binding.adapter.fitWindowInsets
import com.eitanliu.starter.extension.asTypeOrNull
import com.eitanliu.binding.extension.isAppearanceLightStatusBars
import com.eitanliu.starter.utils.ReflectionUtil
import java.lang.ref.Reference
import java.util.Random

abstract class BindingFragment<VB : ViewDataBinding, VM : BaseViewModel> : Fragment(),
    InitView {

    protected lateinit var binding: VB

    protected lateinit var viewModel: VM

    open val viewModelFactory: ViewModelProvider.Factory get() = defaultViewModelProviderFactory

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
        registerUiStateChange()
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
    }

    open fun createViewModel() = ViewModelProvider(this, viewModelFactory)[viewModelType]

    private fun registerUiStateChange() {
        viewModel.activityState.startActivity.observe(viewLifecycleOwner) {
            activity.asTypeOrNull<BindingActivity<*, *>>()?.startActivity(it)
        }
        viewModel.activityState.finish.observe(viewLifecycleOwner) {
            activity?.finish()
        }
        viewModel.activityState.onBackPressed.observe(viewLifecycleOwner) {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        // 系统栏显示控制
        viewModel.lightStatusBars.observe(viewLifecycleOwner) {
            activity?.isAppearanceLightStatusBars = it == true
        }
        viewModel.fitSystemBars.observe(viewLifecycleOwner, fixWindowInsets)
        viewModel.fitStatusBars.observe(viewLifecycleOwner, fixWindowInsets)
        viewModel.fitNavigationBars.observe(viewLifecycleOwner, fixWindowInsets)
        viewModel.fitCaptionBar.observe(viewLifecycleOwner, fixWindowInsets)
        viewModel.fitDisplayCutout.observe(viewLifecycleOwner, fixWindowInsets)
        viewModel.fitHorizontal.observe(viewLifecycleOwner, fixWindowInsets)
        viewModel.fitMergeType.observe(viewLifecycleOwner, fixWindowInsets)
        viewModel.fitInsetsType.observe(viewLifecycleOwner) { fitWindowInsets() }
        viewModel.fitInsetsMode.observe(viewLifecycleOwner) { fitWindowInsets() }
    }

    private val fixWindowInsets = Observer<Boolean?> {
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

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            viewModel.lightStatusBars.notifyChange()
        }
    }
}