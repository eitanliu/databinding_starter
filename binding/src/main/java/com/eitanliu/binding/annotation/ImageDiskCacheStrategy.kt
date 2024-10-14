package com.eitanliu.binding.annotation

import androidx.annotation.IntDef
import com.bumptech.glide.load.engine.DiskCacheStrategy

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
            NONE -> DiskCacheStrategy.NONE
            ALL -> DiskCacheStrategy.ALL
            DATA -> DiskCacheStrategy.DATA
            RESOURCE -> DiskCacheStrategy.RESOURCE
            AUTOMATIC -> DiskCacheStrategy.AUTOMATIC
            else -> DiskCacheStrategy.AUTOMATIC
        }
    }
}