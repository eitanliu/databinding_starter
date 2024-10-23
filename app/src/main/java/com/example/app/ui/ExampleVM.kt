package com.example.app.ui

import android.content.Context
import android.content.res.Configuration
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.SavedStateHandle
import com.eitanliu.binding.event.bindingConsumer
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.extension.observe
import com.eitanliu.binding.state.multipleState
import com.eitanliu.binding.state.singleState
import com.eitanliu.starter.binding.BindingViewModel
import com.eitanliu.utils.Logcat
import com.eitanliu.utils.hideSoftKeyboard
import com.eitanliu.utils.imeInsets
import com.eitanliu.utils.isShowSoftwareKeyboard
import com.eitanliu.utils.showSoftKeyboard
import com.example.app.extension.bundle
import com.example.app.extension.not
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExampleVM @Inject constructor(
    stateHandle: SavedStateHandle,
) : BindingViewModel(stateHandle) {
    private val args = ExampleArgs(stateHandle.bundle)
    override val event = Event(this)
    override val state = State(this)

    val uiModelLight get() = (uiMode.value ?: 0) and Configuration.UI_MODE_NIGHT_MASK

    val isNoNight get() = uiModelLight == Configuration.UI_MODE_NIGHT_NO

    val nextModeNight
        get() = when (uiModelLight) {
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }

    init {
        title.value = "${args.arg1} ${args.arg2}"
        backVisible.value = true
        fitSystemBars.value = false
        uiMode.observe(this) {
            lightStatusBars.value = isNoNight
            lightNavigationBars.value = isNoNight
        }
    }

    inner class Event(
        viewModel: BindingViewModel
    ) : BindingViewModel.Event(viewModel) {

        val toggleSoftKeyboard = bindingConsumer<View> { view ->
            // view.toggleSoftKeyboard()
            view.rootView.apply {
                Logcat.msg("softKeyboard $isShowSoftwareKeyboard ${view.imeInsets}")
                if (isShowSoftwareKeyboard) {
                    hideSoftKeyboard()
                } else {
                    showSoftKeyboard()
                }
            }
        }

        val fitSystemBarsClick = bindingEvent {
            fitSystemBars.value = fitSystemBars.value.not()
            Logcat.msg("fitSystemBars ${fitSystemBars.value}")
        }

        val fitHorizontalClick = bindingEvent {
            fitHorizontal.value = fitHorizontal.value.not()
            Logcat.msg("fitSystemBars ${fitHorizontal.value}")
        }

        val lightStatusBar = bindingEvent {
            lightStatusBars.value = lightStatusBars.value.not(isNoNight)
            Logcat.msg("$isNoNight $uiModelLight ${lightStatusBars.value}")
        }

        val lightNavBar = bindingEvent {
            lightNavigationBars.value = lightNavigationBars.value.not(isNoNight)
            Logcat.msg("$isNoNight $uiModelLight ${lightNavigationBars.value}")
        }

        private val modeNightList = listOf(
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_YES,
            AppCompatDelegate.MODE_NIGHT_UNSPECIFIED,
            // AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            // AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        )
        private var modeNightIndex = 0
        val nightModeClick = bindingEvent {
            // lightStatusBars.value = null
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("zh"))
            modeNightIndex %= modeNightList.size
            // val model = modeNightList[modeNightIndex]
            val model = nextModeNight
            Logcat.msg("nightModel $isNoNight $uiModelLight $model")
            AppCompatDelegate.setDefaultNightMode(model)
            Context.UI_MODE_SERVICE
            modeNightIndex++
        }
    }

    inner class State(
        viewModel: BindingViewModel
    ) : BindingViewModel.State(viewModel) {
        val testState = singleState<Int>()
    }
}