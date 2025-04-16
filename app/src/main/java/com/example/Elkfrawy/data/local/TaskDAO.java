package com.example.Elkfrawy.data.local;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.Elkfrawy.model.Task;

import java.util.List;

@Dao
public interface TaskDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);

    @Update()
    void update(Task task);

    @Delete()
    void delete(Task task);

    @Query("Select * from task")
    LiveData<List<Task>> getData();

}

