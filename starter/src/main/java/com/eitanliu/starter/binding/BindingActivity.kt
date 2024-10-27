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
import androidx.core.graphics.Insets
import androidx.core.util.containsKey
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eitanliu.binding.ViewBindingUtil
import com.eitanliu.binding.adapter.fitWindowInsets
import com.eitanliu.binding.extension.isAppearanceLightStatusBars
import com.eitanliu.starter.binding.handler.OnBackPressedHandler
import com.eitanliu.starter.binding.model.ActivityLauncherInfo
import com.eitanliu.starter.binding.registry.ISystemInsets
import com.eitanliu.starter.utils.ReflectionUtil
import com.eitanliu.utils.BarUtil.setNavBar
import com.eitanliu.utils.refWeak
import java.lang.ref.Reference
import java.util.Random

abstract class BindingActivity<VB : ViewDataBinding, VM : BindingViewModel> : AppCompatActivity(),
    BindingView, ActivityLauncher, BindingDelegate {

    override val bindingOwner: BindingOwner = BindingOwner.Impl().bind(this)

    protected lateinit var binding: VB

    protected lateinit var viewModel: VM

    val systemInsetsRegistry get() = viewModel as ISystemInsets

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
        // viewModel.observeSystemInsets(this, systemInsetsRegistry)
        // 系统栏显示控制
        systemInsetsRegistry.lightStatusBars.observe(this) { state ->
            if (state == null) return@observe
            isAppearanceLightStatusBars = state == true
        }
        systemInsetsRegistry.lightNavigationBars.observe(this) { state ->
            val color = systemInsetsRegistry.navigationBarsColor.value
            if (color == null && state == null) return@observe
            val isLight = state == true
            window.setNavBar(isLight, color)
        }
        systemInsetsRegistry.navigationBarsColor.observe(this) { color ->
            val state = systemInsetsRegistry.lightNavigationBars.value
            if (color == null && state == null) return@observe
            val isLight = state == true
            window.setNavBar(isLight, color)
        }
        systemInsetsRegistry.fitSystemBars.observe(this) {
            ViewCompat.requestApplyInsets(binding.root.rootView)
            fitWindowInsets()
        }
        systemInsetsRegistry.fitStatusBars.observe(this, fixWindowInsetsObserver)
        systemInsetsRegistry.fitNavigationBars.observe(this) {
            ViewCompat.requestApplyInsets(binding.root.rootView)
            fitWindowInsets()
        }
        systemInsetsRegistry.fitCaptionBar.observe(this, fixWindowInsetsObserver)
        systemInsetsRegistry.fitDisplayCutout.observe(this, fixWindowInsetsObserver)
        systemInsetsRegistry.fitHorizontal.observe(this, fixWindowInsetsObserver)
        systemInsetsRegistry.fitMergeType.observe(this, fixWindowInsetsObserver)
        systemInsetsRegistry.fitInsetsType.observe(this) {
            ViewCompat.requestApplyInsets(binding.root.rootView)
            fitWindowInsets()
        }
        systemInsetsRegistry.fitInsetsMode.observe(this) { fitWindowInsets() }
    }

    private val fixWindowInsetsObserver = Observer<Boolean?> {
        fitWindowInsets()
    }

    private fun fitWindowInsets() {
        binding.root.fitWindowInsets(
            systemInsetsRegistry.fitSystemBars.value,
            systemInsetsRegistry.fitStatusBars.value,
            systemInsetsRegistry.fitNavigationBars.value,
            systemInsetsRegistry.fitCaptionBar.value,
            systemInsetsRegistry.fitDisplayCutout.value,
            systemInsetsRegistry.fitHorizontal.value,
            systemInsetsRegistry.fitMergeType.value,
            systemInsetsRegistry.fitInsetsType.value,
            systemInsetsRegistry.fitInsetsMode.value,
            // ViewCompat.getRootWindowInsets(binding.root)
        ) { v, wInsets ->
            val builder = WindowInsetsCompat.Builder(wInsets)
            val needFixNav = systemInsetsRegistry.fitSystemBars.value == true
                    || systemInsetsRegistry.fitNavigationBars.value == true
            val rootInsets = ViewCompat.getRootWindowInsets(binding.root)
            val imeInsets = rootInsets!!.getInsets(WindowInsetsCompat.Type.ime())
            val navInsets = rootInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            if (needFixNav) {
                val fixImeInsets = Insets.max(Insets.subtract(imeInsets, navInsets), Insets.NONE)
                builder.setInsets(WindowInsetsCompat.Type.ime(), fixImeInsets)
                builder.setInsets(WindowInsetsCompat.Type.navigationBars(), Insets.NONE)
            }
            val insets = builder.build()
            insets
        }

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