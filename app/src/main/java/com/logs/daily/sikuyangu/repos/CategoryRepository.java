package com.logs.daily.sikuyangu.repos;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.provider.CalendarContract;

import com.logs.daily.sikuyangu.DAO.CategoryDAO;
import com.logs.daily.sikuyangu.DAO.EventDAO;
import com.logs.daily.sikuyangu.db.EventRoomDatabase;
import com.logs.daily.sikuyangu.models.Category;

import java.util.List;

public class CategoryRepository {

    private CategoryDAO mCategoryDAO;
    LiveData<List<Category>> mAllCategories;

    public CategoryRepository(Application application){
        EventRoomDatabase db = EventRoomDatabase.getDatabase(application);
        mCategoryDAO = db.categoryDAO();
        mAllCategories = mCategoryDAO.getAllCategories();
    }

    public LiveData<List<Category>> getmAllCategories() {
        return mAllCategories;
    }

    public void insertCategory(Category category){
        // mCategoryDAO.insert(category);
        new insertCategoryAsyncTask(mCategoryDAO).execute(category);
    }

    private static class insertCategoryAsyncTask extends AsyncTask<Category, Void, Void>{

        private CategoryDAO mCategoryDAO;

        insertCategoryAsyncTask(CategoryDAO categoryDAO){
            mCategoryDAO = categoryDAO;
        }

        @Override
        protected Void doInBackground(final Category... category) {
            mCategoryDAO.insert(category[0]);
            return null;
        }
    }

    private static class deleteAllCategoriesAsyncTask extends AsyncTask<Category, Void, Void>{
        CategoryDAO mCategoryDAO;

        deleteAllCategoriesAsyncTask(CategoryDAO categoryDAO){
            mCategoryDAO = categoryDAO;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            mCategoryDAO.deleteAllCategories();
            return null;
        }
    }



}
