package org.themoviedb.utils.ext

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.themoviedb.R

fun Activity.setWhiteStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // for set light status bar
        window.decorView.systemUiVisibility = flags
        window.statusBarColor = Color.WHITE
    }
}

fun Activity.setBlackStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = window.decorView.systemUiVisibility
        flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // for clear light status bar
        window.decorView.systemUiVisibility = flags
        window.statusBarColor = getColor(R.color.colorPrimaryDark)
    }
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T) -> Unit) {
    liveData.observe(this, Observer(body))
}

fun Activity?.toast(@StringRes resId: Int) =
    Toast.makeText(this, this?.getString(resId), Toast.LENGTH_SHORT).show()