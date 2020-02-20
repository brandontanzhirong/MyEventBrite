package com.example.myeventbrite.ui.valid_email

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myeventbrite.R
import kotlinx.android.synthetic.main.add_email.*

class AddOrDeleteEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var toastMessage = "Emails have been saved"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_email)
        val listOfValidEmails = findViewById(R.id.listOfValidEmails) as EditText
        if(intent.getIntExtra("requestCode",0) == DELETE_EMAIL_REQUEST){
            toastMessage = "Emails have been deleted"
        }
        doneButton.setOnClickListener{
            //pass the result back when Done button is clicked
        setResult(Activity.RESULT_OK,
            Intent().putExtra("Emails",listOfValidEmails.text.toString()))
            Toast.makeText(this,toastMessage, Toast.LENGTH_SHORT).show()
        finish()
    }}

}