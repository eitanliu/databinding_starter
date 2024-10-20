package com.example.app.ui

import android.os.Bundle
import com.eitanliu.starter.binding.BindingActivity
import com.example.app.BR
import com.example.app.R
import com.example.app.databinding.ActivityExampleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExampleActivity : BindingActivity<ActivityExampleBinding, ExampleVM>() {

    override val initContentView = R.layout.activity_example

    override val initVariableId = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun observeActivityUiState() {
        super.observeActivityUiState()
        viewModel.state.testState.observe(this) {
            recreate()
        }
    }
}