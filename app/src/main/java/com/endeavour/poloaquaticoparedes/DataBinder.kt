package com.endeavour.poloaquaticoparedes

import android.view.View
import androidx.databinding.BindingAdapter

object DataBinder {

    @BindingAdapter("goneUnless")
    @JvmStatic
    fun goneUnless(view: View, value: Boolean) {
        view.visibility = if (value) View.VISIBLE else View.GONE
    }
}