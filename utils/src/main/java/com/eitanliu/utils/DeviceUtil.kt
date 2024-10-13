package com.eitanliu.utils

import java.io.File

object DeviceUtil {
    private const val DEF_NUMBER = 4

    /**
     * 获取CPU核心个数
     */
    val cpuCoresNumber = run {
        var cores = DEF_NUMBER
        try {
            cores = File("/sys/devices/system/cpu/")
                .listFiles { pathname ->
                    val path = pathname.name
                    if (path.startsWith("cpu")) {
                        for (i in 3 until path.length) {
                            if (path[i] < '0' || path[i] > '9') {
                                return@listFiles false
                            }
                        }
                        return@listFiles true
                    }
                    return@listFiles false
                }?.size ?: cores
            cores = if (cores < DEF_NUMBER) DEF_NUMBER else cores
        } catch (_: Exception) {
        }
        cores
    }
}