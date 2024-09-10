package com.eitanliu.binding.adapter

import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.Insets
import androidx.core.util.putAll
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.eitanliu.binding.R
import com.eitanliu.binding.annotation.FitInsetsMode
import com.eitanliu.binding.event.UiEvent
import com.eitanliu.binding.event.UiEventConsumer
import com.eitanliu.binding.event.UiEventConsumerResult
import com.eitanliu.binding.event.UiEventResult
import com.eitanliu.binding.extension.cacheWindowInsetsCompat
import com.eitanliu.binding.extension.setBindingTag
import com.eitanliu.binding.extension.bindingTags
import com.eitanliu.binding.extension.getBindingTag
import com.eitanliu.binding.extension.viewWindowInsetsCompat
import com.eitanliu.binding.listener.CLICK_DELAY_DEFAULT
import com.eitanliu.binding.listener.DebounceClickListener
import com.eitanliu.binding.listener.DoubleTabTouchListener
import com.eitanliu.binding.listener.OnDoubleTapListener
import com.eitanliu.binding.listener.OnSingleTapConfirmedListener
import com.eitanliu.binding.model.FitSystemInsets

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

@JvmOverloads
fun View.fitWindowInsets(
    windowInsets: WindowInsetsCompat,
    fitSystemBar: Boolean? = null,
    fitStatusBars: Boolean? = null,
    fitNavigationBars: Boolean? = null,
    fitCaptionBar: Boolean? = null,
    fitDisplayCutout: Boolean? = null,
    fitHorizontal: Boolean? = null,
    fitMergeType: Boolean? = null,
    @InsetsType type: Int? = null,
    @FitInsetsMode mode: Int? = null,
) {
    val status = type != null || fitSystemBar == true
            || fitStatusBars == true || fitNavigationBars == true
            || fitCaptionBar == true || fitDisplayCutout == true
            || fitHorizontal == true

    var insetsType: Int? = null
    if (fitStatusBars == true) {
        insetsType = WindowInsetsCompat.Type.statusBars()
    }
    if (fitNavigationBars == true) {
        insetsType = WindowInsetsCompat.Type.navigationBars() or (insetsType ?: 0)
    }
    if (fitCaptionBar == true) {
        insetsType = WindowInsetsCompat.Type.captionBar() or (insetsType ?: 0)
    }
    if (fitDisplayCutout == true) {
        insetsType = WindowInsetsCompat.Type.displayCutout() or (insetsType ?: 0)
    }
    if (fitSystemBar == true) {
        insetsType = WindowInsetsCompat.Type.systemBars() or (insetsType ?: 0)
    }
    insetsType = when {
        type == null -> insetsType
        fitMergeType == true -> type or (insetsType ?: 0)
        else -> type
    }

    val cacheModel = getBindingTag(R.id.fitSystemInsets) as? FitSystemInsets
    val cacheStatus = cacheModel?.status
    val cacheMode = cacheModel?.mode
    val cacheType = cacheModel?.type
    val cacheInsets = cacheModel?.insets
    val fitMode = mode ?: cacheMode ?: FitInsetsMode.MARGIN
    val fitType = insetsType ?: cacheType ?: WindowInsetsCompat.Type.systemBars()
    val fitInsets = windowInsets.getInsets(fitType)
    val insets = if (fitHorizontal == true) {
        val horizontalInsets = windowInsets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        Insets.max(horizontalInsets, fitInsets)
    } else fitInsets
    // Log.e("fitSystemBar", "$status $cacheStatus, $fitMode $cacheMode, $insetsType $cacheType")
    // Log.e("fitSystemBarInsets", "apply: $insets, \ncache: $cacheInsets")
    if (cacheStatus == null && !status) return // 跳过未设置
    if (status == cacheStatus && cacheInsets == insets) return // 跳过未变化
    val model = FitSystemInsets(status, fitMode, fitType, insets)
    updateLayoutParams {
        if (fitMode == FitInsetsMode.MARGIN && this is ViewGroup.MarginLayoutParams) {
            var left = leftMargin
            var top = topMargin
            var right = rightMargin
            var bottom = bottomMargin
            if (cacheStatus == true && cacheInsets != null) {
                if (cacheMode == FitInsetsMode.MARGIN) {
                    left -= cacheInsets.left
                    top -= cacheInsets.top
                    right -= cacheInsets.right
                    bottom -= cacheInsets.bottom
                } else {
                    if (height > 0) height -= insets.top + insets.bottom
                    setPadding(
                        paddingLeft - cacheInsets.left, paddingTop - cacheInsets.top,
                        paddingRight - cacheInsets.right, paddingBottom - cacheInsets.top,
                    )
                }
            }
            if (status) {
                leftMargin = left + insets.left
                topMargin = top + insets.top
                rightMargin = right + insets.right
                bottomMargin = bottom + insets.bottom
            } else {
                leftMargin = left
                topMargin = top
                rightMargin = right
                bottomMargin = bottom
            }
        } else {
            var left = paddingLeft
            var top = paddingTop
            var right = paddingRight
            var bottom = paddingBottom
            if (cacheStatus == true && cacheInsets != null) {
                if (cacheMode == FitInsetsMode.MARGIN && this is ViewGroup.MarginLayoutParams) {
                    leftMargin -= cacheInsets.left
                    topMargin -= cacheInsets.top
                    rightMargin -= cacheInsets.right
                    bottomMargin -= cacheInsets.bottom
                } else {
                    left -= cacheInsets.left
                    top -= cacheInsets.top
                    right -= cacheInsets.right
                    bottom -= cacheInsets.bottom
                    if (height > 0) height -= insets.top + insets.bottom
                }
            }
            if (status) {
                if (height > 0) height += insets.top + insets.bottom
                setPadding(
                    left + insets.left, top + insets.top,
                    right + insets.right, bottom + insets.bottom
                )
            } else {
                setPadding(left, top, right, bottom)
            }
        }
    }
    setBindingTag(R.id.fitSystemInsets, model)
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
    listener.singleTapConfirmedListener = OnSingleTapConfirmedListener {
        onClick?.invoke()
        true
    }
    listener.doubleTapListener = OnDoubleTapListener {
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
    listener.singleTapConfirmedListener = OnSingleTapConfirmedListener {
        onClick?.invoke()
        true
    }
    listener.doubleTapListener = OnDoubleTapListener {
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
    listener.singleTapConfirmedListener = OnSingleTapConfirmedListener {
        onClick?.invoke(it)
        true
    }
    listener.doubleTapListener = OnDoubleTapListener {
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
    listener.singleTapConfirmedListener = OnSingleTapConfirmedListener {
        onClick?.invoke(it) ?: false
    }
    listener.doubleTapListener = OnDoubleTapListener {
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