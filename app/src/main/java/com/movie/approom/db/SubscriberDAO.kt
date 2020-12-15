package com.movie.approom.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {

    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber) : Long

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber) : Int

    @Update
    suspend fun  updateSubscriber(subscriber: Subscriber): Int

    @Query("DELETE FROM subscriber_data_table")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubscriber() : LiveData<List<Subscriber>>


}