package com.vivek.rxapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ObserverPushImperativeApproachActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentDateTime();
        Data data = new Data();
        data.subscribe(observer);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentDateTime();
        data.add("E");
        currentDateTime();
        data.add("F");

        data.unSubscribe(observer);
    }

    private interface Observable {
        void subscribe(Observer observer);

        void unSubscribe(Observer observer);

        void notifyToEveryOne();
    }

    private interface Observer {
        void heyDataIsChanged(List data);
    }

    private static class Data implements Observable {

        private List<Observer> observers = new ArrayList<>();

        @Override
        public void subscribe(Observer observer) {
            observers.add(observer);
        }

        @Override
        public void unSubscribe(Observer observer) {
            observers.remove(observer);
        }

        @Override
        public void notifyToEveryOne() {
            for (Observer observer : observers) {
                observer.heyDataIsChanged(data);
            }
        }

        private ArrayList<String> data = new ArrayList<>();

        public Data() {
            data.add("A");
            data.add("B");
            data.add("C");
            data.add("D");
            iterateOnData(data);
        }

        void add(String object) {
            data.add(object);
            notifyToEveryOne();
        }

    }

    private static Observer observer = new Observer() {
        @Override
        public void heyDataIsChanged(List data) {
            iterateOnData(data);
        }
    };


    private static void iterateOnData(List data) {
        Iterator iterator = data.iterator();
        while (iterator.hasNext()) {
            Log.d("ObserverImperative", iterator.next().toString());
        }
    }

    private static void currentDateTime() {
        Log.d("ObserverImperative", new Date(System.currentTimeMillis()).toString());
    }
}
