package com.endeavour.poloaquaticoparedes.ui.login

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.endeavour.poloaquaticoparedes.Injection
import com.endeavour.poloaquaticoparedes.MainActivity
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.isNotEmpty
import com.endeavour.poloaquaticoparedes.model.LoginUser
import com.endeavour.poloaquaticoparedes.ui.athletes.AthletesViewModel
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import java.util.*

class LoginFragment : Fragment() {

    private lateinit var viewModel: AthletesViewModel
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)

        view.login_btn.setOnClickListener {

            when{
                isAdmin() -> loginUser("admin")
                validLogin() -> verifyUser()
            }
        }

        view.public_login_btn.setOnClickListener {

            loginUser("public")
        }

        view.privacy_btn.setOnClickListener {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/polo-aquatico-paredes"))
            startActivity(browserIntent);
        }

        view.user_id_login_txt.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                when{
                    isAdmin() -> loginUser("admin")
                    validLogin() -> verifyUser()
                }
            }
            true
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, Injection.provideAthletesViewModelFactory(context!!))
                .get(AthletesViewModel::class.java)

        sharedPref = activity!!.getSharedPreferences(
                getString(R.string.shared_preferences), Context.MODE_PRIVATE)
    }

    private fun validLogin() : Boolean {

        return (user_email_login_txt.isNotEmpty() && user_id_login_txt.isNotEmpty())
    }

    private fun isAdmin() : Boolean{
        return (user_id_login_txt.text.toString() == "12345" && user_email_login_txt.text.toString().toLowerCase() == "jpinto")
    }

    private fun verifyUser (){

        viewModel.validateUser(loginObject()).observe(this, Observer {
            if (it){
                loginUser("athlete")
            }else{
                Toast.makeText(context, getString(R.string.invalid_login),Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loginUser(privileges: String){

        hideKeyboardFrom(context!!, view!!)

        if (Build.VERSION.SDK_INT >= 25 && isAdmin()) createDynamicShortCuts()

        sharedPref.edit().apply {
            putString(getString(R.string.email), user_email_login_txt.text.toString()).apply()
            putString(getString(R.string.card_id), user_id_login_txt.text.toString()).apply()
            putString(getString(R.string.privileges), privileges).apply()
        }

        (activity as MainActivity).setupBottomNavigationMenu()

        Navigation.findNavController(view!!).popBackStack()
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun loginObject(): LoginUser{

        return LoginUser(user_email_login_txt.text.toString(),
                user_id_login_txt.text.toString(),sharedPref.getString(getString(R.string.firebase_token),"") ?: FirebaseInstanceId.getInstance().token ?: "")
    }

    @TargetApi(25)
    private fun createDynamicShortCuts() {
        val shortcutManager = context?.getSystemService<ShortcutManager>(ShortcutManager::class.java)

        val intent = Intent(context, MainActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        intent.putExtra("destination", "createEvent")

        val resume = ShortcutInfo.Builder(context, "createEvent")
                .setShortLabel(getString(R.string.create_event))
                .setLongLabel(getString(R.string.create_event))
                .setIcon(Icon.createWithResource(context, R.drawable.paredes_logo))
                .setIntent(intent)
                .build()


        intent.putExtra("destination", "createAthlete")

        val top = ShortcutInfo.Builder(context, "createAthlete")
                .setShortLabel(getString(R.string.create_athlete))
                .setLongLabel(getString(R.string.create_athlete))
                .setIcon(Icon.createWithResource(context, R.drawable.paredes_logo))
                .setIntent(intent)
                .build()

        shortcutManager!!.dynamicShortcuts = Arrays.asList(resume,top)
    }

}
