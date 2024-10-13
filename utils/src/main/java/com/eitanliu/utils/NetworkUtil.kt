package com.eitanliu.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission

object NetworkUtil {
    // 是否有网络连接
    val Context.isNetWorkConnected
        @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
        get() = isMobilConnected || isWifiConnected

    // Wifi 网络
    val Context.isWifiConnected
        @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
        get() = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false

    // 移动 网络
    val Context.isMobilConnected
        @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
        get() = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false

    val Context.isNetworkRoaming
        @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
        get() = telephonyManager.isNetworkRoaming

    val Context.is5GConnected
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            telephonyManager.dataNetworkType == TelephonyManager.NETWORK_TYPE_NR && isNetWorkConnected
        } else false

    val Context.is4GConnected
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        get() = telephonyManager.dataNetworkType == TelephonyManager.NETWORK_TYPE_LTE
                && isNetWorkConnected

    val Context.is3GConnected
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        get() = telephonyManager.dataNetworkType == TelephonyManager.NETWORK_TYPE_UMTS
                || telephonyManager.dataNetworkType == TelephonyManager.NETWORK_TYPE_HSDPA
                || telephonyManager.dataNetworkType == TelephonyManager.NETWORK_TYPE_EVDO_0
                && isNetWorkConnected

    val Context.is2GConnected
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        get() = telephonyManager.dataNetworkType == TelephonyManager.NETWORK_TYPE_GPRS
                || telephonyManager.dataNetworkType == TelephonyManager.NETWORK_TYPE_EDGE
                || telephonyManager.dataNetworkType == TelephonyManager.NETWORK_TYPE_CDMA
                && isNetWorkConnected

    val Context.networkCapabilities
        @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
        get() = connectivityManager.networkCapabilities

    val ConnectivityManager.networkCapabilities
        @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
        get() = getNetworkCapabilities(activeNetwork)

    val Context.connectivityManager get() = run { getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    val Context.telephonyManager
        get() = run { getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(
            network: Network, networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            onCapabilitiesChanged?.invoke(network, networkCapabilities)
        }
    }

    var onCapabilitiesChanged: ((
        network: Network, networkCapabilities: NetworkCapabilities
    ) -> Unit)? = null

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun Context.registerNetworkCallback(
        onCapabilitiesChanged: ((
            network: Network, networkCapabilities: NetworkCapabilities
        ) -> Unit)? = null,
    ) {
        NetworkUtil.onCapabilitiesChanged = onCapabilitiesChanged
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun Context.unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}