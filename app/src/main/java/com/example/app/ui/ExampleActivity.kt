package com.example.app.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.DisplayCompat
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

    override fun attachBaseContext(newBase: Context) {
        val displayMetrics = newBase.resources.displayMetrics
        // val width = min(displayMetrics.widthPixels, displayMetrics.heightPixels)
        val mode = DisplayCompat.getMode(newBase, ContextCompat.getDisplayOrDefault(newBase))
        val width = min(mode.physicalWidth, mode.physicalHeight)
        val designWidth = 420.0
        val designDip = width / designWidth
        val designDpi = (designDip * 160).roundToInt()
        val conf = Configuration(newBase.resources.configuration)
        val differDip = designDip - displayMetrics.density
        Logcat.msg("width $width, differDip $designDip - ${displayMetrics.density} = $differDip")
        // 避免差异过大，超过指定系数使用系统默认值
        if (abs(differDip) < 0.6) conf.densityDpi = designDpi
        val context = newBase.createConfigurationContext(conf)
        return super.attachBaseContext(context)
    }
}