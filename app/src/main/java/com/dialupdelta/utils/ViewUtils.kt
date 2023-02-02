package com.dialupdelta.utils

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dialupdelta.R


fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun showToastMessage(context: Context, message:String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Suppress("DEPRECATION")
fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
    } else {
        cm?.run {
            cm.activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    result = true
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    result = true
                }
            }
        }
    }
    return result
}

fun isValidContextForGlide(context: Context?): Boolean {
    if (context == null) {
        return false
    }
    if (context is Activity) {
        if (context.isDestroyed || context.isFinishing) {
            return false
        }
    }
    return true
}

fun ImageView.setUserImage(context: Context, imageUrl: String?) {
    if (isValidContextForGlide(context)) {
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.new_user_place_holder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
        this.background =
            ContextCompat.getDrawable(context, R.drawable.rounded_imageview_background)
        this.clipToOutline = true
    }
}

@Suppress("DEPRECATION")
fun hideStatusBar(activity: Activity){
    val window = activity.window
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}