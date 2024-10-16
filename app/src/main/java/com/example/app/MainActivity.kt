package com.example.app

import android.os.Bundle
import com.eitanliu.starter.binding.BindingActivity
import com.example.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding, MainVM>() {


    override val initContentView = R.layout.activity_main

    override val initVariableId = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}