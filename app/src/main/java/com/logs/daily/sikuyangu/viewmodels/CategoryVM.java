package com.logs.daily.sikuyangu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.logs.daily.sikuyangu.models.Category;
import com.logs.daily.sikuyangu.repos.CategoryRepository;

import java.util.List;

public class CategoryVM extends AndroidViewModel {

    private CategoryRepository mCategoryRepository;
    private LiveData<List<Category>> mAllCategories;

    public CategoryVM(@NonNull Application application) {
        super(application);
        mCategoryRepository = new CategoryRepository(application);
        mAllCategories = mCategoryRepository.getmAllCategories();
    }

    public void addNewCategory(Category category){
        mCategoryRepository.insertCategory(category);
    }

    public LiveData<List<Category>> getAllCategories(){
        return mCategoryRepository.getmAllCategories();
    }
}
