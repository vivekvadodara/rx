package com.vivek.rxapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PullImperativeActivity extends AppCompatActivity {

    private static ArrayList<String> data = new ArrayList<>();
    Timer timer = new Timer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pullExample();
    }

    private void pullExample() {
        currentDateTime();
        data.add("A");
        data.add("B");
        data.add("C");
        data.add("D");

        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(dataTimerTask, 0, 1000);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentDateTime();
        data.add("E");
        data.add("F");
    }

    private static final TimerTask dataTimerTask = new TimerTask() {
        private int lastCount = 0;

        @Override
        public void run() {
            currentDateTime();
            if (lastCount != data.size()) {
                iterateOnData(data);
                lastCount = data.size();
            } else {
                Log.d("Pull", "No change in data");
            }
        }
    };

    private static void iterateOnData(List data) {
        Iterator iterator = data.iterator();
        while (iterator.hasNext()) {
            Log.d("Pull", iterator.next().toString());
        }
    }

    private static void currentDateTime() {
        Log.d("Pull", new Date(System.currentTimeMillis()).toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;

    }
}
