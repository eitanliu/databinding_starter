package com.eitanliu.starter.binding.controller

import android.view.MenuItem
import android.view.View
import com.eitanliu.binding.controller.ViewController

class NavigationBarController(
    override val view: View
) : ViewController {
    companion object Key : ViewController.Key<NavigationBarController>

    override val key: ViewController.Key<*> = Key

    var menuItemSelected: MenuItem? = null
}