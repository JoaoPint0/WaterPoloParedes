package com.endeavour.poloaquaticoparedes.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.endeavour.poloaquaticoparedes.model.ApiResponse
import com.endeavour.poloaquaticoparedes.model.Game
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CloudFireStore {

    private val db = FirebaseFirestore.getInstance()
    private val TAG = "CloudFireStore"

    fun addGame(game: Game): LiveData<ApiResponse> {

        val response = MutableLiveData<ApiResponse>()

        db.collection("games")
            .add(game)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                response.value = ApiResponse("", true, documentReference.id)
            }.addOnCanceledListener {
                Log.d(TAG, "canceled listener")
            }.addOnCompleteListener {
                Log.d(TAG, "DocumentSnapshot complete $it")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                response.value = ApiResponse("", false, "0")
            }

        return response
    }

    fun getGames(): LiveData<List<Game>> {

        val games = MutableLiveData<List<Game>>()

        db.collection("games")
            .get()
            .addOnSuccessListener { result ->
                val gamesList = mutableListOf<Game>()
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val game = document.toObject(Game::class.java)
                    game.id = document.id
                    gamesList.add(game)
                }
                games.value = gamesList.sortedBy { it.date }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                games.value = emptyList()
            }
        return games
    }

    fun getGameById(id: String): LiveData<Game> {
        val game = MutableLiveData<Game>()

        db.collection("games")
            .document(id).addSnapshotListener { result, firebaseFirestoreException ->
                game.value = result!!.toObject(Game::class.java)
            }

        return game
    }

    fun updateGame(game: Game): LiveData<ApiResponse> {

        val response = MutableLiveData<ApiResponse>()

        db.collection("games")
            .document(game.id)
            .set(game)
            .addOnSuccessListener {
                response.value = ApiResponse("", true, game.id)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        return response

    }
}