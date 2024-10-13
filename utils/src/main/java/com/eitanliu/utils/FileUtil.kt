package com.eitanliu.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID

@Suppress("NO_TAIL_CALLS_FOUND", "NON_TAIL_RECURSIVE_CALL")
object FileUtil {
    tailrec fun File.forEachFolder(action: ((total: Long, file: File) -> Unit)?): Long {
        var size = 0L
        val file = this
        if (file.exists()) {
            if (file.isDirectory) {
                file.listFiles()?.forEach {
                    size += it.forEachFolder { total, file ->
                        val subtotal = size + total
                        action?.invoke(subtotal, file)
                    }
                }
            } else {
                size += file.length()
                action?.invoke(size, file)
            }
        }
        return size
    }

    tailrec fun File.deleteFolder(delSelf: Boolean = false) {
        val file = this
        if (file.exists()) {
            file.listFiles()?.forEach {
                if (it.isDirectory) {
                    it.deleteFolder(true)
                } else {
                    it.delete()
                }
            }
            if (delSelf) {
                file.delete()
            }
        }
    }

    fun getFileFormatSize(size: Double): String {

        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return size.toString() + "B"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(kiloByte.toString())
            return result1.setScale(2, RoundingMode.HALF_UP).toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, RoundingMode.HALF_UP).toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, RoundingMode.HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, RoundingMode.HALF_UP).toPlainString() + "TB"
    }

    tailrec fun Context.randomCacheFile(
        prefix: String = "tamp_", suffix: String = "",
        time: Boolean = true, uuid: Boolean = true,
        external: Boolean = false,
    ): File = run {
        val dir = if (external) externalCacheDir else cacheDir
        File(dir, randomName(prefix, suffix, time, uuid)).let { file ->
            if (file.exists()) randomCacheFile(prefix, suffix, time, uuid, external)
            else file
        }
    }

    fun randomName(
        prefix: String = "tamp_", suffix: String = "",
        time: Boolean = true, uuid: Boolean = true,
    ) = run {
        val timeMillis = System.currentTimeMillis().toString().takeIf { time } ?: ""
        val uuidSequence = UUID.randomUUID().toString().run {
            subSequence(length / 2, length)
        }.takeIf { uuid } ?: ""
        "${prefix}${timeMillis}${uuidSequence}${suffix}"
    }

    val hasSdcard by lazy {
        Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
    }

    val sdcardPath by lazy { Environment.getExternalStorageDirectory() }
}