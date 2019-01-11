package com.logs.daily.sikuyangu.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.logs.daily.sikuyangu.models.Event;

import java.util.List;

@Dao
public interface EventDAO {

    @Insert
    void insert(Event event);

    @Query("DELETE FROM event_table")
    void deleteAll();

    @Query("SELECT * from event_table ORDER BY word ASC")
    LiveData<List<Event>> getAllEevents();
}
