package com.eitanliu.starter.binding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eitanliu.binding.ViewBindingUtil
import com.eitanliu.starter.binding.handler.OnBackPressedHandler
import com.eitanliu.starter.binding.model.ActivityLauncherInfo
import com.eitanliu.starter.binding.registry.IFragment
import com.eitanliu.starter.utils.ReflectionUtil
import com.eitanliu.utils.asTypeOrNull

abstract class BindingFragment<VB : ViewDataBinding, VM : BindingViewModel> : Fragment(),
    BindingView, ActivityLauncher {

    protected lateinit var binding: VB

    protected lateinit var viewModel: VM

    @Suppress("UNCHECKED_CAST")
    private val viewBindingType: Class<VB> by lazy {
        ReflectionUtil.getViewBindingGenericType(this) as Class<VB>
    }

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
        ensureBinding()
        observeActivityUiState()
        bindData()
        bindView()
        bindObserve()
        return binding.root
    }

    open fun createViewModel() = ViewModelProvider(this)[viewModelType]

    private fun ensureBinding(parent: ViewGroup? = null) {
        binding = if (bindLayoutId != ResourcesCompat.ID_NULL) {
            DataBindingUtil.inflate(layoutInflater, bindLayoutId, parent, false)
        } else ViewBindingUtil.inflate(viewBindingType, layoutInflater, null, false)
        viewModel = createViewModel()
        binding.setVariable(bindVariableId, viewModel)
        binding.lifecycleOwner = this
        lifecycle.addObserver(viewModel)
    }

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
        val systemInsets = requireActivity().asTypeOrNull<BindingActivity<*, *>>()?.systemInsets
        viewModel.observeSystemInsets(this, systemInsets)
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
        viewModel.fitSystemBars.notifyChange()
        viewModel.fitNavigationBars.notifyChange()
        viewModel.fitInsetsType.notifyChange()
    }

    override fun startActivity(info: ActivityLauncherInfo) {
        activity.asTypeOrNull<BindingActivity<*, *>>()?.startActivity(info)
    }
}