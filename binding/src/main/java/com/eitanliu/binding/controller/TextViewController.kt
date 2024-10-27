package com.eitanliu.binding.controller

import android.view.View
import com.eitanliu.binding.model.FontFamilyTag

class TextViewController(
    override val view: View
) : ViewController {
    companion object Key : ViewController.Key<TextViewController>

    override val key: ViewController.Key<*> = Key

    var fontFamilyTag: FontFamilyTag? = null
}