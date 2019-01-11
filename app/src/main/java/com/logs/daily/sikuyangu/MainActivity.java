package com.logs.daily.sikuyangu;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.logs.daily.sikuyangu.Adapters.EventListAdapter;
import com.logs.daily.sikuyangu.models.Event;
import com.logs.daily.sikuyangu.ui.NewEventActivity;
import com.logs.daily.sikuyangu.viewmodels.EventVM;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_EVENT_ACTIVITY_REQUEST_CODE = 1;

    private EventVM mEventVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEventVM = ViewModelProviders.of(this).get(EventVM.class);


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
            Event event = new Event(data.getStringExtra(NewEventActivity.EXTRA_REPLY));
            mEventVM.insert(event);
        } else {
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mEventVM.
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
