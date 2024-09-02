package com.example.base.mvvm

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.containsKey
import androidx.databinding.ViewDataBinding
import com.example.base.extension.refWeak
import com.example.base.mvvm.model.ActivityLaunchModel
import com.example.base.shared.wrapperPreferences
import java.lang.ref.Reference
import java.util.Random

open class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel<*>> : AppCompatActivity() {

    protected lateinit var binding: VB

    protected lateinit var viewModel: VM

    private val codeRandom = Random()
    private val requestCallbacks = SparseArray<Reference<ActivityResultCallback<ActivityResult>>>()

    override fun getSharedPreferences(name: String?, mode: Int) = wrapperPreferences(name, mode)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        registerUiStateChange()
    }

    private fun registerUiStateChange() {
        viewModel.activityState.startActivity.observe(this) {
            startActivity(it)
        }
        viewModel.activityState.finish.observe(this) {
            finish()
        }
        viewModel.activityState.onBackPressed.observe(this) {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * 跳转页面
     * @param model
     */
    fun startActivity(
        model: ActivityLaunchModel
    ) {
        val intent = Intent(this, model.clz)
        if (model.bundle != null) {
            intent.putExtras(model.bundle)
        }
        if (model.callback != null) {
            val requestCode = generateRandomNumber()
            requestCallbacks[requestCode] = model.callback.refWeak()
            @Suppress("DEPRECATION")
            startActivityForResult(intent, requestCode)
        } else {
            startActivity(intent)
        }
    }

    private fun generateRandomNumber(): Int {
        var number: Int = codeRandom.nextInt(2147418112) + 65536
        while (requestCallbacks.containsKey(number)) {
            number = codeRandom.nextInt(2147418112) + 65536
        }
        return number
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val callback = requestCallbacks.get(requestCode)
        callback?.get()?.onActivityResult(ActivityResult(resultCode, data))
    }

}