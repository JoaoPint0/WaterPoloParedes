package com.endeavour.poloaquaticoparedes.ui.athletes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.endeavour.poloaquaticoparedes.model.*
import com.endeavour.poloaquaticoparedes.repository.WaterPoloRepository

class AthletesViewModel(private val repository: WaterPoloRepository) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()
    private val cardId = MutableLiveData<String>()

    private val athlete : LiveData<ComplexAthlete> = Transformations.switchMap(cardId) { it ->
        repository.searchAthleteByCardId(it)
    }

    val athletes: LiveData<List<Athlete>> = Transformations.switchMap(queryLiveData) { it ->
        repository.searchAllAthletes(it)
    }

    fun searchAthlete(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun createAthlete(athlete: ComplexAthlete): LiveData<Boolean> {

        return repository.createAthlete(athlete)
    }

    fun editAthlete(createComplexAthlete: ComplexAthlete) : LiveData<Boolean> {

        return repository.updateAthlete(createComplexAthlete)
    }

    fun deleteAthlete(cardId: String) : LiveData<Boolean> {

        return repository.deleteAthlete(cardId)
    }

    fun getAthlete(): LiveData<ComplexAthlete> {

       return athlete
    }

    fun updateAthlete(id: String){

        cardId.value = id
    }

    private var refresh = MutableLiveData<String>()

    fun refresh(){

        refresh.value = "refresh"
    }

    fun getAthleteSeasonPayments(cardId: Long, year: Int): LiveData<List<Payment>> {

        return Transformations.switchMap(refresh){
            repository.searchAthletePaymentBySeason(cardId, year)
        }
    }

    fun createMonthPayment(cardId: Long, season: Int, month: Int?) : LiveData<Boolean>  {

        val monthPayment = CreatePayment(cardId, season, if (month != null) listOf(Payment(month, 15.0)) else emptyList())

        return repository.createPayment(monthPayment)
    }

    fun createSeasonPayment(cardId: Long, season: Int, year:List<Payment>) : LiveData<Boolean> {

        val monthPayment = CreatePayment(cardId, season, year)

        return repository.createPayment(monthPayment)
    }

    fun validateUser(loginObject: LoginUser) : LiveData<Boolean> {

        return repository.validateUser(loginObject)
    }

}
