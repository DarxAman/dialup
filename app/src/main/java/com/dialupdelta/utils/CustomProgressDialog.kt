package com.dialupdelta.utils

import android.content.Context
import com.kaopiz.kprogresshud.KProgressHUD

class CustomProgressDialog(private val context: Context) {
    private var progressDialog: KProgressHUD? = null

    fun showSweetDialog() {
        progressDialog = KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.1f)
            .show()
    }
    fun dismissSweet() {
        progressDialog?.dismiss()
    }
}