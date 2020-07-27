package com.wozpi.infinity.kit.framework.network

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat

class ConnectActivity(private val context: Context) {
    fun isHaveNetworkConnected():Boolean{
        var hasInternet = false
        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            val connectManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networks = connectManager.allNetworks

            if (networks.isNotEmpty()) {
                for (network in networks) {
                    val networkCapabilities = connectManager.getNetworkCapabilities(network)
                    if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
                        hasInternet = true
                    }
                }
            }
        }
        return hasInternet
    }
}