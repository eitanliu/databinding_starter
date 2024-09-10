package com.eitanliu.starter.binding.controller

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import com.eitanliu.binding.event.UiEvent
import com.eitanliu.binding.event.bindingEvent
import com.eitanliu.binding.state.MultipleUiState
import com.eitanliu.binding.state.invoke
import com.eitanliu.binding.state.lateSingleState
import com.eitanliu.starter.bundle.BundleDelegate
import com.eitanliu.starter.binding.model.ActivityLaunchModel

class ActivityController : IActivity {
    override val activityEvent = Event()
    override val activityState = State()

    override fun startActivity(
        clz: Class<*>, bundle: Bundle?, callback: ActivityResultCallback<ActivityResult>?
    ) {
        activityState.startActivity(ActivityLaunchModel(clz, bundle, callback))
    }

    override fun finish() {
        activityState.finish()
    }

    override fun onBackPressed() {
        activityState.onBackPressed()
    }

    inner class Event : IActivity.Event {

        override val onBackClick = bindingEvent {
            onBackPressed()
        }

        override val finishActivity = bindingEvent {
            finish()
        }
    }

    inner class State : IActivity.State {

        override val startActivity = lateSingleState<ActivityLaunchModel>()

        override val finish = lateSingleState<Unit>()

        override val onBackPressed = lateSingleState<Unit>()
    }
}

/**
 * Activity 操作
 */
interface IActivity {
    val activityEvent: Event
    val activityState: State

    fun startActivity(
        clz: Class<*>,
        bundle: Bundle? = null,
        callback: ActivityResultCallback<ActivityResult>? = null,
    )

    fun finish()

    fun onBackPressed()

    interface Event {
        val onBackClick: UiEvent

        val finishActivity: UiEvent
    }

    interface State {
        val startActivity: MultipleUiState<ActivityLaunchModel>

        val finish: MultipleUiState<Unit>

        val onBackPressed: MultipleUiState<Unit>
    }
}

inline fun <reified T> IActivity.startActivity(
    bundle: BundleDelegate? = null,
    callback: ActivityResultCallback<ActivityResult>? = null,
) = startActivity<T>(bundle?.bundle, callback)

inline fun <reified T> IActivity.startActivity(
    bundle: Bundle? = null,
    callback: ActivityResultCallback<ActivityResult>? = null,
) = startActivity(T::class.java, bundle, callback)