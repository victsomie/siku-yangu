package com.logs.daily.sikuyangu.repos;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.logs.daily.sikuyangu.DAO.EventDAO;
import com.logs.daily.sikuyangu.db.EventRoomDatabase;
import com.logs.daily.sikuyangu.models.Event;

import java.util.List;

public class EventRepository {
    private EventDAO mEventDao;
    private LiveData<List<Event>> mAllEvents;

    public EventRepository(Application application){
        EventRoomDatabase db = EventRoomDatabase.getDatabase(application);
        mEventDao = db.eventDAO();
        mAllEvents = mEventDao.getAllEevents();
    }

    public LiveData<List<Event>> getmAllEvents(){
        return mAllEvents;
    }

    public void insert (Event event) {
        new insertAsyncTask(mEventDao).execute(event);
    }

    private static class insertAsyncTask extends AsyncTask<Event, Void, Void>{
        private EventDAO mAsyncTaskDao;

        insertAsyncTask(EventDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
