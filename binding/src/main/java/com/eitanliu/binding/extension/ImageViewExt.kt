package com.eitanliu.binding.extension

import android.widget.ImageView
import com.eitanliu.binding.R
import com.eitanliu.binding.model.CacheImage
import com.eitanliu.utils.getBindingTag
import com.eitanliu.utils.setBindingTag

class ImageViewExt

var ImageView.cacheImage
    get() = getBindingTag(R.id.cacheImage) as? CacheImage
    set(value) {
        setBindingTag(R.id.cacheImage, value)
    }