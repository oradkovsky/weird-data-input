package com.ror.weirddatainput.extensions

import android.view.Gravity
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.toast(
    @StringRes message: Int,
    duration: Int = Toast.LENGTH_LONG,
    gravity: Int = Gravity.CENTER
) {
    this.toast(getString(message), duration, gravity)
}

fun Fragment.toast(
    message: String,
    duration: Int = Toast.LENGTH_LONG,
    gravity: Int = Gravity.CENTER
) {
    Toast.makeText(requireContext(), message, duration).apply {
        setGravity(gravity, 0, 0)
        show()
    }
}