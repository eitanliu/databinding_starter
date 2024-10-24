package com.example.app

import android.content.res.Configuration
import android.os.Bundle
import com.eitanliu.starter.binding.BindingActivity
import com.eitanliu.utils.Logcat
import com.example.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding, MainVM>() {


    override val bindLayoutId = R.layout.activity_main

    override val bindVariableId = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logcat.msg("${savedInstanceState}")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Logcat.msg("${newConfig.uiMode}")
    }


}