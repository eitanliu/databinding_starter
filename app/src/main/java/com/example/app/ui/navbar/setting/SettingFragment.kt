package com.example.app.ui.navbar.setting

import com.eitanliu.starter.binding.BindingFragment
import com.example.app.BR
import com.example.app.R
import com.example.app.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BindingFragment<FragmentHomeBinding, SettingVM>() {

    override val bindLayoutId = R.layout.fragment_setting

    override val bindVariableId: Int = BR.viewModel
}