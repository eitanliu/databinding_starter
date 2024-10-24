package com.example.app.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.context
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

    override val initContentView = R.layout.activity_example

    override val initVariableId = BR.viewModel

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
    }

    override fun attachBaseContext(newBase: Context) {
        val displayMetrics = newBase.resources.displayMetrics
        val width = min(displayMetrics.widthPixels, displayMetrics.heightPixels)
        val designWidth = 420.0
        val designDip = width / designWidth
        val differDip = designDip - displayMetrics.density
        Logcat.msg("differDip $designDip - ${displayMetrics.density} = $differDip")
        // 避免差异过大，超过指定系数使用系统默认值
        val fixDip = if (abs(differDip) < 0.6) designDip else displayMetrics.density.toDouble()
        val fixDpi = (fixDip * 160).roundToInt()
        val conf = Configuration().apply {
            densityDpi = fixDpi
        }
        val context = newBase.createConfigurationContext(conf)
        return super.attachBaseContext(context)
    }
}