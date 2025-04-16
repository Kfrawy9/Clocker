package com.example.Elkfrawy.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "event")
public class Event {
    @PrimaryKey(autoGenerate = true)
    int id;
    String title;
    String description;
    String dateTime;
    boolean reminder;

    public Event(String title, String description, String date, boolean reminder) {
        this.title = title;
        this.description = description;
        this.dateTime = date;
        this.reminder = reminder;
    }

    public Event() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return reminder == event.reminder && title.equals(event.title) && Objects.equals(description, event.description) && Objects.equals(dateTime, event.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, dateTime, reminder);
    }
}
