package com.eitanliu.utils

import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * 中文编码格式
 */
@Suppress("HasPlatformType")
object CharsetInstances {
    val GB2312 by lazy { Charset.forName("GB2312") } // cn = 2 byte, en = 1 byte
    val GBK by lazy { Charset.forName("GBK") } // cn = 2byte, en = 1 byte
    val GB18030 by lazy { Charset.forName("GB18030") } // cn = 2 byte, en = 1 byte
    val UTF8 by lazy { Charset.forName("UTF-8") } // cn = 3 byte, en = 1 byte
    val UTF16 by lazy { Charset.forName("UTF-16") } // all = 4 byte
    val UTF32 by lazy { Charset.forName("UTF-32") } // all = 8 byte
    val Unicode by lazy { Charset.forName("Unicode") } // all = 4byte
}

@Suppress("HasPlatformType")
object MessageDigestInstances {

    val MD5 = MessageDigest.getInstance("MD5")
    val SHA1 = MessageDigest.getInstance("SHA-1")
    val SHA256 = MessageDigest.getInstance("SHA-256")
}