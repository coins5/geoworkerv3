package com.example.geoworkerv3.utils;

import android.content.Context;

import com.example.geoworkerv3.classes.GeoWorkerTransport;

// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;

public class Task implements Runnable {
    private String name;
    Context context;
    public Task(Context _context, String _name)
    {
        this.name = _name;
        this.context = _context;
    }

    public void run()
    {
        try
        {
            new GeoWorkerTransport(this.context, this.name);
            System.out.println(name+" complete");
        } catch (Exception e) {
            System.out.println(this.name + " - ERROR WHILE SPAWNING IN BACKGROUND");
            System.out.println(e.toString());
        }
    }
}
