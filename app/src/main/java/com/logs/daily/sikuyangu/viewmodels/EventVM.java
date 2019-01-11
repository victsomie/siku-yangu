package com.logs.daily.sikuyangu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.logs.daily.sikuyangu.models.Event;
import com.logs.daily.sikuyangu.repos.EventRepository;

import java.util.List;

public class EventVM extends AndroidViewModel {
    private EventRepository mRepository;

    private LiveData<List<Event>> mAllEvents;

    public EventVM (Application application) {
        super(application);
        mRepository = new EventRepository(application);
        mAllEvents = mRepository.getmAllEvents();
    }

    public LiveData<List<Event>> getAllWords() { return mAllEvents; }

    public void insert(Event event) { mRepository.insert(event); }
}
