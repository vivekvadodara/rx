package com.vivek.rxapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RxApproachActivity extends AppCompatActivity {

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


        Tutorial.REGISTER_FOR_SUBSCRIPTION.subscribe(A);
        Tutorial.REGISTER_FOR_SUBSCRIPTION.subscribe(C);
        Tutorial.REGISTER_FOR_SUBSCRIPTION.subscribe(D);

        Tutorial android4 = new Tutorial("Hafiz 4", "........");
        android4.publish();

    }

    public static void sendEmail(User user) {
        Log.d("RxApproach", "Email send: " + user.getName());
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
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Object o) {
            sendEmail(this);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }

    public static class Tutorial {

        private String authorName;
        private String post;

        private Tutorial() {
        }

        public static Observable REGISTER_FOR_SUBSCRIPTION = Observable.just(new Tutorial());

        public Tutorial(String authorName, String post) {
            this.authorName = authorName;
            this.post = post;
        }

        public void publish() {
            REGISTER_FOR_SUBSCRIPTION.publish();
        }

    }


}
