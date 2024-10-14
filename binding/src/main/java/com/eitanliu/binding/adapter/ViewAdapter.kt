package com.eitanliu.binding.adapter

import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.putAll
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.eitanliu.binding.event.UiEvent
import com.eitanliu.binding.event.UiEventConsumer
import com.eitanliu.binding.event.UiEventConsumerResult
import com.eitanliu.binding.event.UiEventResult
import com.eitanliu.binding.listener.CLICK_DELAY_DEFAULT
import com.eitanliu.binding.listener.DebounceClickListener
import com.eitanliu.binding.listener.DoubleTabTouchListener
import com.eitanliu.utils.WindowInsetsUtil.fitWindowInsets
import com.eitanliu.utils.annotation.FitInsetsMode
import com.eitanliu.utils.bindingTags
import com.eitanliu.utils.cacheWindowInsetsCompat
import com.eitanliu.utils.setBindingTag
import com.eitanliu.utils.viewWindowInsetsCompat

/**
 * 官方支持的绑定函数查看 [androidx.databinding.adapters]
 */
@BindingMethods(
    BindingMethod(type = View::class, attribute = "select", method = "setSelect"),
    BindingMethod(type = View::class, attribute = "enabled", method = "setEnabled"),
)
class ViewAdapter

/**
 * 设置View的背景颜色
 */
@BindingAdapter("android:backgroundColorRes")
fun View.setBackgroundColorRes(@ColorRes color: Int?) {
    if (color != null) {
        setBackgroundColor(ContextCompat.getColor(context, color))
    } else {
        background = null
    }
}

@BindingAdapter("android:background")
fun View.setBackgroundResource(@DrawableRes resId: Int?) {
    if (resId != null && resId != ResourcesCompat.ID_NULL) {
        setBackgroundResource(resId)
    } else {
        background = null
    }
}

/**
 * 设置系统状态栏和导航栏的高度
 * @param type [WindowInsetsCompat.Type]
 */
@BindingAdapter(
    "fitSystemBars", "fitStatusBars", "fitNavigationBars",
    "fitCaptionBar", "fitDisplayCutout", "fitHorizontal",
    "fitMergeType", "fitInsetsType", "fitInsetsMode",
    "applyWindowInsets", "onApplyWindowInsetsListener",
    requireAll = false
)
fun View.fitWindowInsets(
    fitSystemBars: Boolean? = null,
    fitStatusBars: Boolean? = null,
    fitNavigationBars: Boolean? = null,
    fitCaptionBar: Boolean? = null,
    fitDisplayCutout: Boolean? = null,
    fitHorizontal: Boolean? = null,
    fitMergeType: Boolean? = null,
    @InsetsType type: Int? = null,
    @FitInsetsMode mode: Int? = null,
    windowInsets: WindowInsetsCompat? = null,
    listener: OnApplyWindowInsetsListener? = null,
) {
    val status = type != null || fitSystemBars == true
            || fitStatusBars == true || fitNavigationBars == true
            || fitCaptionBar == true || fitDisplayCutout == true
            || fitHorizontal == true

    val hasWindowInsets = windowInsets != null || listener != null

    cacheWindowInsetsCompat?.also {
        fitWindowInsets(
            it, fitSystemBars, fitStatusBars, fitNavigationBars,
            fitCaptionBar, fitDisplayCutout, fitHorizontal,
            fitMergeType, type, mode,
        )
    }

    ViewCompat.setOnApplyWindowInsetsListener(this, { _: View, wInsets: WindowInsetsCompat ->
        viewWindowInsetsCompat = wInsets
        val insets = listener?.onApplyWindowInsets(
            this, windowInsets ?: wInsets
        ) ?: windowInsets ?: wInsets
        fitWindowInsets(
            insets, fitSystemBars, fitStatusBars, fitNavigationBars,
            fitCaptionBar, fitDisplayCutout, fitHorizontal,
            fitMergeType, type, mode,
        )
        cacheWindowInsetsCompat = insets
        insets
    }.takeIf { status || hasWindowInsets })
}

/**
 * view设置显示隐藏
 * @param visibility Boolean
 * @param hide 隐藏模式默认为[View.GONE]
 */
@BindingAdapter("android:visibility", "android:visibilityHide", requireAll = false)
fun View.setVisibility(visibility: Boolean, hide: Int? = null) {

    if (visibility) {
        setVisibility(View.VISIBLE)
    } else {
        setVisibility(hide ?: View.GONE)
    }
}

@BindingAdapter("bindingTags")
fun View.bindingTags(tags: Array<Pair<Int, Any?>>?) {
    tags?.forEach { (key, tag) ->
        setBindingTag(key, tag)
    }
}

@BindingAdapter("bindingTags")
fun View.bindingTags(tags: List<Pair<Int, Any?>>?) {
    tags?.forEach { (key, tag) ->
        setBindingTag(key, tag)
    }
}

@BindingAdapter("bindingTags")
fun View.bindingTags(tags: SparseArray<Any?>?) {
    if (tags != null) bindingTags.putAll(tags)
}

/**
 * View的onClick事件绑定
 *
 * @param delay 双击防抖延迟时间
 * @param debounce Boolean? 开启双击防止过快点击
 * @param onRepeatClick UiEvent 重复点击事件回调
 * @param onClick UiEvent 点击事件回调
 */
@BindingAdapter(
    "onClickDelay", "debounce",
    "onRepeatClickEvent", "onClickEvent",
    requireAll = false
)
fun View.onClickListener(
    delay: Long? = CLICK_DELAY_DEFAULT, debounce: Boolean? = true,
    onRepeatClick: UiEvent? = null, onClick: UiEvent?,
) {
    if ((delay != null && delay <= 0) || debounce == false) {
        setOnClickListener {
            onClick?.invoke()
        }
    } else {
        setOnClickListener(DebounceClickListener(delay ?: CLICK_DELAY_DEFAULT, {
            onRepeatClick?.invoke()
        }) {
            onClick?.invoke()
        })
    }
}

@BindingAdapter(
    "onClickDelay", "debounce",
    "onRepeatClickEvent", "onClickEvent",
    requireAll = false
)
fun View.onClickViewListener(
    delay: Long? = CLICK_DELAY_DEFAULT, debounce: Boolean? = true,
    onRepeatClick: UiEventConsumer<View>? = null,
    onClick: UiEventConsumer<View>?,
) {
    if ((delay != null && delay <= 0) || debounce == false) {
        setOnClickListener {
            onClick?.invoke(it)
        }
    } else {
        setOnClickListener(DebounceClickListener(delay ?: CLICK_DELAY_DEFAULT, {
            onRepeatClick?.invoke(it)
        }) {
            onClick?.invoke(it)
        })
    }
}

@BindingAdapter(
    "onClickEvent", "onDoubleClickEvent", requireAll = false
)
fun View.onDoubleClickListener(
    onClick: UiEvent? = null,
    onDoubleClick: UiEvent?
) {
    val listener = DoubleTabTouchListener(this)
    listener.onSingleTapConfirmedListener {
        onClick?.invoke()
        true
    }
    listener.onDoubleTapListener {
        onDoubleClick?.invoke()
    }
    setOnTouchListener(listener)
}

@BindingAdapter(
    "onClickEvent", "onDoubleClickEvent", requireAll = false
)
fun View.onDoubleClickMotionListener(
    onClick: UiEvent? = null,
    onDoubleClick: UiEventConsumer<MotionEvent>?
) {
    val listener = DoubleTabTouchListener(this)
    listener.onSingleTapConfirmedListener {
        onClick?.invoke()
        true
    }
    listener.onDoubleTapListener {
        onDoubleClick?.invoke(it)
    }
    setOnTouchListener(listener)
}

@BindingAdapter(
    "onClickEvent", "onDoubleClickEvent", requireAll = false
)
fun View.onDoubleClickMotion2Listener(
    onClick: UiEventConsumer<MotionEvent>? = null,
    onDoubleClick: UiEventConsumer<MotionEvent>?
) {
    val listener = DoubleTabTouchListener(this)
    listener.onSingleTapConfirmedListener {
        onClick?.invoke(it)
        true
    }
    listener.onDoubleTapListener {
        onDoubleClick?.invoke(it)
    }
    setOnTouchListener(listener)
}

/**
 * 支持单击和双击
 */
@BindingAdapter(
    "onClickEvent", "onDoubleClickEvent", requireAll = false
)
fun View.onDoubleClickMotionResultListener(
    onClick: UiEventConsumerResult<MotionEvent, Boolean>? = null,
    onDoubleClick: UiEventConsumer<MotionEvent>?
) {
    val listener = DoubleTabTouchListener(this)
    listener.onSingleTapConfirmedListener {
        onClick?.invoke(it) ?: false
    }
    listener.onDoubleTapListener {
        onDoubleClick?.invoke(it)
    }
    setOnTouchListener(listener)
}

/**
 * view的onLongClick事件绑定
 * @param event Function0<Unit>?
 */
@BindingAdapter("onLongClickEvent", requireAll = false)
fun View.onLongClickListener(event: UiEvent?) {
    setOnLongClickListener {
        event?.invoke()
        true
    }
}

@BindingAdapter("onLongClickEvent", requireAll = false)
fun View.onLongClickViewListener(event: UiEventConsumer<View>?) {
    setOnLongClickListener {
        event?.invoke(it)
        true
    }
}

@BindingAdapter("onLongClickEvent", requireAll = false)
fun View.onLongClickResultListener(event: UiEventResult<Boolean>) {
    setOnLongClickListener {
        event.invoke()
    }
}

@BindingAdapter("onLongClickEvent", requireAll = false)
fun View.onLongClickViewResultListener(event: UiEventConsumerResult<View, Boolean>) {
    setOnLongClickListener {
        event.invoke(it)
    }
}