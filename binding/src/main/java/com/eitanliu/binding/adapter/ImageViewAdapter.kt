package com.eitanliu.binding.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethods
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.eitanliu.binding.annotation.ImageDiskCacheStrategy
import com.eitanliu.binding.annotation.ResourcesId
import com.eitanliu.binding.controller.ImageViewController
import com.eitanliu.binding.model.CacheImage

@BindingMethods(
    // BindingMethod(type = ImageView::class, attribute = "srcCompat", method = "setImageResource"),
)
class ImageViewAdapter

val ImageView.imageViewController
    get() = viewController[ImageViewController] ?: ImageViewController(this).also {
        viewController += it
    }

@BindingAdapter("srcCompat")
fun ImageView.setImageResource(@DrawableRes resId: Int?) {
    if (resId != null && resId != ResourcesCompat.ID_NULL) {
        setImageResource(resId)
    } else {
        setImageDrawable(null)
    }
}

/**
 * 加载图片绑定
 *
 * @param image Any? 加载图片
 * @param imageError Any? 出错图片
 * @param imageThumbnail Any? 预加载图片
 * @param imagePlaceholder Drawable? 占位图
 */
@BindingAdapter(
    "image",
    "imageError",
    "imageThumbnail",
    "imagePlaceholder",
    "resPlaceholder",
    "colorPlaceholder",
    "requestOptions",
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
    imageError: Any? = null,
    imageThumbnail: Any? = null,
    imagePlaceholder: Drawable? = null,
    @DrawableRes resPlaceholder: Int? = null,
    @ColorInt colorPlaceholder: Int? = null,
    requestOptions: RequestOptions? = null,
    skipMemoryCache: Boolean? = null,
    @ImageDiskCacheStrategy diskCacheStrategy: Int? = null,
    crossFade: Int? = null,
    isCrossFade: Boolean? = null,
    isGif: Boolean? = null,
    clearOnDetach: Boolean? = null,
): ImageViewTarget<Drawable>? {

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

    if (checkImageEmpty(image) && checkImageEmpty(imageError) && checkImageEmpty(imageThumbnail)) {
        Glide.with(this).clear(this)
        this.setImageDrawable(placeholder)
        return null
    }

    if (imageViewController.cacheImage?.model != image)
        imageViewController.cacheImage = CacheImage(image, null)

    fun load(): RequestBuilder<Drawable> {
        val imageListener = { result: Result<Drawable> ->
            // Log.e("LoadImage", "image $result $image")
            imageViewController.cacheImage = CacheImage(image, result.getOrNull())
            false
        }

        // Log.e("LoadImage", "image load $image")
        return context.loadImageBuilder(
            image, imageError, imageThumbnail, placeholder, requestOptions,
            skipMemoryCache, diskCacheStrategy,
            crossFade, isCrossFade, isGif, imageListener
        )
    }

    val builder = load()

    val imageTarget = builder.into(this) as? ImageViewTarget<Drawable>
    if (clearOnDetach == true) {
        imageTarget?.clearOnDetach()
    }
    return imageTarget
}

fun ImageView.loadImage(
    image: Any?,
    error: Any? = null,
    thumbnail: Any? = null,
    placeholder: Drawable? = null,
    requestOptions: RequestOptions? = null,
    skipMemoryCache: Boolean? = null,
    @ImageDiskCacheStrategy diskCacheStrategy: Int? = null,
    crossFade: Int? = null,
    isCrossFade: Boolean? = null,
    isGif: Boolean? = null,
    clearOnDetach: Boolean? = null,
    listener: ((Result<Drawable>) -> Boolean)? = null,
): ImageViewTarget<Drawable>? {

    if (checkImageEmpty(image) && checkImageEmpty(error) && checkImageEmpty(thumbnail)) {
        Glide.with(this).clear(this)
        this.setImageDrawable(placeholder)
        return null
    }

    val builder = context.loadImageBuilder(
        image, error, thumbnail, placeholder, requestOptions,
        skipMemoryCache, diskCacheStrategy,
        crossFade, isCrossFade, isGif, listener,
    )
    // .run {
    //     if (!checkImageEmpty(thumbnail)) {
    //         // Log.e("LoadImage", "thumbnail load $imageThumbnail")
    //         val thumbnailBuilder = context.loadImageBuilder(
    //             thumbnail, error, null, placeholder, priority,
    //             skipMemoryCache, diskCacheStrategy,
    //             crossFade, isCrossFade, null,
    //         )
    //         thumbnail(thumbnailBuilder)
    //     } else this
    // }
    val imageTarget = builder.into(this) as? ImageViewTarget<Drawable>
    if (clearOnDetach == true) {
        imageTarget?.clearOnDetach()
    }
    return imageTarget
}

fun Context.loadImageBuilder(
    image: Any?,
    error: Any? = null,
    thumbnail: Any? = null,
    placeholder: Drawable? = null,
    requestOptions: RequestOptions? = null,
    skipMemoryCache: Boolean? = null,
    @ImageDiskCacheStrategy diskCacheStrategy: Int? = null,
    crossFade: Int? = null,
    isCrossFade: Boolean? = null,
    isGif: Boolean? = null,
    listener: ((Result<Drawable>) -> Boolean?)? = null,
): RequestBuilder<Drawable> {

    val builder = Glide.with(this).run {
        @Suppress("UNCHECKED_CAST")
        if (isGif == true) asGif() as RequestBuilder<Drawable> else asDrawable()
    }.load(image).apply(
        (requestOptions ?: RequestOptions()).placeholder(placeholder)
    ).run {
        if (!checkImageEmpty(error)) error(error) else this
    }.run {
        if (!checkImageEmpty(thumbnail)) thumbnail(
            clone().error(null as? RequestBuilder<Drawable>?)
                .thumbnail(null as RequestBuilder<Drawable>?)
                .load(thumbnail)
        ) else this
    }.run {
        if (skipMemoryCache == true) skipMemoryCache(true) else this
    }.run {
        if (diskCacheStrategy != null) diskCacheStrategy(
            ImageDiskCacheStrategy.convert(diskCacheStrategy)
        ) else this
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

fun Context.loadBitmapBuilder(
    image: Any?,
    error: Any? = null,
    thumbnail: Any? = null,
    placeholder: Drawable? = null,
    requestOptions: RequestOptions? = null,
    skipMemoryCache: Boolean? = null,
    @ImageDiskCacheStrategy diskCacheStrategy: Int? = null,
    crossFade: Int? = null,
    isCrossFade: Boolean? = null,
    listener: ((Result<Bitmap>) -> Boolean?)? = null,
): RequestBuilder<Bitmap> {

    val builder = Glide.with(this).asBitmap().load(image).apply(
        (requestOptions ?: RequestOptions()).placeholder(placeholder)
    ).run {
        if (!checkImageEmpty(error)) error(error) else this
    }.run {
        if (!checkImageEmpty(thumbnail)) thumbnail(
            clone().error(null as? RequestBuilder<Bitmap>?)
                .thumbnail(null as RequestBuilder<Bitmap>?)
                .load(thumbnail)
        ) else this
    }.run {
        if (skipMemoryCache == true) skipMemoryCache(true) else this
    }.run {
        if (diskCacheStrategy != null) diskCacheStrategy(
            ImageDiskCacheStrategy.convert(diskCacheStrategy)
        ) else this
    }.run {
        if (isCrossFade == true) transition(BitmapTransitionOptions.withCrossFade(
            DrawableCrossFadeFactory.Builder(crossFade.takeIf {
                it != null && it > 0
            } ?: 300).setCrossFadeEnabled(true)
        )) else this
    }.run {
        if (listener != null) listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>,
                isFirstResource: Boolean
            ): Boolean {
                return listener.invoke(
                    Result.failure(e ?: NullPointerException("Glide LoadFailed"))
                ) ?: false
            }

            override fun onResourceReady(
                resource: Bitmap, model: Any, target: Target<Bitmap>?,
                dataSource: DataSource, isFirstResource: Boolean
            ): Boolean {
                return listener.invoke(Result.success(resource)) ?: false
            }
        }) else this
    }
    return builder
}

fun checkImageEmpty(image: Any?): Boolean {
    return image == null || (image as? String) == ""
            || (image as? Int) == ResourcesId.ID_NULL
            || (image as? Uri) == Uri.EMPTY
}
