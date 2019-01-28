package com.logs.daily.sikuyangu.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.logs.daily.sikuyangu.models.Category;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Insert
    void insert(Category category);

    @Query("DELETE from category_table")
    void deleteAllCategories();

    @Query("SELECT * from category_table ORDER BY category_name asc")
    LiveData<List<Category>> getAllCategories();


    @Query("SELECT category_name from category_table")
    LiveData<List<String>> getCategoryNamesAsArray();
}
