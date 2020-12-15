package com.movie.approom

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.movie.approom.adapter.SubscriberRecyclerViewAdapter
import com.movie.approom.databinding.ActivityMainBinding
import com.movie.approom.db.Subscriber
import com.movie.approom.db.SubscriberDAO
import com.movie.approom.db.SubscriberDatabase
import com.movie.approom.db.SubscriberRepository
import com.movie.approom.vm.SubscriberViewModel
import com.movie.approom.vm.SubscriberViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel

    private lateinit var adapter: SubscriberRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao: SubscriberDAO = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        binding.mainViewModel = subscriberViewModel
        binding.lifecycleOwner = this
        initRecyclerView()

        subscriberViewModel.message.observe(this, Observer { 
            it.getContentIfNotHandled()?.let { str ->
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SubscriberRecyclerViewAdapter({ subscriber: Subscriber -> listItemClicked(subscriber) })
        binding.recyclerView.adapter = adapter
        displaySubscriberList()
    }

    private fun displaySubscriberList() {
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.i("TAG", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(subscriber: Subscriber): String {
        Log.e("TAG","Subscriber name is ${subscriber.name}")
        subscriberViewModel.initUpdateOrDelete(subscriber)
        return subscriber.email
    }
}
