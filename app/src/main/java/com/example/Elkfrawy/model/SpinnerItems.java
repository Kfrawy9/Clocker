package com.example.Elkfrawy.model;

import java.util.Objects;

public class SpinnerItems {

    String priority;
    int color;

    public SpinnerItems(String priority, int color) {
        this.priority = priority;
        this.color = color;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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
        SpinnerItems that = (SpinnerItems) o;
        return color == that.color && Objects.equals(priority, that.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority, color);
    }
}
