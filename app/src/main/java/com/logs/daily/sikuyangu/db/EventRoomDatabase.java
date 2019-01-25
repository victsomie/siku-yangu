package com.logs.daily.sikuyangu.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.logs.daily.sikuyangu.DAO.CategoryDAO;
import com.logs.daily.sikuyangu.DAO.EventDAO;
import com.logs.daily.sikuyangu.models.Category;
import com.logs.daily.sikuyangu.models.Event;

@Database(entities = {Event.class, Category.class}, version = 1)
public abstract class EventRoomDatabase extends RoomDatabase {

    public abstract EventDAO eventDAO();
    public abstract CategoryDAO categoryDAO();

    private static volatile EventRoomDatabase INSTANCE;

    public static EventRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (EventRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EventRoomDatabase.class, "event_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>{

        private final EventDAO mDao;

        PopulateDbAsync(EventRoomDatabase db){
            mDao = db.eventDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDao.deleteAll();
            Event event1 = new Event("Bible Study");
            mDao.insert(event1);
            Event event2 = new Event("Friday Service");
            mDao.insert(event2);

            return null;
        }
    }

}
