package com.example.geoworkerv3;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import com.example.geoworkerv3.classes.GeoWorkerTransport;
import com.example.geoworkerv3.utils.Task;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Iniciando el transporte");
                // new GeoWorkerTransport(context,"Transporte 1");

                ArrayList<Task> tasks = new ArrayList<>();
                int TASKS_COUNT = 10;
                for (int i = 0; i < TASKS_COUNT; i++) {
                    tasks.add(new Task(context,"Transporte " + i));
                }
                /*
                Runnable r3 = new Task("task 3");
                */

                // creates a thread pool with MAX_T no. of
                // threads as the fixed pool size(Step 2)
                ExecutorService pool = Executors.newFixedThreadPool(TASKS_COUNT);

                // passes the Task objects to the pool to execute (Step 3)
                for (int i = 0; i < TASKS_COUNT; i++) {
                    pool.execute(tasks.get(i));
                }
                // pool.execute(r1);


                // pool shutdown ( Step 4)
                pool.shutdown();


                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
