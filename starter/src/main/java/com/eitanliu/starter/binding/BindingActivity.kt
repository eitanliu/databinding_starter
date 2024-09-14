package com.eitanliu.starter.binding

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.containsKey
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eitanliu.binding.adapter.fitWindowInsets
import com.eitanliu.binding.extension.isAppearanceLightStatusBars
import com.eitanliu.binding.extension.refWeak
import com.eitanliu.starter.binding.model.ActivityLaunchModel
import com.eitanliu.starter.utils.ReflectionUtil
import java.lang.ref.Reference
import java.util.Random

abstract class BindingActivity<VB : ViewDataBinding, VM : BindingViewModel> : AppCompatActivity(),
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initParams(savedInstanceState)
        initViewDataBinding()
        registerUiStateChange()
        initData()
        initView()
        initObserve()
    }

    private fun initViewDataBinding() {
        binding = DataBindingUtil.setContentView(this, initContentView)
        viewModel = createViewModel()
        binding.setVariable(initVariableId, viewModel)
        binding.lifecycleOwner = this
    }

    open fun createViewModel() = ViewModelProvider(this, viewModelFactory)[viewModelType]

    private fun registerUiStateChange() {
        viewModel.state.startActivity.observe(this) {
            startActivity(it)
        }
        viewModel.state.finish.observe(this) {
            finish()
        }
        viewModel.state.onBackPressed.observe(this) {
            onBackPressedDispatcher.onBackPressed()
        }
        // 系统栏显示控制
        viewModel.lightStatusBars.observe(this) {
            isAppearanceLightStatusBars = it == true
        }
        viewModel.fitSystemBars.observe(this, fixWindowInsets)
        viewModel.fitStatusBars.observe(this, fixWindowInsets)
        viewModel.fitNavigationBars.observe(this, fixWindowInsets)
        viewModel.fitCaptionBar.observe(this, fixWindowInsets)
        viewModel.fitDisplayCutout.observe(this, fixWindowInsets)
        viewModel.fitHorizontal.observe(this, fixWindowInsets)
        viewModel.fitMergeType.observe(this, fixWindowInsets)
        viewModel.fitInsetsType.observe(this) { fitWindowInsets() }
        viewModel.fitInsetsMode.observe(this) { fitWindowInsets() }
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

    /**
     * 跳转页面
     * @param model
     */
    fun startActivity(
        model: ActivityLaunchModel
    ) {
        val intent = Intent(this, model.clz)
        if (model.bundle != null) {
            intent.putExtras(model.bundle)
        }
        if (model.callback != null) {
            val requestCode = generateRandomNumber()
            requestCallbacks[requestCode] = model.callback.refWeak()
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