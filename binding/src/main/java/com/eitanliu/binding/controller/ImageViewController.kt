package com.eitanliu.binding.controller

import android.widget.ImageView
import com.eitanliu.binding.model.CacheImage

class ImageViewController(
    override val view: ImageView
) : ViewController {
    companion object Key : ViewController.Key<ImageViewController>

    override val key: ViewController.Key<*> = Key

    var cacheImage: CacheImage? = null
}