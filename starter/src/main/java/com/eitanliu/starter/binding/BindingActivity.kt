package com.eitanliu.starter.binding

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.SparseArray
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.containsKey
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eitanliu.binding.ViewBindingUtil
import com.eitanliu.binding.adapter.fitWindowInsets
import com.eitanliu.binding.extension.isAppearanceLightStatusBars
import com.eitanliu.starter.binding.handler.OnBackPressedHandler
import com.eitanliu.starter.binding.model.ActivityLauncherInfo
import com.eitanliu.starter.utils.ReflectionUtil
import com.eitanliu.utils.BarUtil.setNavBar
import com.eitanliu.utils.refWeak
import java.lang.ref.Reference
import java.util.Random

abstract class BindingActivity<VB : ViewDataBinding, VM : BindingViewModel> : AppCompatActivity(),
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

    private val codeRandom = Random()
    private val requestCallbacks = SparseArray<Reference<ActivityResultCallback<ActivityResult>>>()
    private val onBackPressedHandler by lazy { OnBackPressedHandler(true) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initParams(savedInstanceState)
        ensureBinding()
        observeActivityUiState()
        bindData()
        bindView()
        bindObserve()
    }

    protected val uiModeNight get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.uiMode.value = resources.configuration.uiMode
    }

    open fun createViewModel() = ViewModelProvider(this)[viewModelType]

    private fun ensureBinding() {
        binding = if (bindLayoutId != ResourcesCompat.ID_NULL) {
            DataBindingUtil.setContentView(this, bindLayoutId)
        } else ViewBindingUtil.inflate(viewBindingType, layoutInflater, null, false).apply {
            setContentView(root)
        }
        viewModel = createViewModel()
        binding.setVariable(bindVariableId, viewModel)
        binding.lifecycleOwner = this
        lifecycle.addObserver(viewModel)
    }

    protected open fun observeActivityUiState() {
        viewModel.uiMode.value = resources.configuration.uiMode
        viewModel.state.startActivity.observe(this) {
            startActivity(it)
        }
        viewModel.state.finish.observe(this) {
            finish()
        }
        viewModel.state.onBackPressed.observe(this) {
            onBackPressedDispatcher.onBackPressed()
        }

        viewModel.onBackPressedEnable.observe(this) { value ->
            onBackPressedHandler.isEnabled = value != false
        }
        viewModel.state.handleOnBackPressed.observe(this) { callback ->
            onBackPressedHandler.onBackPressed = callback
            onBackPressedHandler.isEnabled = viewModel.onBackPressedEnable.value != false
            if (callback != null) {
                onBackPressedDispatcher.addCallback(this, onBackPressedHandler)
            } else {
                onBackPressedHandler.remove()
            }
        }
        // 系统栏显示控制
        viewModel.lightStatusBars.observe(this) { state ->
            if (state == null) return@observe
            isAppearanceLightStatusBars = state == true
        }
        viewModel.lightNavigationBars.observe(this) { state ->
            val color = viewModel.navigationBarsColor.value
            if (color == null && state == null) return@observe
            val isLight = state == true
            window.setNavBar(isLight, color)
        }
        viewModel.navigationBarsColor.observe(this) { color ->
            val state = viewModel.lightNavigationBars.value
            if (color == null && state == null) return@observe
            val isLight = state == true
            window.setNavBar(isLight, color)
        }
        viewModel.fitSystemBars.observe(this, fixWindowInsetsObserver)
        viewModel.fitStatusBars.observe(this, fixWindowInsetsObserver)
        viewModel.fitNavigationBars.observe(this, fixWindowInsetsObserver)
        viewModel.fitCaptionBar.observe(this, fixWindowInsetsObserver)
        viewModel.fitDisplayCutout.observe(this, fixWindowInsetsObserver)
        viewModel.fitHorizontal.observe(this, fixWindowInsetsObserver)
        viewModel.fitMergeType.observe(this, fixWindowInsetsObserver)
        viewModel.fitInsetsType.observe(this) { fitWindowInsets() }
        viewModel.fitInsetsMode.observe(this) { fitWindowInsets() }
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

    /**
     * 跳转页面
     * @param info
     */
    override fun startActivity(
        info: ActivityLauncherInfo
    ) {
        val intent = info.intent
        if (info.clz != null) info.intent.setClass(this, info.clz)

        if (info.callback != null) {
            val requestCode = generateRandomNumber()
            requestCallbacks[requestCode] = info.callback.refWeak()
            @Suppress("DEPRECATION")
            startActivityForResult(intent, requestCode)
        } else {
            startActivity(intent)
        }
    }

    private fun generateRandomNumber(): Int {
        var number: Int = codeRandom.nextInt(2147418112) + 65536
        while (requestCallbacks.containsKey(number)) {
            number = codeRandom.nextInt(2147418112) + 65536
        }
        return number
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val callback = requestCallbacks.get(requestCode)
        if (callback != null) {
            requestCallbacks.remove(resultCode)
            callback.get()?.onActivityResult(ActivityResult(resultCode, data))
        }
    }

}