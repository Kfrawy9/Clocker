package com.example.Elkfrawy.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.Elkfrawy.model.Event;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Event e);

    @Delete
    void delete(Event e);

    @Update
    void update(Event e);

    @Query("Select * from event")
    LiveData<List<Event>> getAllEvents();

    @Query("Select * from event")
    List<Event> getEvents();

    @Query("Select * from event where dateTime LIKE :date")
    LiveData<List<Event>> getEventsBy(String date);

    @Query("DELETE FROM event WHERE title = :title AND description = :desc")
    void deleteBy(String title, String desc);

}
