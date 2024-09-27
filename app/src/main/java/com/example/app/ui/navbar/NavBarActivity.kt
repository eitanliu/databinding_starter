package com.example.app.ui.navbar

import com.eitanliu.starter.binding.BindingActivity
import com.example.app.BR
import com.example.app.R
import com.example.app.databinding.ActivityNavBarBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavBarActivity : BindingActivity<ActivityNavBarBinding, NavBarVM>() {

    override val initContentView = R.layout.activity_nav_bar

    override val initVariableId = BR.viewModel
}