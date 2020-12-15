package com.movie.approom.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movie.approom.R
import com.movie.approom.databinding.SubscriberRowBinding
import com.movie.approom.db.Subscriber

class SubscriberRecyclerViewAdapter(private val clickListener: (Subscriber) -> String) :
    RecyclerView.Adapter<MyViewHolder>() {

    private val subscribersList = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: SubscriberRowBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.subscriber_row, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subscribersList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscribersList[position], clickListener)
    }

    fun setList(subscribers: List<Subscriber>) {
        subscribersList.clear()
        subscribersList.addAll(subscribers)
    }

}

class MyViewHolder(private val binding: SubscriberRowBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(subscriber: Subscriber, clickListener: (Subscriber) -> String) {
        binding.nameTextView.text = subscriber.name
        binding.emailTextView.text = subscriber.email
        binding.listItemView.setOnClickListener {
            val ss = clickListener(subscriber)
            Log.e("TAG","result is $ss.")
        }
    }
}

