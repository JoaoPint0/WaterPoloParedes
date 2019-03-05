package com.endeavour.poloaquaticoparedes.ui.event

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.endeavour.poloaquaticoparedes.model.ApiResponse
import com.endeavour.poloaquaticoparedes.model.Event
import com.endeavour.poloaquaticoparedes.repository.WaterPoloRepository

class EventViewModel(private val repository: WaterPoloRepository) : ViewModel() {


    var isGameEvent = ObservableBoolean()
    private val queryLiveData = MutableLiveData<String>()

    val events: LiveData<List<Event>> = Transformations.switchMap(queryLiveData) {
        repository.searchAllEvents(it)
    }

    fun searchEvent(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun getEventById(id: Long):LiveData<Event> {

        return repository.getEventById(id)
    }

    fun createEvent(event: Event): LiveData<ApiResponse> {

        return repository.createEvent(event)
    }

    fun editEvent(event: Event): LiveData<Boolean> {

        return repository.editEvent(event)
    }

    fun deleteEvent(id: Long): LiveData<Boolean> {

        return repository.deleteEvent(id)
    }
}
