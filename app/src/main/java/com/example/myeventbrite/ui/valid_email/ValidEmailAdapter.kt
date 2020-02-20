package com.example.myeventbrite.ui.valid_email

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myeventbrite.R
import kotlinx.android.synthetic.main.custom_email_item_layout.view.*

class ValidEmailAdapter(private var emails: List<String>) :
    RecyclerView.Adapter<ValidEmailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_email_item_layout,
                parent,
                false
            )
        )
    }
    fun setValue(emails: List<String>){
        this.emails = emails
        notifyDataSetChanged()
    }
    fun getValue(): List<String> {
        return emails
    }

    override fun getItemCount(): Int {
        return emails.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindEmail(emails[position],position)
    }

    class ViewHolder(private var view : View) : RecyclerView.ViewHolder(view) {

        fun bindEmail(email: String,position: Int){
            view.index.text = (position+1).toString()
            view.email.text = email
        }
    }
}