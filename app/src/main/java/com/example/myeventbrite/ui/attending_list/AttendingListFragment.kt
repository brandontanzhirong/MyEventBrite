package com.example.myeventbrite.ui.attending_list

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
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

class AttendingListFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var adapter: MyRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var attendees: Attendees = Attendees(ArrayList())
    private lateinit var model: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val root = inflater.inflate(R.layout.fragment_attending, container, false)
        val recyclerViewFull = root.findViewById(R.id.recyclerViewFull) as RecyclerView
        val progressBar = root.findViewById(R.id.progressBar) as ProgressBar
        adapter = MyRecyclerViewAdapter(attendees)
        recyclerViewFull.adapter = adapter
        linearLayoutManager = LinearLayoutManager(this.context)
        recyclerViewFull.layoutManager = linearLayoutManager
        //check if there is connection to the internet
        if (isNetworkConnected()) {
            val noInternetText = root.findViewById(R.id.noInternetText) as TextView
            noInternetText.visibility = View.GONE
            model = ViewModelProviders.of(this).get(MyViewModel::class.java)
            model.getAttendees().observe(this, Observer<Attendees> { attendee ->
                //pass the full list of attendees(attending) to the adapter
                progressBar.visibility = View.GONE
                attendees = attendee
                adapter.setValue(attendee)

            })
        } else {
            progressBar.visibility = View.GONE
            Toast.makeText(this.context, "no intenet", Toast.LENGTH_SHORT).show()
        }
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.sorting_by, menu)
        val searchView =
            menu.findItem(R.id.search_table).actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = "Search table number"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_by_seating -> {
                adapter.getValue().attendees
                    .sortWith(
                        compareBy(
                            { it.assigned_unit.pairs[0][1].toInt() },
                            { it.assigned_unit.pairs[1][1].toInt() })
                    )
                adapter.notifyDataSetChanged()
                return true
            }
            R.id.sort_by_country -> {
                adapter.getValue().attendees
                    .sortBy { it.answers[1].answer.toLowerCase() }
                adapter.notifyDataSetChanged()
                return true
            }R.id.count_attendees->{
            val count = attendees.attendees.size
            Toast.makeText(
                this.context
                , "The number of attendees is $count"
                , Toast.LENGTH_LONG
            ).show()
            return true
        }R.id.count_exchanged_students->{
            val count = attendees.attendees.count {
                it.answers[0].answer.toLowerCase().startsWith("nex")
                        ||it.answers[0].answer.toLowerCase().startsWith("ngx")
            }
            Toast.makeText(
                this.context
                , "The number of exchanged students is $count"
                , Toast.LENGTH_LONG
            ).show()
            return true
        }
            else ->
                return super.onOptionsItemSelected(item)
        }   }



    override fun onQueryTextSubmit(query: String?): Boolean {
        filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        filter(newText)
        return false
    }

    private fun isNetworkConnected(): Boolean {
        val cm =
            this.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }

    private fun filter(query: String?) {
        if (query != null) {
            adapter.setValue(Attendees(attendees.attendees
                .filter {
                    it.assigned_unit.pairs[0][1]
                        .contains(query)
                } as ArrayList<Attendee>
            )
            )
            adapter.notifyDataSetChanged()
        }else{
            adapter.setValue(attendees)
            adapter.notifyDataSetChanged()
        }
    }
}