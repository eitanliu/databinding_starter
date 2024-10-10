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
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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
    imageError: Any? = null,
    imageThumbnail: Any? = null,
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

    if (cacheImage?.model != image) cacheImage = CacheImage(image, null)

    fun load(): RequestBuilder<Drawable> {
        val imageListener = { result: Result<Drawable> ->
            // Log.e("LoadImage", "image $result $image")
            cacheImage = CacheImage(image, result.getOrNull())
            false
        }

        // Log.e("LoadImage", "image load $image")
        return context.loadImageBuilder(
            image, imageError, imageThumbnail, placeholder, priority,
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
    priority: Priority? = null,
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
        image, error, thumbnail, placeholder, priority,
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
    priority: Priority? = null,
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
        RequestOptions().placeholder(placeholder)
            .priority(priority ?: Priority.NORMAL)
    ).run {
        if (!checkImageEmpty(error)) error(
            Glide.with(this@loadImageBuilder).load(error)
        ) else this
    }.run {
        if (!checkImageEmpty(error)) thumbnail(
            Glide.with(this@loadImageBuilder).load(thumbnail)
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
    priority: Priority? = null,
    skipMemoryCache: Boolean? = null,
    @ImageDiskCacheStrategy diskCacheStrategy: Int? = null,
    crossFade: Int? = null,
    isCrossFade: Boolean? = null,
    listener: ((Result<Bitmap>) -> Boolean?)? = null,
): RequestBuilder<Bitmap> {

    val builder = Glide.with(this).asBitmap().load(image).apply(
        RequestOptions().placeholder(placeholder)
            .priority(priority ?: Priority.NORMAL)
    ).run {
        if (!checkImageEmpty(error)) error(error) else this
    }.run {
        @Suppress("CAST_NEVER_SUCCEEDS")
        if (!checkImageEmpty(error)) thumbnail(
            clone().error(null as? Any).load(thumbnail)
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

        const val NONE = 1

        const val ALL = 2

        const val DATA = 3

        const val RESOURCE = 4

        const val AUTOMATIC = 5

        fun convert(value: Int): DiskCacheStrategy = when (value) {
            ALL -> DiskCacheStrategy.ALL
            DATA -> DiskCacheStrategy.DATA
            RESOURCE -> DiskCacheStrategy.RESOURCE
            AUTOMATIC -> DiskCacheStrategy.AUTOMATIC
            NONE -> DiskCacheStrategy.NONE
            else -> DiskCacheStrategy.AUTOMATIC
        }
    }
}
