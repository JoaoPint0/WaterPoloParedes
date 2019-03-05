package com.endeavour.poloaquaticoparedes.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.endeavour.poloaquaticoparedes.Injection.provideRepository
import com.endeavour.poloaquaticoparedes.MainActivity
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.model.LoginUser
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PoloFirebaseMessaging : FirebaseMessagingService() {

    private lateinit var sharedPref:  SharedPreferences

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage);
        Log.e("Firebase", "OnReceive ${remoteMessage?.notification?.title} ${remoteMessage?.notification?.body}" )

        if(remoteMessage?.notification?.body != null) {
            sharedPref = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)
            sharedPref.edit().putLong(getString(R.string.event), remoteMessage.data["eventId"]?.toLong() ?: 0).apply()

            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            createNotificationChannel()

            sendNotification(remoteMessage.notification?.title ?: "",
                    remoteMessage.notification?.body ?: "", pendingIntent)

        }
    }

    private fun sendNotification(title :String, body:String ,pendingIntent : PendingIntent) {


        val notification = NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.paredes_logo)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)


        val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification.build())
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.channel_id), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onNewToken(token: String) {

        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {

        Log.e("PoloFirebaseMessaging", token)

        sharedPref = getSharedPreferences(
                getString(R.string.shared_preferences), Context.MODE_PRIVATE)
        sharedPref.edit().putString(getString(R.string.firebase_token), token).apply()

        val user = LoginUser(sharedPref.getString(getString(R.string.email), "")?:"",sharedPref.getString(getString(R.string.card_id), "")?:"", token)

        if(isLoggedIn()) provideRepository(this).validateUser(user)


    }

    private fun isLoggedIn(): Boolean{

        return (sharedPref.getString(getString(R.string.card_id), "").isNullOrEmpty()
                || sharedPref.getString(getString(R.string.privileges), "").isNullOrEmpty())
    }
}