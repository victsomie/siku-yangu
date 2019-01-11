package com.logs.daily.sikuyangu.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "event_table")
public class Event {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String name;

    public Event(String name) {this.name = name;}

    public String getName() {
        return name;
    }

}
