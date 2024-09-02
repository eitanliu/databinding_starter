package com.example.base.mvvm.controller

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import com.example.base.mvvm.model.ActivityLaunchModel
import com.example.binding.event.UiEvent
import com.example.binding.event.bindingEvent
import com.example.binding.state.UiState
import com.example.binding.state.invoke
import com.example.binding.state.lateSingleState

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

    }

    inner class State : IActivity.State {

        override val startActivity: UiState<ActivityLaunchModel> = lateSingleState()

        override val finish: UiState<Unit> = lateSingleState()

        override val onBackPressed: UiState<Unit> = lateSingleState()
    }
}

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
    }

    interface State {
        val startActivity: UiState<ActivityLaunchModel>

        val finish: UiState<Unit>

        val onBackPressed: UiState<Unit>
    }
}

inline fun <reified T> IActivity.startActivity(
    bundle: Bundle? = null,
    callback: ActivityResultCallback<ActivityResult>? = null,
) = startActivity(T::class.java, bundle, callback)