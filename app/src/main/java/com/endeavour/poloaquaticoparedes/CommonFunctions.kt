package com.endeavour.poloaquaticoparedes

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.format.DateFormat
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.endeavour.poloaquaticoparedes.model.Game
import com.endeavour.poloaquaticoparedes.model.GameEventType
import com.endeavour.poloaquaticoparedes.model.Leagues
import com.google.android.material.textfield.TextInputEditText
import com.savvyapps.togglebuttonlayout.ToggleButtonLayout
import kotlinx.android.synthetic.main.create_event_fragment.*
import java.text.SimpleDateFormat
import java.util.*


fun formatDate(date: Date): String {

    val format = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())
    return format.format(date)
}

fun formatGameDate(date: Date): String {

    val format = SimpleDateFormat("EEE dd/MM", Locale.getDefault())
    return format.format(date)
}

fun formatTime(date: Date): String {

    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(date)
}

fun formatCounter(time: Long): String {

    val format = SimpleDateFormat("mm:ss", Locale.getDefault())
    return format.format(Date(time))
}

fun getGameScore(game: Game, showTime:Boolean): String {
    return if (game.date.before(Date())) {
        if (game.activity.isEmpty()) "0 : 0" else {

            val homeGoals = game.activity.count { it.type == GameEventType.GOAL && it.team == 0 }
            val awayGoals = game.activity.count { it.type == GameEventType.GOAL && it.team == 1 }
            " $homeGoals : $awayGoals "
        }
    } else {
        if(showTime) formatTime(game.date) else " vs "
    }
}

fun loadGlideImage(view: ImageView, url: String?) {

    Glide.with(view)
        .load(if (url.isNullOrBlank()) R.drawable.paredes_logo else url)
        .apply(RequestOptions().centerCrop())
        .into(view)
}

fun getEventTargets(young_toggle: ToggleButtonLayout, older_toggle: ToggleButtonLayout, sex_toggle: ToggleButtonLayout): List<Leagues> {

    val resources = young_toggle.resources
    val toggles = ArrayList<String>()
    val leagues = ArrayList<Leagues>()
    toggles.addAll(young_toggle.selectedToggles().map { it.title.toString() })
    toggles.addAll(older_toggle.selectedToggles().map { it.title.toString() })

    val isMale = sex_toggle.toggles[0].isSelected

    for (target in toggles) {
        leagues.add(
            when (target) {
                resources.getString(R.string.mini_polo) -> Leagues.MINIPOLO
                resources.getString(R.string.sub12) -> Leagues.SUB12
                resources.getString(R.string.infantil) -> if (isMale) Leagues.INFANTIL_MALE else Leagues.INFANTIL_FEMALE
                resources.getString(R.string.juvenil) -> if (isMale) Leagues.JUVENIL_MALE else Leagues.JUVENIL_FEMALE
                resources.getString(R.string.a18) -> if (isMale) Leagues.A18_MALE else Leagues.A18_FEMALE
                resources.getString(R.string.a20) -> if (isMale) Leagues.A20_MALE else Leagues.A20_FEMALE
                resources.getString(R.string.senior) -> if (isMale) Leagues.SENIOR_MALE else Leagues.SENIOR_FEMALE
                else -> return emptyList()
            }
        )
    }
    return leagues
}

fun getLeagueText(context: Context?, league: Leagues): String? {

    return when (league) {
        Leagues.MINIPOLO -> context?.getString(R.string.mini_polo)
        Leagues.SUB12 -> context?.getString(R.string.sub12)
        Leagues.INFANTIL_MALE -> "${context?.getString(R.string.infantil)} - ${context?.getString(R.string.male)}"
        Leagues.INFANTIL_FEMALE -> "${context?.getString(R.string.infantil)} - ${context?.getString(R.string.female)}"
        Leagues.JUVENIL_MALE -> "${context?.getString(R.string.juvenil)} - ${context?.getString(R.string.male)}"
        Leagues.JUVENIL_FEMALE -> "${context?.getString(R.string.juvenil)} - ${context?.getString(R.string.female)}"
        Leagues.A18_MALE -> "${context?.getString(R.string.a18)} - ${context?.getString(R.string.male)}"
        Leagues.A18_FEMALE -> "${context?.getString(R.string.a18)} - ${context?.getString(R.string.female)}"
        Leagues.A20_MALE -> "${context?.getString(R.string.a20)} - ${context?.getString(R.string.male)}"
        Leagues.A20_FEMALE -> "${context?.getString(R.string.a20)} - ${context?.getString(R.string.female)}"
        Leagues.SENIOR_MALE -> "${context?.getString(R.string.senior)} - ${context?.getString(R.string.male)}"
        Leagues.SENIOR_FEMALE -> "${context?.getString(R.string.senior)} - ${context?.getString(R.string.female)}"
    }
}

fun TextView.callIntentListener() {

    this.setOnClickListener {
        val call = Uri.parse("tel:${this.text}")
        val surf = Intent(Intent.ACTION_DIAL, call)
        startActivity(context, surf, null)
    }
}

fun TextView.emailIntentListener() {

    this.setOnClickListener {
        val surf = Intent(Intent.ACTION_SENDTO)
        surf.type = "text/plain";
        surf.data = Uri.parse("mailto: ${this.text}");
        startActivity(context, Intent.createChooser(surf, "Send Email"), null)
    }
}

fun String.toEditable(): Editable {

    return Editable.Factory.getInstance().newEditable(this)
}

fun TextInputEditText.isNotEmpty(): Boolean {

    if (this.text.toString().isEmpty()) {
        this.error = context.getString(R.string.required_field)
        this.requestFocus()
    }

    return this.text.toString().isNotEmpty()
}

fun ToggleButtonLayout.isSelected(field: String): Boolean {

    if (this.selectedToggles().isEmpty()) Toast.makeText(context, context.getString(R.string.toggle_selected, field), Toast.LENGTH_SHORT).show()
    this.requestFocus()

    return this.selectedToggles().isNotEmpty()
}

fun createDatePicker(fragmentManager: FragmentManager, view: TextView) {

    val args = Bundle()
    args.putString("date", view.text.toString())

    val datePicker = DatePickerFragment()
    datePicker.setView(view)
    datePicker.arguments = args
    datePicker.show(fragmentManager, "datePicker")
}

fun createTimePicker(fragmentManager: FragmentManager, view: TextView) {

    val args = Bundle()
    args.putString("time", view.text.toString())

    val timePicker = TimePickerFragment()
    timePicker.setView(view)
    timePicker.arguments = args
    timePicker.show(fragmentManager, "timePicker")
}

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    lateinit var parent: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var (day, month, year) =
            arguments?.getString("date")?.split("/")?.map { it.toInt() }
                ?: listOf(0, 0, 0)
        month -= 1

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(context, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user

        val date = Date(year - 1900, month, day)

        (parent as TextView).text = formatDate(date)
    }

    fun setView(view: View) {

        parent = view
    }
}

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    lateinit var parent: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Use the current time as the default values for the picker
        val (hour, minute) =
            arguments?.getString("time")?.split(":")?.map { it.toInt() }
                ?: listOf(0, 0, 0)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(
            activity, this, hour, minute,
            DateFormat.is24HourFormat(activity)
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        val data = Date()
        data.hours = hourOfDay
        data.minutes = minute

        (parent as TextView).text = formatTime(data)
    }

    fun setView(view: View) {

        parent = view
    }
}