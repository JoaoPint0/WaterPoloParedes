package com.endeavour.poloaquaticoparedes.api

import android.content.Context
import android.util.Log
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.model.*
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


private const val TAG = "WaterPoloService"

fun validateUser(
        service: WaterPoloService,
        login: LoginUser,
        onSuccess: (success: ApiResponse?) -> Unit,
        onError: (error: String) -> Unit) {

    service.validateAccount(login).enqueue(
            object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>?, t: Throwable) {
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<ApiResponse>?,
                        response: Response<ApiResponse>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {

                        onSuccess(response.body())
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

fun searchAllAthletes(
        service: WaterPoloService,
        onSuccess: (athletes: List<Athlete>) -> Unit,
        onError: (error: String) -> Unit) {

    service.searchAllAthletes().enqueue(
            object : Callback<List<Athlete>> {
                override fun onFailure(call: Call<List<Athlete>>?, t: Throwable) {
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<List<Athlete>>?,
                        response: Response<List<Athlete>>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val repos = response.body() ?: emptyList()
                        onSuccess(repos)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

fun searchAthleteByCardId(
        service: WaterPoloService,
        cardId: String,
        onSuccess: (athlete: ComplexAthlete?) -> Unit,
        onError: (error: String) -> Unit) {

    service.searchAthleteByCardId(cardId).enqueue(
            object : Callback<ComplexAthlete> {
                override fun onFailure(call: Call<ComplexAthlete>?, t: Throwable) {
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<ComplexAthlete>?,
                        response: Response<ComplexAthlete>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val athlete = response.body()
                        onSuccess(athlete)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

fun createAthlete(
        service: WaterPoloService,
        newAthlete: ComplexAthlete,
        onSuccess: (response: ApiResponse?) -> Unit,
        onError: (error: String) -> Unit) {

    service.createAthlete(newAthlete).enqueue(
            object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>?, t: Throwable) {
                    onError(t.message ?: "createAccount failure error")
                }

                override fun onResponse(
                        call: Call<ApiResponse>?,
                        response: Response<ApiResponse>
                ) {
                    if (response.isSuccessful) {
                        val success = response.body()
                        onSuccess(success)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

fun updateAthlete(
        service: WaterPoloService,
        updatedAthlete: ComplexAthlete,
        onSuccess: (response: ApiResponse?) -> Unit,
        onError: (error: String) -> Unit) {

    service.updateAthlete(updatedAthlete).enqueue(
            object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>?, t: Throwable) {
                    onError(t.message ?: "createAccount failure error")
                }

                override fun onResponse(
                        call: Call<ApiResponse>?,
                        response: Response<ApiResponse>
                ) {
                    if (response.isSuccessful) {
                        val success = response.body()
                        onSuccess(success)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

fun deleteAthlete(
        service: WaterPoloService,
        cardId: String,
        onSuccess: (response: ApiResponse?) -> Unit,
        onError: (error: String) -> Unit) {

    service.deleteAthlete(cardId).enqueue(
            object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>?, t: Throwable) {
                    onError(t.message ?: "createAccount failure error")
                }

                override fun onResponse(
                        call: Call<ApiResponse>?,
                        response: Response<ApiResponse>
                ) {
                    if (response.isSuccessful) {
                        val success = response.body()
                        onSuccess(success)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

fun searchAthletePaymentBySeason(
        service: WaterPoloService,
        cardId: Long,
        year: Int,
        onSuccess: (payments: List<Payment>) -> Unit,
        onError: (error: String) -> Unit) {

    service.searchAthletePaymentBySeason(cardId, year).enqueue(
        object : Callback<PaymentsResponse> {
            override fun onFailure(call: Call<PaymentsResponse>?, t: Throwable) {
                Log.d(TAG, "fail to get data")
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                    call: Call<PaymentsResponse>?,
                    response: Response<PaymentsResponse>
            ) {
                Log.d(TAG, "got a response $response")
                if (response.isSuccessful) {
                    val payments = response.body()?.monthPayments ?: emptyList()
                    onSuccess(payments)
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

fun createAthletePayments(
        service: WaterPoloService,
        payments: CreatePayment,
        onSuccess: (response: ApiResponse?) -> Unit,
        onError: (error: String) -> Unit) {

    service.createAthletePayments(payments).enqueue(
        object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>?, t: Throwable) {
                onError(t.message ?: "createAccount failure error")
            }

            override fun onResponse(
                    call: Call<ApiResponse>?,
                    response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    val success = response.body()
                    onSuccess(success)
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

fun searchAllEvents(
        service: WaterPoloService,
        onSuccess: (events: List<Event>) -> Unit,
        onError: (error: String) -> Unit) {

    service.searchAllEvents().enqueue(
            object : Callback<List<Event>> {
                override fun onFailure(call: Call<List<Event>>?, t: Throwable) {
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<List<Event>>?,
                        response: Response<List<Event>>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val events = response.body() ?: emptyList()
                        onSuccess(events)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

fun getEventById(
        service: WaterPoloService,
        id: Long,
        onSuccess: (event: Event?) -> Unit,
        onError: (error: String) -> Unit) {

    service.getEventById(id).enqueue(
            object : Callback<Event> {
                override fun onFailure(call: Call<Event>?, t: Throwable) {
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<Event>?,
                        response: Response<Event>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val event = response.body()
                        onSuccess(event)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

fun createEvent(
        service: WaterPoloService,
        newEvent: Event,
        onSuccess: (response: ApiResponse?) -> Unit,
        onError: (error: String) -> Unit) {

    service.createEvent(newEvent).enqueue(
            object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>?, t: Throwable) {
                    onError(t.message ?: "createAccount failure error")
                }

                override fun onResponse(
                        call: Call<ApiResponse>?,
                        response: Response<ApiResponse>
                ) {
                    if (response.isSuccessful) {
                        val success = response.body()
                        onSuccess(success)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

fun editEvent(
        service: WaterPoloService,
        updatedEvent: Event,
        onSuccess: (response: ApiResponse?) -> Unit,
        onError: (error: String) -> Unit) {

    service.editEvent( updatedEvent).enqueue(
            object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>?, t: Throwable) {
                    onError(t.message ?: "createAccount failure error")
                }

                override fun onResponse(
                        call: Call<ApiResponse>?,
                        response: Response<ApiResponse>
                ) {
                    if (response.isSuccessful) {
                        val success = response.body()
                        onSuccess(success)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}


fun deleteEvent(
        service: WaterPoloService,
        id: Long,
        onSuccess: (response: ApiResponse?) -> Unit,
        onError: (error: String) -> Unit) {

    service.deleteEvent(id).enqueue(
            object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>?, t: Throwable) {
                    onError(t.message ?: "createAccount failure error")
                }

                override fun onResponse(
                        call: Call<ApiResponse>?,
                        response: Response<ApiResponse>
                ) {
                    if (response.isSuccessful) {
                        val success = response.body()
                        onSuccess(success)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

fun searchAllTeams(
    service: WaterPoloService,
    onSuccess: (teams: List<Team>) -> Unit,
    onError: (error: String) -> Unit) {

    service.searchAllTeams().enqueue(
        object : Callback<List<Team>> {
            override fun onFailure(call: Call<List<Team>>?, t: Throwable) {
                Log.d(TAG, "fail to get data")
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<List<Team>>?,
                response: Response<List<Team>>
            ) {
                Log.d(TAG, "got a response $response")
                if (response.isSuccessful) {
                    val teams = response.body() ?: emptyList()
                    onSuccess(teams)
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

fun createTeam(
    service: WaterPoloService,
    newTeam: Team,
    onSuccess: (response: ApiResponse?) -> Unit,
    onError: (error: String) -> Unit) {

    service.createTeam(newTeam).enqueue(
        object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>?, t: Throwable) {
                onError(t.message ?: "createAccount failure error")
            }

            override fun onResponse(
                call: Call<ApiResponse>?,
                response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    val success = response.body()
                    onSuccess(success)
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

fun searchAllGames(
    service: WaterPoloService,
    onSuccess: (games: List<Game>) -> Unit,
    onError: (error: String) -> Unit) {

    service.searchAllGames().enqueue(
        object : Callback<List<Game>> {
            override fun onFailure(call: Call<List<Game>>?, t: Throwable) {
                Log.d(TAG, "fail to get data")
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<List<Game>>?,
                response: Response<List<Game>>
            ) {
                Log.d(TAG, "got a response $response")
                if (response.isSuccessful) {
                    val games = response.body() ?: emptyList()
                    onSuccess(games)
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

fun getGameById(
    service: WaterPoloService,
    id: Long,
    onSuccess: (game: Game?) -> Unit,
    onError: (error: String) -> Unit) {

    service.getGameById(id).enqueue(
        object : Callback<Game> {
            override fun onFailure(call: Call<Game>?, t: Throwable) {
                Log.d(TAG, "fail to get data")
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<Game>?,
                response: Response<Game>
            ) {
                Log.d(TAG, "got a response $response")
                if (response.isSuccessful) {
                    val game = response.body()
                    onSuccess(game)
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

fun createGame(
    service: WaterPoloService,
    newGame: Game,
    onSuccess: (response: ApiResponse?) -> Unit,
    onError: (error: String) -> Unit) {

    service.createGame(newGame).enqueue(
        object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>?, t: Throwable) {
                onError(t.message ?: "createAccount failure error")
            }

            override fun onResponse(
                call: Call<ApiResponse>?,
                response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    val success = response.body()
                    onSuccess(success)
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

interface WaterPoloService {

    @GET("account/all")
    fun searchAllAthletes(): Call<List<Athlete>>

    @POST("account/authenticate")
    fun validateAccount(@Body login: LoginUser): Call<ApiResponse>

    @GET("account/{cardId}")
    fun searchAthleteByCardId(@Path("cardId") id: String): Call<ComplexAthlete>

    @POST("account/create")
    fun createAthlete(@Body account: ComplexAthlete): Call<ApiResponse>

    @POST("account/update")
    fun updateAthlete(@Body account: ComplexAthlete): Call<ApiResponse>

    @POST("account/delete/{cardId}")
    fun deleteAthlete(@Path("cardId") id: String): Call<ApiResponse>

    @GET("account/payments/{cardId}/{year}")
    fun searchAthletePaymentBySeason(@Path("cardId") id: Long, @Path("year") year: Int): Call<PaymentsResponse>

    @POST("payments/create")
    fun createAthletePayments(@Body payments: CreatePayment): Call<ApiResponse>

    @GET("event/all")
    fun searchAllEvents(): Call<List<Event>>

    @GET("event/{id}")
    fun getEventById(@Path("id") id: Long): Call<Event>

    @POST("event/create")
    fun createEvent(@Body event: Event): Call<ApiResponse>

    @POST("event/update")
    fun editEvent(@Body event: Event): Call<ApiResponse>

    @POST("event/delete/{id}")
    fun deleteEvent(@Path("id") id: Long): Call<ApiResponse>

    @GET("team/all")
    fun searchAllTeams(): Call<List<Team>>

    @POST("team/create")
    fun createTeam(@Body team: Team): Call<ApiResponse>

    @GET("game/all")
    fun searchAllGames(): Call<List<Game>>

    @GET("game/{id}")
    fun getGameById(@Path("id") id: Long): Call<Game>

    @POST("team/create")
    fun createGame(@Body game: Game): Call<ApiResponse>

    companion object {
        private var BASE_URL = ""

        fun create(context: Context): WaterPoloService {

            BASE_URL = context.getString(R.string.water_polo_base_url)

            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()

            val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(WaterPoloService::class.java)
        }
    }
}