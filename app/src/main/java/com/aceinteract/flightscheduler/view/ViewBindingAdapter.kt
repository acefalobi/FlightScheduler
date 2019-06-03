package com.aceinteract.flightscheduler.view

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Bind a boolean to show or hide the view based on it's value.
 */
@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean?) {
    view.visibility = if (isGone == true) {
        View.GONE
    } else {
        View.VISIBLE
    }
}
