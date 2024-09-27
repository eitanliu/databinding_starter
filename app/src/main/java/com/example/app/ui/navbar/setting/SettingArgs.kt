package com.example.app.ui.navbar.setting

import android.os.Bundle
import com.eitanliu.starter.bundle.BundleDelegate
import com.eitanliu.starter.bundle.getValue
import com.eitanliu.starter.bundle.property
import com.eitanliu.starter.bundle.setValue

class SettingArgs(
    override val bundle: Bundle = Bundle()
) : BundleDelegate {

    var fArg2 by property<String>("0")

}