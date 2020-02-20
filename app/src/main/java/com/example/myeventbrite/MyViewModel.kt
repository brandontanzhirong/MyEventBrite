package com.example.myeventbrite


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.result.Result
import com.google.android.gms.common.util.ArrayUtils
import com.google.gson.Gson

data class Attendees(val attendees: ArrayList<Attendee>, val pagination: Page? = null)
data class Page(val continuation: String, val has_more_items : Boolean)
data class Attendee(val created:String, val profile:Profile, val answers:ArrayList<Answer>, val assigned_unit:Seating)
data class Profile(val cell_phone:String,val email:String,val name:String)
data class Answer(val answer:String, val question:String)
data class Seating(val pairs:ArrayList<ArrayList<String>>)

class EventBriteDataDeserializer : ResponseDeserializable<Attendees> {
    override fun deserialize(content: String) = Gson().fromJson(content, Attendees::class.java)
}

class MyViewModel : ViewModel() {
    val eventBriteURL = "https://www.eventbriteapi.com/v3/events/<Event_ID>/attendees/" //Your Event URL
    val token = "" //Token from your EventBrite Account
    var hasMoreItems = false


    private var attendees: MutableLiveData<Attendees>
    init {
        attendees = MutableLiveData()
        loadAttendees(null)

    }
    fun getAttendees(): MutableLiveData<Attendees> {
        return attendees
    }

    private fun loadAttendees(continuation: String? =null, listOfAttendees: ArrayList<Attendee> = ArrayList<Attendee>()){
        val listOfParam = mutableListOf("expand" to "assigned_unit","status" to "attending")
        var retrivedData : ArrayList<Attendee>?
        if (continuation!= null){
            listOfParam.add("continuation" to continuation)
        }
        Fuel.get(eventBriteURL, listOfParam)
            .authentication()
            .bearer(token)
            .responseObject(EventBriteDataDeserializer()){ _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        Log.i("ErrorMsg", result.error.toString())
                    }
                    is Result.Success -> {
                        val(data, _) = result
                        Log.i("Data",data.toString())
                        retrivedData = data?.attendees
                        hasMoreItems = (data?.pagination?.has_more_items)?:false
                        retrivedData?.forEach {
                            listOfAttendees.add(it)
                        }
                        if(hasMoreItems){
                            //fetch multiple times if there is more than a page to fetch
                            //as EventBrite send their data in pages (1 page = 50items)
                             loadAttendees(data?.pagination?.continuation,listOfAttendees)
                        }else{
                                listOfAttendees.reverse()
                                attendees.postValue(Attendees(listOfAttendees))
                        }
                    }
                }
            }

    }
}