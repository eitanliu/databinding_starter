package com.example.app.ui.navbar

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.eitanliu.binding.extension.findOrCreateFragment
import com.eitanliu.binding.extension.show
import com.eitanliu.starter.binding.BindingActivity
import com.eitanliu.starter.extension.findOrCreateFragment
import com.example.app.BR
import com.example.app.R
import com.example.app.databinding.ActivityNavBarBinding
import com.example.app.ui.navbar.home.HomeArgs
import com.example.app.ui.navbar.home.HomeFragment
import com.example.app.ui.navbar.setting.SettingFragment
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavBarActivity : BindingActivity<ActivityNavBarBinding, NavBarVM>() {

    override val bindLayoutId = R.layout.activity_nav_bar

    override val bindVariableId = BR.viewModel

    private val homeFragment by lazy {
        supportFragmentManager.findOrCreateFragment<HomeFragment>(HomeArgs().apply {
            fArg1 = "333"
        })
    }

    private val settingFragment by lazy {
        supportFragmentManager.findOrCreateFragment<SettingFragment>()
    }

    private val navListener = object :
        NavigationBarView.OnItemSelectedListener,
        NavigationBarView.OnItemReselectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            item.order
            switchFragment(item.itemId)
            return true
        }

        override fun onNavigationItemReselected(item: MenuItem) {

        }
    }

    private var lastFragment: Fragment? = null
    private val containerViewId = R.id.mainContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initTab()
    }


    /**
     * [com.google.android.material.R.styleable.BottomNavigationView]
     *  [com.google.android.material.R.attr.bottomNavigationStyle]
     *  [com.google.android.material.R.style.Widget_Material3_BottomNavigationView]
     */
    private fun initTab() {
        binding.navView.setOnItemSelectedListener(navListener)
        binding.navView.setOnItemReselectedListener(navListener)
        lastFragment = supportFragmentManager.findFragmentById(containerViewId)
        if (lastFragment == null) switchFragment(binding.navView.selectedItemId)
    }

    private fun switchFragment(itemId: Int) {
        val cur = when (itemId) {
            R.id.homeFragment -> homeFragment
            R.id.settingFragment -> settingFragment
            else -> null
        } ?: return

        val tag = cur::class.java.name

        supportFragmentManager.show(binding.mainContainer.id, cur, tag)
            .commitAllowingStateLoss()
        lastFragment = cur
    }
}