package com.movie.approom.vm

import android.util.Log
import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.approom.Event
import com.movie.approom.db.Subscriber
import com.movie.approom.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(), Observable {

    private var isUpdateOrDelete = false
    private lateinit var updateOrDeleteSubscriber: Subscriber

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    val subscribers = repository.subscribers
    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()
    @Bindable
    val saveOrUpdateButton = MutableLiveData<String>()
    @Bindable
    val clearAllOrDeleteButton = MutableLiveData<String>()

    private val statusMessageDigest = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessageDigest


    init {
        saveOrUpdateButton.value = "save"
        clearAllOrDeleteButton.value = "clear all"
    }

    fun saveOrUpdate() {
        if (inputName.value == null) {
            statusMessageDigest.value = Event("请输入姓名！")
        } else if (inputEmail.value == null) {
            statusMessageDigest.value = Event("请输入邮箱！")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessageDigest.value = Event("邮箱格式有误！")
        } else {
            if (isUpdateOrDelete) {
                updateOrDeleteSubscriber.name = inputName.value!!
                updateOrDeleteSubscriber.email = inputEmail.value!!
                update(updateOrDeleteSubscriber)
            } else {
                val name: String = inputName.value!!
                val email: String = inputEmail.value!!
                insert(Subscriber(0, name, email))
                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(updateOrDeleteSubscriber)
        } else {
            clearAll()
        }
    }

    private fun insert(subscriber: Subscriber): Job = viewModelScope.launch {
        val id = repository.insert(subscriber)
        if (id > -1) {
            statusMessageDigest.value = Event("Subscriber insert Successfully $id")
        } else {
            statusMessageDigest.value = Event("Error Occurred")
        }

    }

    private fun update(subscriber: Subscriber): Job = viewModelScope.launch {
        val row = repository.update(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        saveOrUpdateButton.value = "save"
        clearAllOrDeleteButton.value = "clear all"
        statusMessageDigest.value = Event("row $row Subscriber update Successfully")
    }

    private fun delete(subscriber: Subscriber): Job = viewModelScope.launch {
        val row = repository.delete(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        saveOrUpdateButton.value = "save"
        clearAllOrDeleteButton.value = "clear all"
        statusMessageDigest.value = Event("row $row Subscriber delete Successfully")
    }

    private fun clearAll(): Job = viewModelScope.launch {
        val rows = repository.deleteAll()
        statusMessageDigest.value = Event("rows $rows Subscriber clearAll Successfully")
    }

    fun initUpdateOrDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        updateOrDeleteSubscriber = subscriber
        saveOrUpdateButton.value = "update"
        clearAllOrDeleteButton.value = "delete"
    }


}