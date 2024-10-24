package com.eitanliu.starter.binding

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.ComponentDialog
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eitanliu.binding.ViewBindingUtil
import com.eitanliu.binding.adapter.fitWindowInsets
import com.eitanliu.binding.extension.selfFragment
import com.eitanliu.starter.binding.registry.IDialog
import com.eitanliu.starter.binding.dialog.SafetyDialog
import com.eitanliu.starter.binding.handler.OnBackPressedHandler
import com.eitanliu.starter.binding.listener.DialogLifecycle
import com.eitanliu.starter.binding.model.ActivityLauncherInfo
import com.eitanliu.starter.utils.ReflectionUtil
import com.eitanliu.utils.BarUtil.setNavBar
import com.eitanliu.utils.asTypeOrNull
import com.eitanliu.utils.isAppearanceLightStatusBars

abstract class BindingDialogFragment<VB : ViewDataBinding, VM : BindingViewModel> :
    DialogFragment(), DialogLifecycle.OnCreateListener, DialogInterface.OnShowListener,
    BindingView, ActivityLauncher {

    var onCreateListener: DialogLifecycle.OnCreateListener? = null
    var onDismissListener: DialogInterface.OnDismissListener? = null
    var onCancelListener: DialogInterface.OnCancelListener? = null
    var onShowListener: DialogInterface.OnShowListener? = null

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
        ensureBinding()
        observeActivityUiState()
        observeDialogUiState()
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
            dialog.asTypeOrNull<ComponentDialog>()?.onBackPressedDispatcher?.onBackPressed()
        }
        viewModel.onBackPressedEnable.observe(viewLifecycleOwner) { value ->
            onBackPressedHandler.isEnabled = value != false
        }
        viewModel.state.handleOnBackPressed.observe(viewLifecycleOwner) { callback ->
            onBackPressedHandler.onBackPressed = callback
            onBackPressedHandler.isEnabled = viewModel.onBackPressedEnable.value != false
            if (callback != null) {
                dialog.asTypeOrNull<ComponentDialog>()?.also { dialog ->
                    dialog.onBackPressedDispatcher.addCallback(dialog, onBackPressedHandler)
                }
            } else {
                onBackPressedHandler.remove()
            }
        }
        // 系统栏显示控制
        viewModel.lightStatusBars.observe(viewLifecycleOwner) { state ->
            if (state == null) return@observe
            dialog?.window?.isAppearanceLightStatusBars = state == true
        }
        viewModel.lightNavigationBars.observe(viewLifecycleOwner) { state ->
            val color = viewModel.navigationBarsColor.value
            if (color == null && state == null) return@observe
            val isLight = state == true
            dialog?.also { dialog ->
                dialog.window?.setNavBar(isLight, color)
            }
        }
        viewModel.navigationBarsColor.observe(viewLifecycleOwner) { color ->
            val state = viewModel.lightNavigationBars.value
            if (color == null && state == null) return@observe
            val isLight = state == true
            dialog?.also { dialog ->
                dialog.window?.setNavBar(isLight, color)
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

    protected open fun observeDialogUiState() {
        val vm = viewModel.asTypeOrNull<IDialog>() ?: return
        vm.canceledOnTouchOutside.observe(this) {
            dialog?.setCanceledOnTouchOutside(it)
        }
        vm.state.dismiss.observe(this) {
            dismiss()
        }
        vm.state.dismissNow.observe(this) {
            dismissNow()
        }
        vm.state.dismissAllowingStateLoss.observe(this) {
            dismissAllowingStateLoss()
        }
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

    override fun startActivity(info: ActivityLauncherInfo) {
        activity.asTypeOrNull<BindingActivity<*, *>>()?.startActivity(info)
    }
}