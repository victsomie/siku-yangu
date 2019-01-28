package com.logs.daily.sikuyangu.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.logs.daily.sikuyangu.R;
import com.logs.daily.sikuyangu.models.Category;
import com.logs.daily.sikuyangu.viewmodels.CategoryVM;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NewEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = NewEventActivity.class.getName();
    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    public static final String EXTRA_CATEGORY = "category_selected";

    private CategoryVM categoryVM;

    // @BindView(R.id.spinner_categories)
    // Spinner spCategories;

    private Spinner spCategories;
    private EditText mEditWordView;
    private ProgressBar progressBar;

    private List<Category> mainCategoriesList;

    private int mCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        // ButterKnife.bind(this);

        // Initialize views
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        spCategories = (Spinner) findViewById(R.id.spinner_categories);
        mEditWordView = (EditText) findViewById(R.id.edit_word);

        categoryVM = ViewModelProviders.of(this).get(CategoryVM.class);
        mainCategoriesList = new ArrayList<>();

        // Spinner click listener
        spCategories.setOnItemSelectedListener(this);

         mCategoryId = 0;

        final Button button = findViewById(R.id.button_save_event);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.e(TAG, "LOG selected item below");
                Log.e(TAG, "The selected item is :==> "+ String.valueOf(spCategories.getSelectedItem()));
                //Log.e(TAG, "ConvertedAFTER: " + categoriesToAdd);

                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditWordView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = mEditWordView.getText().toString();
                    if(mCategoryId < 1){
                        Toast.makeText(NewEventActivity.this, "Category is required!", Toast.LENGTH_SHORT).show();
                    }
                    // int categoryId = mainCategoriesList.indexOf(spCategories.getSelectedItem().toString());


                    mCategoryId = getTheCategoryId((String) spCategories.getSelectedItem(), mainCategoriesList);

                    replyIntent.putExtra(EXTRA_REPLY, word);
                    replyIntent.putExtra(EXTRA_CATEGORY, String.valueOf(mCategoryId));
                    setResult(RESULT_OK, replyIntent);
                }
                finish();

            }
        });
    }


    // Method to return id of a category
    private Integer getTheCategoryId(String name, List<Category> theCategories){

        if(theCategories.isEmpty()){
            // Fall back if the list provided is empty
            return null;
        }

        Iterator<Category> iterator = theCategories.iterator();
        while (iterator.hasNext()) {
            Category category = iterator.next();
            if (category.getName().equals(name)) {
                Log.e(TAG, "Cagegory id is: " + String.valueOf(category.getId()));
                return category.getId();
            }
        }
        return null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String item = adapterView.getItemAtPosition(position).toString();
        // Log.e("** CATGORY SELECTED **", "THE CATEGORY SELECTED");
        // Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        // mCategoryId = getTheCategoryId(item, mainCategoriesList);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d(TAG, "herf");

    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();
        // initSpinner(); // Show spinner data

    }


    private void initData(){

        progressBar.setVisibility(View.VISIBLE);
        categoryVM.getCategoryNamesAsArray().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> categoryNames) {
                if (categoryNames != null) {

                    // LOAD DATA TO THE SPINNER
                    initSpinner(categoryNames);

                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        categoryVM.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                if (categories != null) {
                    Log.e(TAG, String.valueOf(categories.size()));
                    mainCategoriesList.addAll(categories);
                }
            }
        });

    }

    private void initSpinner(List<String> categoriesList){

        // String[] theList = categoriesToAdd.toArray(new String[10]);
        String []theList = {"test", "Tester", "Testing"};

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoriesList);
        spCategories.setAdapter(dataAdapter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategories.setPrompt("Select Category");
    }
}
