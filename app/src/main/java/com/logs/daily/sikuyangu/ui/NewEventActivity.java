package com.logs.daily.sikuyangu.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.logs.daily.sikuyangu.R;
import com.logs.daily.sikuyangu.models.Category;
import com.logs.daily.sikuyangu.viewmodels.CategoryVM;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    public static final String EXTRA_CATEGORY = "category_selected";

    private CategoryVM categoryVM;

    @BindView(R.id.spinner_categories)
    Spinner spCategories;

    private EditText mEditWordView;

    private List<Category> mainCategoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        ButterKnife.bind(this);

        mainCategoriesList = new ArrayList<>();
        categoryVM = ViewModelProviders.of(this).get(CategoryVM.class);

        mEditWordView = findViewById(R.id.edit_word);

        // Spinner click listener
        spCategories.getOnItemSelectedListener();

        final List<String> categoriesToAdd = new ArrayList<>();
        categoryVM.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                if (categories == null || categories.isEmpty()) {
                    return;
                }
                mainCategoriesList.addAll(categories); // Populate inversal list for this activity

                // Populate the spinner
                for (Category category: mainCategoriesList) {
                    categoriesToAdd.add(category.getName());
                }

            }
        });
        // Adapter Connecting the Categories list to the spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoriesToAdd);
        spCategories.setAdapter(dataAdapter);

        final Button button = findViewById(R.id.button_save_event);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditWordView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = mEditWordView.getText().toString();
                    // int categoryId = mainCategoriesList.indexOf(spCategories.getSelectedItem().toString());

                    replyIntent.putExtra(EXTRA_REPLY, word);
                    replyIntent.putExtra(EXTRA_CATEGORY, getTheCategoryId(spCategories.getSelectedItem().toString(), mainCategoriesList));
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // Method to return id of a category
    private Integer getTheCategoryId(String name, List<Category> theCategories){

        Iterator<Category> iterator = theCategories.iterator();
        while (iterator.hasNext()) {
            Category category = iterator.next();
            if (category.getName().equals(name)) {
                return category.getId();
            }
        }
        return null;
    }
}
