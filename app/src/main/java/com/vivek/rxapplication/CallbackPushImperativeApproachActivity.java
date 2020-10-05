package com.vivek.rxapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CallbackPushImperativeApproachActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentDateTime();
        Data data = new Data(callback);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentDateTime();
        data.add("E");
        currentDateTime();
        data.add("F");
    }

    private void accpetIntergers(Integer...items) {

        int length = items.length;
    }

    private static class Data {

        private interface Callback {
            void dataChanged(List data);
        }

        private ArrayList<String> data = new ArrayList<>();
        private Callback callback;

        public Data(Callback callback) {
            this.callback = callback;
            data.add("A");
            data.add("B");
            data.add("C");
            data.add("D");
            iterateOnData(data);
        }

        void add(String object) {
            data.add(object);
            callback.dataChanged(data);
        }

    }

    private static Data.Callback callback = new Data.Callback() {
        @Override
        public void dataChanged(List data) {
            iterateOnData(data);
        }
    };

    private static void iterateOnData(List data) {
        Iterator iterator = data.iterator();
        while (iterator.hasNext()) {
            Log.d("Callback", iterator.next().toString());
        }
    }

    private static void currentDateTime() {
        Log.d("Callback", new Date(System.currentTimeMillis()).toString());
    }
}
