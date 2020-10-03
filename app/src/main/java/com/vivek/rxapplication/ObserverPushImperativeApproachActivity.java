package com.vivek.rxapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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


        Button button = new Button(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TOdo
            }
        });

        //button.setOnClickListener{view -> view.animation}


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

        //reactive programming needs a continuous flow or a stream..but why?

        // stream -> start point  <->> end point

        //server API request

        //DATA server response is the starting point of your stream/flow
        //    |
        //    |
        //    |
        //what will be end point? -> UI, Database

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
        Log.d("TAG", giveMeBack(10)+"");
        Log.d("TAG", giveMeBack(10L)+"");
        Log.d("TAG", giveMeBack(10.1f)+"");

        String s = giveMeBackTypeInterface(10.1f);

        Log.d("TAG", (String) giveMeBackTypeInterface(10.1f));
//        Log.d("TAG", giveMeBackTypeInterface("skjh"));
//        Log.d("TAG", giveMeBackTypeInterface(new PollingActivity.User()));
    }


    public static int  giveMeBack(int a){
        return a;
    }

    public static long  giveMeBack(long a){
        return a;
    }

    public static Float  giveMeBack(Float a){
        return a;
    }

    public static<I, R> R  giveMeBackTypeInterface(I a){
        return (R) a;
    }
}
