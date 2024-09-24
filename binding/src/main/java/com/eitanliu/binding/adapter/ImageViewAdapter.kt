package com.eitanliu.binding.adapter

import android.app.Activity
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.eitanliu.binding.annotation.ResourcesId
import com.eitanliu.binding.extension.cacheImage
import com.eitanliu.binding.model.CacheImage

class ImageViewAdapter

/**
 * 加载图片绑定
 *
 * @param image Any? 加载图片
 * @param imagePreview Any? 预加载图片
 * @param imagePlaceholder Drawable? 占位图
 */
@BindingAdapter(
    "image",
    "imagePreview",
    "imagePlaceholder",
    "resPlaceholder",
    "colorPlaceholder",
    "priority",
    "skipMemoryCache",
    "diskCacheStrategy",
    "crossFade",
    "isCrossFade",
    "isGif",
    "clearOnDetach",
    requireAll = false
)
fun ImageView.loadImage(
    image: Any?,
    imagePreview: Any? = null,
    imagePlaceholder: Drawable? = null,
    @DrawableRes resPlaceholder: Int? = null,
    @ColorInt colorPlaceholder: Int? = null,
    priority: Priority? = null,
    skipMemoryCache: Boolean? = null,
    @ImageDiskCacheStrategy diskCacheStrategy: Int? = null,
    crossFade: Int? = null,
    isCrossFade: Boolean? = null,
    isGif: Boolean? = null,
    clearOnDetach: Boolean? = null,
): ImageViewTarget<out Drawable>? {

    val context = context
    if (context is Activity && context.isDestroyed) {
        return null
    }

    val placeholder: Drawable? = when {

        imagePlaceholder != null -> {
            imagePlaceholder
        }

        resPlaceholder != null -> {
            ContextCompat.getDrawable(context, resPlaceholder)
        }

        colorPlaceholder != null -> {
            GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(colorPlaceholder))
        }

        else -> null
    }

    fun checkEmpty(image: Any?): Boolean {
        return image == null || (image as? String) == ""
                || (image as? Int) == ResourcesId.ID_NULL
                || (image as? Uri) == Uri.EMPTY
    }

    if (cacheImage?.model != image) cacheImage = null

    if (!checkEmpty(imagePreview)) {
        val previewBuilder = loadImageBuilder(
            imagePreview, placeholder, priority,
            skipMemoryCache, diskCacheStrategy,
            crossFade, isCrossFade,
        ) { result: Result<Drawable> ->
            result.onSuccess { resource ->
                // Log.e("LoadImage", "preview $result $preview")

                if (cacheImage?.drawable == null) {
                    setImageDrawable(resource)
                    // loadImage(
                    //     resource, placeholder, priority,
                    //     skipMemoryCache, diskCacheStrategy,
                    //     crossFade, isCrossFade, clearOnDetach
                    // )
                }
            }
            false
        }
        // Log.e("LoadImage", "preview load $preview")
        previewBuilder?.preload()
    }

    fun load(): RequestBuilder<out Drawable>? {
        val imageListener = { result: Result<Drawable> ->
            // Log.e("LoadImage", "image $result $image")
            cacheImage = CacheImage(image, result.getOrNull())
            false
        }

        // Log.e("LoadImage", "image load $image")
        return if (isGif == true) {
            loadGifBuilder(
                image, placeholder, priority,
                skipMemoryCache, diskCacheStrategy,
                crossFade, isCrossFade, imageListener
            )
        } else {
            loadImageBuilder(
                image, placeholder, priority,
                skipMemoryCache, diskCacheStrategy,
                crossFade, isCrossFade, imageListener
            )
        }
    }

    val imageTarget = load()?.into(this) as? ImageViewTarget<out Drawable>
    if (clearOnDetach == true) {
        imageTarget?.clearOnDetach()
    }
    return imageTarget
}

fun ImageView.loadImage(
    image: Any?,
    placeholder: Drawable? = null,
    priority: Priority? = null,
    skipMemoryCache: Boolean? = null,
    @ImageDiskCacheStrategy diskCacheStrategy: Int? = null,
    crossFade: Int? = null,
    isCrossFade: Boolean? = null,
    clearOnDetach: Boolean? = null,
    listener: ((Result<Drawable>) -> Boolean)? = null,
): ImageViewTarget<Drawable>? {
    val builder = loadImageBuilder(
        image, placeholder, priority,
        skipMemoryCache, diskCacheStrategy,
        crossFade, isCrossFade, listener
    )
    val imageTarget = builder?.into(this) as? ImageViewTarget<Drawable>
    if (clearOnDetach == true) {
        imageTarget?.clearOnDetach()
    }
    return imageTarget
}

fun ImageView.loadImageBuilder(
    image: Any?,
    placeholder: Drawable? = null,
    priority: Priority? = null,
    skipMemoryCache: Boolean? = null,
    @ImageDiskCacheStrategy diskCacheStrategy: Int? = null,
    crossFade: Int? = null,
    isCrossFade: Boolean? = null,
    listener: ((Result<Drawable>) -> Boolean?)? = null,
): RequestBuilder<Drawable>? {
    fun checkEmpty(image: Any?): Boolean {
        return image == null || (image as? String) == ""
                || (image as? Int) == ResourcesId.ID_NULL
                || (image as? Uri) == Uri.EMPTY
    }

    if (checkEmpty(image)) {
        Glide.with(this).clear(this)
        this.setImageDrawable(placeholder)
        return null
    }

    val builder = Glide.with(this).load(image).apply(
        RequestOptions().placeholder(placeholder)
            .priority(priority ?: Priority.NORMAL)
    ).run {
        if (skipMemoryCache == true) skipMemoryCache(true) else this
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
        if (isCrossFade == true) transition(DrawableTransitionOptions.withCrossFade(
            DrawableCrossFadeFactory.Builder(crossFade.takeIf {
                it != null && it > 0
            } ?: 300).setCrossFadeEnabled(true)
        )) else this
    }.run {
        if (listener != null) listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean
            ): Boolean {
                return listener.invoke(
                    Result.failure(e ?: NullPointerException("Glide LoadFailed"))
                ) ?: false
            }

            override fun onResourceReady(
                resource: Drawable, model: Any, target: Target<Drawable>?,
                dataSource: DataSource, isFirstResource: Boolean
            ): Boolean {
                return listener.invoke(Result.success(resource)) ?: false
            }
        }) else this
    }
    return builder
}

fun ImageView.loadGifBuilder(
    image: Any?,
    placeholder: Drawable? = null,
    priority: Priority? = null,
    skipMemoryCache: Boolean? = null,
    @ImageDiskCacheStrategy diskCacheStrategy: Int? = null,
    crossFade: Int? = null,
    isCrossFade: Boolean? = null,
    listener: ((Result<GifDrawable>) -> Boolean?)? = null,
): RequestBuilder<GifDrawable>? {
    fun checkEmpty(image: Any?): Boolean {
        return image == null || (image as? String) == ""
                || (image as? Int) == ResourcesId.ID_NULL
                || (image as? Uri) == Uri.EMPTY
    }

    if (checkEmpty(image)) {
        Glide.with(this).clear(this)
        this.setImageDrawable(placeholder)
        return null
    }

    val builder = Glide.with(this).asGif().load(image).apply(
        RequestOptions().placeholder(placeholder)
            .priority(priority ?: Priority.NORMAL)
    ).run {
        if (skipMemoryCache == true) skipMemoryCache(true) else this
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
        if (isCrossFade == true) transition(DrawableTransitionOptions.withCrossFade(
            DrawableCrossFadeFactory.Builder(crossFade.takeIf {
                it != null && it > 0
            } ?: 300).setCrossFadeEnabled(true)
        )) else this
    }.run {
        if (listener != null) listener(object : RequestListener<GifDrawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable>,
                isFirstResource: Boolean
            ): Boolean {
                return listener.invoke(
                    Result.failure(e ?: NullPointerException("Glide LoadFailed"))
                ) ?: false
            }

            override fun onResourceReady(
                resource: GifDrawable, model: Any, target: Target<GifDrawable>?,
                dataSource: DataSource, isFirstResource: Boolean
            ): Boolean {
                return listener.invoke(Result.success(resource)) ?: false
            }
        }) else this
    }
    return builder
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
