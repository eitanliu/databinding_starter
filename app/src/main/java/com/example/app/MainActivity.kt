package com.example.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.view.ContextThemeWrapper
import com.eitanliu.starter.binding.BindingActivity
import com.eitanliu.utils.Logcat
import com.example.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding, MainVM>() {


    override val initContentView = R.layout.activity_main

    override val initVariableId = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logcat.msg("${savedInstanceState}")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Logcat.msg("${newConfig.uiMode}")
    }


}