package com.example.myeventbrite

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.custom_item_layout.view.*
import java.lang.StringBuilder

class MyRecyclerViewAdapter(private var attendees: Attendees) :
    RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_item_layout, parent, false))
    }
    fun setValue(attendees: Attendees){
        this.attendees = attendees
        notifyDataSetChanged()
    }
    fun getValue() : Attendees{
        return attendees
    }

    override fun getItemCount(): Int {
        return attendees.attendees.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindAttendee(attendees.attendees[position])
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bindAttendee(attendee: Attendee) {
            view.fullName.text = attendee.profile.name
            view.email.text = attendee.profile.email
            view.country.text = attendee.answers[1].answer
            view.timeStamp.text = attendee.created
            val table_num = attendee.assigned_unit.pairs[0][1]
            val seat_num = attendee.assigned_unit.pairs[1][1]
            val table = "Table " + table_num
            val seat = " Seat " + seat_num
            view.seating.text = StringBuilder(table).append(seat).toString()
        }

    }
}