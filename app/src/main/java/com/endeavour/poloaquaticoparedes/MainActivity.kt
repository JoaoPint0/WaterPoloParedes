package com.endeavour.poloaquaticoparedes

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ShortcutManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.endeavour.poloaquaticoparedes.Injection.provideRepository
import com.endeavour.poloaquaticoparedes.model.LoginUser
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var navigator: NavController

    private var eventDestination = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        MobileAds.initialize(this, "ca-app-pub-9186906227587004~9493693287")

        NavigationUI.setupWithNavController(bottomNavigation, Navigation.findNavController(this, R.id.nav_host_fragment))

        eventDestination = intent.extras?.getString("destination") ?: ""

        sharedPref = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)

        navigator = Navigation.findNavController(this, R.id.nav_host_fragment)

        eventNewIntent()

        navigator.addOnDestinationChangedListener { controller, destination, _ ->

            if (sharedPref.getString(getString(R.string.privileges), "") != "public" &&
                (destination.id == R.id.mainFragment || destination.id == R.id.athletesFragment)) {

                bottomNavigation.visibility = View.VISIBLE
            } else {
                bottomNavigation.visibility = View.GONE
            }

            if (destination.id != R.id.loginFragment && sharedPref.getString(getString(R.string.privileges), "").isNullOrEmpty()) {
                bottomNavigation.visibility = View.GONE
                controller.navigate(R.id.loginFragment)

            } else if (eventDestination.isNotEmpty()) {

                when (eventDestination) {
                    "createEvent" -> {
                        navigateCreateEvent()
                    }
                    "createAthlete" -> {
                        navigateCreateAthlete()
                    }
                    "seeEvent" -> {
                        navigateEventDetails(1)
                    }
                }
                eventDestination = ""
            }
        }

        bottomNavigation.menu.findItem(R.id.loginFragment).setOnMenuItemClickListener {

            val logoutUser = LoginUser(
                sharedPref.getString(getString(R.string.card_id), "") ?: ""
                , sharedPref.getString(getString(R.string.card_id), "") ?: "",
                ""
            )

            provideRepository(this).validateUser(logoutUser)

            sharedPref.edit()?.putString(getString(R.string.card_id), "")?.apply()
            sharedPref.edit()?.putString(getString(R.string.privileges), "")?.apply()

            if (Build.VERSION.SDK_INT >= 25) removeShorcuts()

            false
        }
    }

    @TargetApi(25)
    private fun removeShorcuts() {
        val shortcutManager = getSystemService(ShortcutManager::class.java)
        shortcutManager!!.disableShortcuts(Arrays.asList("shortcut1"))
        shortcutManager.removeAllDynamicShortcuts()
    }

    private fun navigateCreateAthlete() {
        val bundle = Bundle()
        bundle.putString("cardId", "")
        navigator.navigate(R.id.createAthleteFragment, bundle)
    }

    private fun navigateCreateEvent() {
        val bundle = Bundle()
        bundle.putLong("id", 0L)
        navigator.navigate(R.id.createEventFragment, bundle)
    }

    private fun navigateEventDetails(eventId: Long) {
        val bundle = Bundle()
        bundle.putLong("id", eventId)
        navigator.navigate(R.id.eventDetailsFragment, bundle)
    }

    override fun onBackPressed() {

        if (sharedPref.getString(getString(R.string.privileges), "").isNullOrEmpty()) {
            finish()
        } else if(sharedPref.getString(getString(R.string.privileges), "") == "public" && navigator.currentDestination?.label == "main_fragment"){
            navigator.navigate(R.id.loginFragment)
            sharedPref.edit().putString(getString(R.string.privileges), "").apply()
        } else {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        setupBottomNavigationMenu()
        super.onStart()
    }

    fun setupBottomNavigationMenu() {

        val isAdmin = sharedPref.getString(getString(R.string.privileges), "") == "admin"
        bottomNavigation.menu.findItem(R.id.profileFragment).isVisible = !isAdmin
        bottomNavigation.menu.findItem(R.id.athletesFragment).isVisible = isAdmin
    }

    override fun onNewIntent(intent: Intent?) {

        val extras = intent?.extras
        if (extras != null) {
            when (extras.getString("destination")) {
                "createEvent" -> {
                    navigateCreateEvent()
                }
                "createAthlete" -> {
                    navigateCreateAthlete()
                }
            }
        }
        eventNewIntent()
    }

    private fun eventNewIntent() {

        val eventId = sharedPref.getLong(getString(R.string.event), 0)

        if (eventId > 0) {

            navigateEventDetails(eventId)
            sharedPref.edit().putLong(getString(R.string.event), 0).apply()
        }
    }

}
