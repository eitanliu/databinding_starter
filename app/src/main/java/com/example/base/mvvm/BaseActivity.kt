package com.example.base.mvvm

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.base.shared.wrapperPreferences

open class BaseActivity: AppCompatActivity() {

    override fun getSharedPreferences(name: String?, mode: Int) = wrapperPreferences(name, mode)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
}