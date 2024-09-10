package com.example.binding.adapter

import android.app.Activity
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.example.base.extension.then
import com.example.base.utils.ResourceUtil
import com.example.binding.annotation.ResourcesId

class ImageViewAdapter

/**
 * 加载图片绑定
 *
 * @param image Any?
 * @param imagePlaceholder Drawable?
 */
@BindingAdapter(
    "image",
    "imagePlaceholder",
    "resPlaceholder",
    "colorPlaceholder",
    "priority",
    "clearOnDetach",
    "skipMemoryCache",
    "diskCacheStrategy",
    "crossFade",
    "isCrossFade",
    requireAll = false
)
fun ImageView.loadImage(
    image: Any?,
    imagePlaceholder: Drawable? = null,
    @DrawableRes resPlaceholder: Int? = null,
    @ColorInt colorPlaceholder: Int? = null,
    priority: Priority? = null,
    clearOnDetach: Boolean? = null,
    skipMemoryCache: Boolean? = null,
    @ImageDiskCacheStrategy diskCacheStrategy: Int? = null,
    crossFade: Int? = null,
    isCrossFade: Boolean? = null,
) {

    val context = context
    if (context is Activity && context.isDestroyed) {
        return
    }

    val placeholder: Drawable? = when {

        imagePlaceholder != null -> {
            imagePlaceholder
        }

        resPlaceholder != null -> {
            ResourceUtil.getDrawable(resPlaceholder)
        }

        colorPlaceholder != null -> {
            GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(colorPlaceholder))
        }

        else -> null
    }

    fun checkEmpty(): Boolean {
        return image == null || (image as? String) == ""
                || (image as? Int) == ResourcesId.ID_NULL
                || (image as? Uri) == Uri.EMPTY
    }
    if (checkEmpty()) {
        Glide.with(this).clear(this)
        this.setImageDrawable(placeholder)
        return
    }

    val builder = Glide.with(this).load(image).apply(
        RequestOptions().placeholder(placeholder)
            .priority(priority ?: Priority.NORMAL)
    ).run {
        (skipMemoryCache == true).then({ skipMemoryCache(true) }, { this })
    }.run {
        if (diskCacheStrategy == null) return@run this

        when (diskCacheStrategy) {
            ImageDiskCacheStrategy.ALL -> diskCacheStrategy(DiskCacheStrategy.ALL)
            ImageDiskCacheStrategy.DATA -> diskCacheStrategy(DiskCacheStrategy.DATA)
            ImageDiskCacheStrategy.RESOURCE -> diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            ImageDiskCacheStrategy.AUTOMATIC -> diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            ImageDiskCacheStrategy.NONE -> diskCacheStrategy(DiskCacheStrategy.NONE)
            else -> this
        }
    }.run {
        (isCrossFade == true).then({
            transition(DrawableTransitionOptions.withCrossFade(
                DrawableCrossFadeFactory.Builder(crossFade.takeIf {
                    it != null && it > 0
                } ?: 300).setCrossFadeEnabled(true)
            ))
        }, { this })
    }

    val viewTarget = builder.into(this)
    if (clearOnDetach == true) {
        viewTarget.clearOnDetach()
    }
}

// @BindingAdapter("android:src")
// fun ImageView.setImageSrc(@DrawableRes resId: Int) {
//     setImageDrawable(ResourceUtil.getDrawable(resId))
// }

@IntDef(
    ImageDiskCacheStrategy.NONE,
    ImageDiskCacheStrategy.ALL,
    ImageDiskCacheStrategy.DATA,
    ImageDiskCacheStrategy.RESOURCE,
    ImageDiskCacheStrategy.AUTOMATIC,
)
@Retention(AnnotationRetention.SOURCE)
annotation class ImageDiskCacheStrategy {
    companion object {
        /**
         * [DiskCacheStrategy.NONE]
         */
        const val NONE = 1

        const val ALL = 2

        const val DATA = 3

        const val RESOURCE = 4

        const val AUTOMATIC = 5
    }
}
