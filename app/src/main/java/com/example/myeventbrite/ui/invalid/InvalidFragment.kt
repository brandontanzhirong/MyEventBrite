package com.example.myeventbrite.ui.invalid

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myeventbrite.*
import com.example.myeventbrite.ui.valid_email.AddOrDeleteEmailActivity
import com.example.myeventbrite.ui.valid_email.DELETE_EMAIL_REQUEST
import com.example.myeventbrite.ui.valid_email.ValidEmailViewModel
import kotlinx.android.synthetic.main.fragment_attending.*
import kotlinx.android.synthetic.main.fragment_invalid.*

class InvalidFragment : Fragment() {
    private lateinit var adapter: MyRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var attendees: Attendees = Attendees(ArrayList())


    private lateinit var myViewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val root = inflater.inflate(R.layout.fragment_invalid, container, false)
        val recycleViewDuplicate = root.findViewById(R.id.recyclerViewInvalid) as RecyclerView
        val progressBarInvalid = root.findViewById(R.id.progressBarInvalid) as ProgressBar
        adapter = MyRecyclerViewAdapter(attendees)
        recycleViewDuplicate.adapter = adapter
        linearLayoutManager = LinearLayoutManager(this.context)
        recycleViewDuplicate.layoutManager = linearLayoutManager
        val myModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        val dbModel = ViewModelProviders.of(this).get(ValidEmailViewModel::class.java)
        //check if there is connection to the internet
        if(isNetworkConnected()){
            val noInternetInvalid = root.findViewById(R.id.noInternetInvalid) as TextView
            noInternetInvalid.visibility = View.GONE
        myModel.getAttendees().observe(this, Observer<Attendees> { attendee ->
            val allValidEmails = dbModel.getAllEmail().value
            progressBarInvalid.visibility = View.GONE
            //filter the invalid attendees by comparing the valid emails given
            //eg: xxxx@gmail.com not in the valid emails, the email will be kept
            adapter.setValue(Attendees(
                attendee.attendees
                    .filter { attendees ->  !(allValidEmails?.any{it.contains(attendees.profile.email)}!!)} as ArrayList<Attendee>

            ))

        })}else{
            progressBarInvalid.visibility = View.GONE
            Toast.makeText(this.context,"no intenet", Toast.LENGTH_SHORT).show()
        }

        return root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.send_all, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.send_all -> {
                val recipient : ArrayList<String> = ArrayList()
                adapter.getValue().attendees.forEach{
                    recipient.add(it.profile.email)
                }
                sendEmail(recipient.distinct() ,"Not Registered")
                return true
            }else ->
            return super.onOptionsItemSelected(item)
        }
    }
     private fun sendEmail(recipient: List<String>, subject: String) {
      val mIntent = Intent(Intent.ACTION_SEND)
        //To send an email you need to specify mailto: as URI using setData() method
        //and data type will be to text/plain using setType() method
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        //recipient is put as array because you may wanna send email to multiple emails
        // so enter comma(,) separated emails, it will be stored in array
        mIntent.putExtra(Intent.EXTRA_EMAIL, recipient.toTypedArray())
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(mIntent)
    }
    private fun isNetworkConnected(): Boolean {
        val cm = this.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }
}
