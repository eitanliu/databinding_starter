package com.eitanliu.starter.binding

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eitanliu.binding.adapter.fitWindowInsets
import com.eitanliu.binding.extension.isAppearanceLightStatusBars
import com.eitanliu.binding.extension.selfFragment
import com.eitanliu.starter.binding.dialog.SafetyDialog
import com.eitanliu.starter.binding.listener.DialogLifecycle
import com.eitanliu.starter.extension.asTypeOrNull
import com.eitanliu.starter.utils.ReflectionUtil
import java.lang.ref.Reference
import java.util.Random

abstract class BindingDialogFragment<VB : ViewDataBinding, VM : BindingViewModel> :
    DialogFragment(), DialogLifecycle.OnCreateListener, DialogInterface.OnShowListener,
    InitView {

    var onCreateListener: DialogLifecycle.OnCreateListener? = null
    var onDismissListener: DialogInterface.OnDismissListener? = null
    var onCancelListener: DialogInterface.OnCancelListener? = null
    var onShowListener: DialogInterface.OnShowListener? = null

    protected lateinit var binding: VB

    protected lateinit var viewModel: VM

    @Suppress("UNCHECKED_CAST")
    private val viewModelType: Class<VM> by lazy {
        ReflectionUtil.getViewModelGenericType(this) as Class<VM>
    }

    private val codeRandom = Random()
    private val requestCallbacks = SparseArray<Reference<ActivityResultCallback<ActivityResult>>>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return SafetyDialog(requireContext(), theme).apply {
            setOnCreateListener(selfFragment)
            setOnShowListener(selfFragment)
        }
    }

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

    override fun onCreate(dialog: DialogInterface, window: Window) {
        onCreateListener?.onCreate(dialog, window)
    }

    override fun onShow(dialog: DialogInterface) {
        onShowListener?.onShow(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onCancelListener?.onCancel(dialog)
    }
}