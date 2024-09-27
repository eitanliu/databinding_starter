package com.example.app.ui.navbar.home

import com.eitanliu.starter.binding.BindingFragment
import com.example.app.BR
import com.example.app.R
import com.example.app.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding, HomeVM>() {

    override val initContentView = R.layout.fragment_home

    override val initVariableId: Int = BR.viewModel
}