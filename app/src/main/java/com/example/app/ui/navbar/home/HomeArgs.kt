package com.example.app.ui.navbar.home

import android.os.Bundle
import com.eitanliu.starter.bundle.BundleDelegate
import com.eitanliu.starter.bundle.getValue
import com.eitanliu.starter.bundle.property
import com.eitanliu.starter.bundle.setValue

class HomeArgs(
    override val bundle: Bundle = Bundle()
) : BundleDelegate {

    var fArg1 by property<String>("0")

}