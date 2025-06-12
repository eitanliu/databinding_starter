package com.example.app.ui

import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.eitanliu.utils.Logcat
import kotlin.math.roundToInt

class TranslateDeferringInsetsAnimationCallback(
    private val view: View,
    val persistentInsetTypes: Int,
    val deferredInsetTypes: Int,
    dispatchMode: Int = DISPATCH_MODE_STOP
) : WindowInsetsAnimationCompat.Callback(dispatchMode) {
    init {
        require(persistentInsetTypes and deferredInsetTypes == 0) {
            "persistentInsetTypes and deferredInsetTypes can not contain any of " +
                    " same WindowInsetsCompat.Type values"
        }
    }

    override fun onProgress(
        insets: WindowInsetsCompat,
        runningAnimations: List<WindowInsetsAnimationCompat>
    ): WindowInsetsCompat {
        // onProgress() is called when any of the running animations progress...

        // First we get the insets which are potentially deferred
        val typesInset = insets.getInsets(deferredInsetTypes)
        // Then we get the persistent inset types which are applied as padding during layout
        val otherInset = insets.getInsets(persistentInsetTypes)

        // Now that we subtract the two insets, to calculate the difference. We also coerce
        // the insets to be >= 0, to make sure we don't use negative insets.
        val diff = Insets.subtract(typesInset, otherInset).let {
            Insets.max(it, Insets.NONE)
        }

        // Logcat.msg("$typesInset, $otherInset")
        // The resulting `diff` insets contain the values for us to apply as a translation
        // to the view
        val translationX = (diff.left - diff.right).toFloat()
        val translationY = (diff.top - diff.bottom).toFloat()
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            bottomMargin = -translationY.roundToInt()
        }
        Logcat.msg("$translationX, $translationY")
        // view.setPadding(0, 0, translationX.roundToInt(), (-translationY).roundToInt())
        // view.translationX = translationX
        // view.translationY = translationY

        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimationCompat) {
        // Once the animation has ended, reset the translation values
        // view.translationX = 0f
        // view.translationY = 0f
    }
}