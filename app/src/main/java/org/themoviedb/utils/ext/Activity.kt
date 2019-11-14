package org.themoviedb.utils.ext

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import android.view.WindowManager


@Suppress("DEPRECATION")
fun Context.detectNetworkHealth(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    } else {
        val ni = connectivityManager.activeNetworkInfo
        ni != null && ni.isConnected
    }
}

fun Activity.setTransparentStatusBar() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}

fun Activity.setWhiteStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
    }
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T) -> Unit) {
    liveData.observe(this, Observer(body))
}

fun Activity?.toast(@StringRes resId: Int) =
    Toast.makeText(this, this?.getString(resId), Toast.LENGTH_SHORT).show()