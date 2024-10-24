package com.eitanliu.starter.binding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eitanliu.binding.adapter.fitWindowInsets
import com.eitanliu.binding.extension.isAppearanceLightStatusBars
import com.eitanliu.starter.binding.registry.ActivityLauncher
import com.eitanliu.starter.binding.registry.IFragment
import com.eitanliu.starter.binding.handler.OnBackPressedHandler
import com.eitanliu.starter.binding.model.ActivityLaunchModel
import com.eitanliu.starter.utils.ReflectionUtil
import com.eitanliu.utils.BarUtil.setNavBar
import com.eitanliu.utils.asTypeOrNull

abstract class BindingFragment<VB : ViewDataBinding, VM : BindingViewModel> : Fragment(),
    InitView, ActivityLauncher {

    protected lateinit var binding: VB

    protected lateinit var viewModel: VM

    @Suppress("UNCHECKED_CAST")
    private val viewModelType: Class<VM> by lazy {
        ReflectionUtil.getViewModelGenericType(this) as Class<VM>
    }

    private val onBackPressedHandler by lazy { OnBackPressedHandler(true) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParams(savedInstanceState)
        initViewDataBinding()
        observeActivityUiState()
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

    protected open fun observeActivityUiState() {
        viewModel.state.startActivity.observe(viewLifecycleOwner) {
            startActivity(it)
        }
        viewModel.state.finish.observe(viewLifecycleOwner) {
            activity?.finish()
        }
        viewModel.state.onBackPressed.observe(viewLifecycleOwner) {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        viewModel.onBackPressedEnable.observe(viewLifecycleOwner) { value ->
            onBackPressedHandler.isEnabled = value != false
        }
        viewModel.state.handleOnBackPressed.observe(viewLifecycleOwner) { callback ->
            onBackPressedHandler.onBackPressed = callback
            onBackPressedHandler.isEnabled = viewModel.onBackPressedEnable.value != false
            if (callback != null) {
                activity?.also { act ->
                    act.onBackPressedDispatcher.addCallback(act, onBackPressedHandler)
                }
            } else {
                onBackPressedHandler.remove()
            }
        }
        // 系统栏显示控制
        viewModel.lightStatusBars.observe(viewLifecycleOwner) { state ->
            if (state == null) return@observe
            activity?.isAppearanceLightStatusBars = state == true
        }
        viewModel.lightNavigationBars.observe(viewLifecycleOwner) { state ->
            val color = viewModel.navigationBarsColor.value
            if (color == null && state == null) return@observe
            val isLight = state == true
            activity?.also { act ->
                act.window.setNavBar(isLight, color)
            }
        }
        viewModel.navigationBarsColor.observe(viewLifecycleOwner) { color ->
            val state = viewModel.lightNavigationBars.value
            if (color == null && state == null) return@observe
            val isLight = state == true
            activity?.also { act ->
                act.window.setNavBar(isLight, color)
            }
        }
        viewModel.fitSystemBars.observe(viewLifecycleOwner, fixWindowInsetsObserver)
        viewModel.fitStatusBars.observe(viewLifecycleOwner, fixWindowInsetsObserver)
        viewModel.fitNavigationBars.observe(viewLifecycleOwner, fixWindowInsetsObserver)
        viewModel.fitCaptionBar.observe(viewLifecycleOwner, fixWindowInsetsObserver)
        viewModel.fitDisplayCutout.observe(viewLifecycleOwner, fixWindowInsetsObserver)
        viewModel.fitHorizontal.observe(viewLifecycleOwner, fixWindowInsetsObserver)
        viewModel.fitMergeType.observe(viewLifecycleOwner, fixWindowInsetsObserver)
        viewModel.fitInsetsType.observe(viewLifecycleOwner) { fitWindowInsets() }
        viewModel.fitInsetsMode.observe(viewLifecycleOwner) { fitWindowInsets() }
    }

    private val fixWindowInsetsObserver = Observer<Boolean?> {
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
            notifyUiChange()
        }
        viewModel.asTypeOrNull<IFragment>()?.also { vm ->
            vm.hidden.value = hidden
        }
    }

    protected fun notifyUiChange() {
        viewModel.lightStatusBars.notifyChange()
        viewModel.lightNavigationBars.notifyChange()
        viewModel.navigationBarsColor.notifyChange()
    }

    override fun startActivity(model: ActivityLaunchModel) {
        activity.asTypeOrNull<BindingActivity<*, *>>()?.startActivity(model)
    }
}