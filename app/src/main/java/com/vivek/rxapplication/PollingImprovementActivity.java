package com.vivek.rxapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PollingImprovementActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        polling();

        Tutorial android1 = new Tutorial("Hafiz 1", "........");
        Tutorial android2 = new Tutorial("Hafiz 2", "........");
        Tutorial android3 = new Tutorial("Hafiz 3", "........");

        Tutorial.publish(android1);
        Tutorial.publish(android2);
        Tutorial.publish(android3);

        // I have already three tutorials and later user subscribed for email

        User A = new User("A", "a@a.com");
        User B = new User("B", "b@a.com");
        User C = new User("C", "c@a.com");
        User D = new User("D", "d@a.com");

        // Now A,C and D click subscribe button

        Tutorial.addSubscribedUser(A);
        Tutorial.addSubscribedUser(C);
        Tutorial.addSubscribedUser(D);

        Tutorial android4 = new Tutorial("Hafiz 4", "........");
        Tutorial.publish(android4);
    }

    private static void polling() {

        Polling polling = new Polling();
        Timer timer = new Timer();
        timer.schedule(polling, 0, 1000);

    }


    public static class Polling extends TimerTask {

        @Override
        public void run() {
            Tutorial android4 = new Tutorial("Hafiz 4", "........");
            Tutorial.publish(android4);
        }
    }


    public static void sendEmail(List<User> userList) {

        for (User user : userList) {
            // send email to user

            Log.d("Polling", "Email send: " + user.getName());
        }
    }

    public static class User {

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
    }

    private static class Tutorial {

        private String authorName;
        private String post;

        Tutorial() {
        }

        public Tutorial(String authorName, String post) {
            this.authorName = authorName;
            this.post = post;
        }


        private static List<Tutorial> publishedTutorials = new ArrayList<>();
        private static List<User> subscribedUsers = new ArrayList<>();

        public static void addSubscribedUser(User user) {
            subscribedUsers.add(user);
        }

        public static void publish(Tutorial tutorial) {
            publishedTutorials.add(tutorial);
            sendEmail(subscribedUsers);
        }
    }
}
