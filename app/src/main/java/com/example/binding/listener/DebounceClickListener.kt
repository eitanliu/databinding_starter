package com.example.binding.listener

import android.view.View

// 默认点击延迟
const val CLICK_DELAY_DEFAULT = 600L

// 关闭点击延迟
const val CLICK_DELAY_CLOSE = -1L


class DebounceClickListener @JvmOverloads constructor(
    private val delay: Long = CLICK_DELAY_DEFAULT,
    private val repeatClick: View.OnClickListener? = null,
    private val click: View.OnClickListener
) : View.OnClickListener {

    private var lastClickTime: Long = 0L

    override fun onClick(v: View) {
        val current = System.currentTimeMillis()
        if (current - lastClickTime >= delay) {
            lastClickTime = current
            click.onClick(v)
        } else {
            repeatClick?.onClick(v)
        }
    }
}