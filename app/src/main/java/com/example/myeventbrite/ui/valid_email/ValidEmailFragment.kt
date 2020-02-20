package com.example.myeventbrite.ui.valid_email

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myeventbrite.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.view.MenuItemCompat
import androidx.core.content.ContextCompat.getSystemService






const val ADD_EMAIL_REQUEST = 1
const val DELETE_EMAIL_REQUEST = 2

class ValidEmailFragment : Fragment(),SearchView.OnQueryTextListener{


    private lateinit var adapter: ValidEmailAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var email: List<String> = ArrayList()
    private lateinit var model : ValidEmailViewModel

    override fun onQueryTextSubmit(query: String?): Boolean {
        filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        filter(newText)
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val root = inflater.inflate(R.layout.fragment_vaild_email, container, false)

        val recyclerViewValidEmail = root.findViewById(R.id.recyclerViewValidEmail) as RecyclerView
        adapter = ValidEmailAdapter(email)
        recyclerViewValidEmail.adapter = adapter
        linearLayoutManager = LinearLayoutManager(this.context)
        recyclerViewValidEmail.layoutManager = linearLayoutManager
        model = ViewModelProviders.of(this).get(ValidEmailViewModel::class.java)
        model.getAllEmail().observe(this, Observer {
            adapter.setValue(it)
        })

        val addEmailButton = root.findViewById(R.id.addEmailButton) as FloatingActionButton

        addEmailButton.setOnClickListener {
            val intent = Intent(this.context,AddOrDeleteEmailActivity::class.java)
            intent.putExtra("requestCode", ADD_EMAIL_REQUEST);
            startActivityForResult(intent, ADD_EMAIL_REQUEST)
        }
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( resultCode == Activity.RESULT_OK){
            val emails = mutableListOf<String>()
            data?.getStringExtra("Emails")
                ?.split("\\s".toRegex())
                ?.forEach { emails
                    .add(it.trim())}
            Log.i("how many",emails.size.toString())
            val model = ViewModelProviders.of(this).get(ValidEmailViewModel::class.java)

            if(requestCode == ADD_EMAIL_REQUEST){
            model.insert(emails)


            }else if(requestCode == DELETE_EMAIL_REQUEST){
                model.delete(emails)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete_overflow_menu, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_email -> {
                val intent = Intent(this.context,AddOrDeleteEmailActivity::class.java)
                intent.putExtra("requestCode", DELETE_EMAIL_REQUEST);
                startActivityForResult(intent, DELETE_EMAIL_REQUEST)
                return true
            }R.id.count_valid_email ->{
            val count = adapter.getValue().size
            Toast.makeText(this.context,"The number of valid emails is $count",Toast.LENGTH_LONG).show()
            return true
        }else ->
        return super.onOptionsItemSelected(item)
        }
    }
    private fun filter(query: String?){
        val result = model.getAllEmail().value?.filter{
            if (query != null) {
                it.contains(query)
            }else false
        }
        if (result != null) {
            adapter.setValue(result)
            adapter.notifyDataSetChanged()
        }
    }
}