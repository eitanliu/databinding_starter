package com.example.app

import android.os.Bundle
import com.eitanliu.starter.bundle.BundleDelegate
import com.eitanliu.starter.bundle.getValue
import com.eitanliu.starter.bundle.property
import com.eitanliu.starter.bundle.setValue

class MainArgs(
    override val bundle: Bundle = Bundle()
) : BundleDelegate {
    var arg1 by property("1")
    var arg2 by property(22)
}