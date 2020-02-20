package com.example.myeventbrite.ui.valid_email

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ValidEmailViewModel : ViewModel() {
    private val dbDoc :DocumentReference
    private val emails  = MutableLiveData<List<String>>()
    private val TAG = "FireStore"

    init {
        dbDoc = FirebaseFirestore.getInstance()
            .collection("emailEntity").document("emails")
        dbDoc.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
        }

            if (snapshot != null && snapshot.exists()) {
                if(snapshot.data?.get("emails")!=null) {
                emails.postValue ( snapshot.data?.get("emails") as List<String>)
                }else{
                    emails.postValue(ArrayList<String>())
                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    fun getAllEmail() : LiveData<List<String>>{
        return emails
    }

    fun insert(email: List<String>)=
        email.forEach{
            dbDoc.update("emails", FieldValue.arrayUnion(it))}

    fun delete(email :List<String>)=
        email.forEach{
            dbDoc.update("emails",FieldValue.arrayRemove(it))
        }
    }





