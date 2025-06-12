package com.example.app.ui

import android.content.Context
import android.content.ContextParams
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.DisplayCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import com.eitanliu.binding.adapter.imageViewController
import com.eitanliu.binding.adapter.viewController
import com.eitanliu.binding.adapter.viewExtController
import com.eitanliu.binding.controller.AttachStateChangeListener
import com.eitanliu.binding.controller.ViewController
import com.eitanliu.binding.extension.context
import com.eitanliu.starter.binding.BindingActivity
import com.eitanliu.utils.Logcat
import com.eitanliu.utils.contextTree
import com.example.app.BR
import com.example.app.R
import com.example.app.databinding.ActivityExampleBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

@AndroidEntryPoint
class ExampleActivity : BindingActivity<ActivityExampleBinding, ExampleVM>() {

    override val bindLayoutId = R.layout.activity_example

    override val bindVariableId = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.contextTree {
            Logcat.msg("$it")
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        resources.configuration.orientation
        val display = ContextCompat.getDisplayOrDefault(this)
        display.rotation

        ViewCompat.setWindowInsetsAnimationCallback(
            binding.root,
            TranslateDeferringInsetsAnimationCallback(
                view = binding.root,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime()
            )
        )
    }

    override fun observeActivityUiState() {
        super.observeActivityUiState()
        viewModel.state.testState.observe(this) {
            recreate()
        }
        testViewAttach()
    }

    private fun testViewAttach() {
        binding.root.viewExtController.observe(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                Logcat.msg("$v")
            }

            override fun onViewDetachedFromWindow(v: View) {
                Logcat.msg("$v")
            }
        })
        binding.root.viewExtController.viewLifecycleOwner.observe(object :
            AttachStateChangeListener {
            override fun onAttachedState(owner: LifecycleOwner) {
                Logcat.msg("${owner.lifecycle.currentState}")
            }

            override fun onDetachedState(owner: LifecycleOwner) {
                Logcat.msg("${owner.lifecycle.currentState}")
            }
        })
    }

    override fun onResume() {
        super.onResume()

        binding.root.context.contextTree {
            Logcat.msg("$it", Logcat.E)
        }
        val viewController = binding.ivTest.viewController
        val extController = binding.ivTest.viewExtController
        val imageViewController = binding.ivTest.imageViewController
        val controllers = viewController.fold(arrayListOf<ViewController>()) { list, controller ->
            list.apply { add(controller) }
        }
        Logcat.msg("ViewController $controllers, $viewController")
        Logcat.msg("ViewController $extController, $imageViewController")
        // binding.ivTest.viewController += ViewController.Empty
    }

    override fun getTheme(): Resources.Theme {
        return super.getTheme()
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        Logcat.msg("applyOverrideConfiguration $overrideConfiguration", Logcat.E)
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Logcat.msg("onConfigurationChanged", Logcat.E)
        super.onConfigurationChanged(newConfig)
    }

    override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
        Logcat.msg("createConfigurationContext $overrideConfiguration", Logcat.E)
        return super.createConfigurationContext(overrideConfiguration)
    }

    override fun createContext(contextParams: ContextParams): Context {
        Logcat.msg("createContext $contextParams", Logcat.E)
        return super.createContext(contextParams)
    }

    override fun createAttributionContext(attributionTag: String?): Context {
        Logcat.msg("createAttributionContext $attributionTag", Logcat.E)
        return super.createAttributionContext(attributionTag)
    }

    override fun createContextForSplit(splitName: String?): Context {
        Logcat.msg("createContextForSplit $splitName", Logcat.E)
        return super.createContextForSplit(splitName)
    }

    override fun createPackageContext(packageName: String?, flags: Int): Context {
        Logcat.msg("createPackageContext $packageName, $flags", Logcat.E)
        return super.createPackageContext(packageName, flags)
    }

    override fun createDeviceProtectedStorageContext(): Context {
        Logcat.msg("createDeviceProtectedStorageContext", Logcat.E)
        return super.createDeviceProtectedStorageContext()
    }

    override fun createDeviceContext(deviceId: Int): Context {
        Logcat.msg("createDeviceContext $deviceId", Logcat.E)
        return super.createDeviceContext(deviceId)
    }

    override fun createDisplayContext(display: Display): Context {
        Logcat.msg("createDisplayContext $display", Logcat.E)
        return super.createDisplayContext(display)
    }

    override fun createWindowContext(type: Int, options: Bundle?): Context {
        Logcat.msg("createWindowContext $type, $options", Logcat.E)
        return super.createWindowContext(type, options)
    }

    override fun createWindowContext(display: Display, type: Int, options: Bundle?): Context {
        Logcat.msg("createWindowContext Display $display, $type, $options", Logcat.E)
        return super.createWindowContext(display, type, options)
    }

    override fun attachBaseContext(newBase: Context) {
        val conf = updateConfiguration(newBase)
        // Plan 1：使用 Context.createConfigurationContext
        // val context = newBase.createConfigurationContext(conf)
        // return super.attachBaseContext(context)
        // Plan 2：使用 Configuration.updateFrom
        newBase.resources.configuration.updateFrom(conf)
        return super.attachBaseContext(newBase)
    }

    private fun updateConfiguration(context: Context): Configuration {
        val displayMetrics = context.resources.displayMetrics
        // val width = min(displayMetrics.widthPixels, displayMetrics.heightPixels)
        val display = ContextCompat.getDisplayOrDefault(context)
        val mode = DisplayCompat.getMode(context, display)
        val width = min(mode.physicalWidth, mode.physicalHeight)
        val resources = context.resources
        val configuration = resources.configuration
        val designWidth = 420.0
        val designDesign = width / designWidth
        val designDpi = (designDesign * 160).roundToInt()
        val newConfiguration = Configuration(configuration)
        val differDip = designDesign - displayMetrics.density
        Logcat.msg("Display $display")
        Logcat.msg("DisplayMetrics $${DisplayMetrics.DENSITY_DEFAULT}, ${DisplayMetrics.DENSITY_DEVICE_STABLE}; $displayMetrics")
        Logcat.msg("width $width, differ density $designDesign - ${displayMetrics.density} = $differDip")
        Logcat.msg("width $width, differ dpi $designDpi - ${configuration.densityDpi} = ${designDpi - configuration.densityDpi}")
        // 避免差异过大，超过指定系数使用系统默认值
        if (abs(differDip) < 0.6) {
            newConfiguration.densityDpi = designDpi
        }
        return newConfiguration
    }
}