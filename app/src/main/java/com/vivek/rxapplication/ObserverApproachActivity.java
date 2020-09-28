package com.vivek.rxapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ObserverApproachActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tutorial android1 = new Tutorial("Hafiz 1", "........");
        android1.publish();
        Tutorial android2 = new Tutorial("Hafiz 2", "........");
        android2.publish();
        Tutorial android3 = new Tutorial("Hafiz 3", "........");
        android3.publish();

        // I have already three tutorials and later user subscribed for email
        User A = new User("A", "a@a.com");
        User B = new User("B", "b@a.com");
        User C = new User("C", "c@a.com");
        User D = new User("D", "d@a.com");

        // Now A,C and D click subscribe button

        Tutorial.REGISTER_FOR_SUBSCRIPTION.register(A);
        Tutorial.REGISTER_FOR_SUBSCRIPTION.register(C);
        Tutorial.REGISTER_FOR_SUBSCRIPTION.register(D);

        Tutorial android4 = new Tutorial("Hafiz 4", "........");
        android4.publish();
    }

    public static void sendEmail(User user) {
        Log.d("Observerapproach","Email send: " + user.getName());
    }

    public static class User implements Observer {

        private String name;
        private String email;

        public User() {
        }

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public void notifyMe() {
            sendEmail(this);
        }
    }

    private static class Tutorial implements Observable {

        private String authorName;
        private String post;

        private Tutorial() {
        }

        public static Tutorial REGISTER_FOR_SUBSCRIPTION = new Tutorial();

        public Tutorial(String authorName, String post) {
            this.authorName = authorName;
            this.post = post;
        }

        private static List<Observer> observers = new ArrayList<>();

        @Override
        public void register(Observer observer) {
            observers.add(observer);
        }

        @Override
        public void unregister(Observer observer) {
            observers.remove(observer);
        }

        @Override
        public void notifyAllAboutChange() {
            for (Observer observer : observers) {
                observer.notifyMe();
            }
        }

        public void publish() {
            notifyAllAboutChange();
        }

    }

    public interface Observable {

        void register(Observer observer);

        void unregister(Observer observer);

        // new tutorial published to tell all subscribed users
        void notifyAllAboutChange();

    }

    public interface Observer {

        // New tutorial published
        void notifyMe();
    }
}
