package com.endeavour.poloaquaticoparedes

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.endeavour.poloaquaticoparedes.model.Game
import com.endeavour.poloaquaticoparedes.model.Leagues
import java.util.*


object DataBinder {

    @BindingAdapter("goneUnless")
    @JvmStatic
    fun goneUnless(view: View, value: Boolean) {
        view.visibility = if (value) View.VISIBLE else View.GONE
    }

    @BindingAdapter("time")
    @JvmStatic
    fun time(view: TextView, value: Date?) {

        if (value == null) return

        view.text = formatTime(value)
    }

    @BindingAdapter("date")
    @JvmStatic
    fun date(view: TextView, value: Date?) {

        if (value == null) return

        view.text = formatDate(value)
    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {

        loadGlideImage(view, url)
    }
}