package com.example.Elkfrawy.model;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;
@Fts4
@Entity(tableName = "task")
public class Task {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo()
    String title;


    String time;
    String priority;
    int color;


    boolean checked;

    public Task(String title, String time, String priority, int color) {
        this.title = title;
        this.time = time;
        this.priority = priority;
        this.color = color;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;

        boolean res = Objects.equals(title, task.title) && Objects.equals(time, task.time) && Objects.equals(priority, task.priority) && color == task.color;
        return res;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
