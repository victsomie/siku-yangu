package com.logs.daily.sikuyangu;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.logs.daily.sikuyangu.Adapters.EventListAdapter;
import com.logs.daily.sikuyangu.models.Category;
import com.logs.daily.sikuyangu.models.Event;
import com.logs.daily.sikuyangu.ui.AddCategoryActivity;
import com.logs.daily.sikuyangu.ui.NewEventActivity;
import com.logs.daily.sikuyangu.viewmodels.CategoryVM;
import com.logs.daily.sikuyangu.viewmodels.EventVM;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_EVENT_ACTIVITY_REQUEST_CODE = 1;
    public static final int NEW_CATEGORY_ACTIVITY_REQUEST_CODE = 2;

    private EventVM mEventVM;
    private CategoryVM mCategoryVM;

    private List<Category> mCategoryList;

    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEventVM = ViewModelProviders.of(this).get(EventVM.class);
        mCategoryVM = ViewModelProviders.of(this).get(CategoryVM.class);

        mCategoryList = new ArrayList<>();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main);
        // Setting up the data
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final EventListAdapter adapter = new EventListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mEventVM.getAllWords().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                // Update the cashed copy of the words to the adapter
                adapter.setEvents(events);
                for (Event event:events) {
                    Log.e("** EVENT **", "CATEGORY: " +event.getCategoryId() + ", Event : " + event.getName());
                }
            }
        });

        mCategoryVM.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                for (Category category:categories) {
                    mCategoryList.add(category);
                    Log.e("** CATEGORIES **", category.getName() + ", CategoryID: " + category.getId());
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewEventActivity.class);
                startActivityForResult(intent, NEW_EVENT_ACTIVITY_REQUEST_CODE);
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_EVENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            // mCategoryList.size()
            // Math.random() * mCategoryList.size();
            if(mCategoryList.isEmpty()){
                Snackbar.make(coordinatorLayout, "A category is required..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }
            int theCategoryId = mCategoryList.get(new Random().nextInt( mCategoryList.size())).getId();

            Log.e("** THE CATEGORY ID **", String.valueOf(theCategoryId));

            Event event = new Event(data.getStringExtra(NewEventActivity.EXTRA_REPLY), theCategoryId);
            mEventVM.insert(event);
        } else if (requestCode == NEW_CATEGORY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Category category = new Category(data.getStringExtra(AddCategoryActivity.EXTRA_REPLY));
            mCategoryVM.addNewCategory(category);
        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add_category){
            // Initiate Adding new cateegory
            Intent intent = new Intent(MainActivity.this, AddCategoryActivity.class);
            startActivityForResult(intent, NEW_CATEGORY_ACTIVITY_REQUEST_CODE);
        }

        if (id == R.id.action_delete_all_events) {
            mEventVM.deleteAll();
            return true;
        }
        if (id == R.id.action_delete_all_categories) {
            mCategoryVM.deleteAllCategories();
            Log.e("** DELETE CATEGORIES **", "Initiate delet category");
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Feature coming soon...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
