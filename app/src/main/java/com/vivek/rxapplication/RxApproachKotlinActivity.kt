package com.vivek.rxapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class RxApproachKotlinActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rx)

        Tutorial("Tutorial 1", "........").publish()
        Tutorial("Tutorial 2", "........").publish()
        Tutorial("Tutorial 3", "........").publish()

        // I have already three tutorials and later user subscribed for email
        val a = User("A", "a@a.com")
        val b = User("B", "b@a.com")
        val c = User("C", "c@a.com")
        val d = User("D", "d@a.com")

        // Now A,C and D click subscribe button
        Tutorial.REGISTER_FOR_SUBSCRIPTION.subscribe(a)
        Tutorial.REGISTER_FOR_SUBSCRIPTION.subscribe(c)
        Tutorial.REGISTER_FOR_SUBSCRIPTION.subscribe(d)

        val disposable = Observable.just(NewsAgency("news1", "author1"))
            .subscribe {
                Log.d("RxApproach", "received news: $it")
            }

        Tutorial("Tutorial 4", "........").publish()
    }

    class User(var name: String?, var email: String?) : Observer<Tutorial> {

        override fun onSubscribe(d: Disposable) {}
        override fun onNext(o: Tutorial) {
            sendEmail(this)
        }

        override fun onError(e: Throwable) {}
        override fun onComplete() {}
    }

    data class NewsAgency(val title: String, val author: String)

    class Tutorial {
        private var authorName: String? = null
        private var post: String? = null

        private constructor() {}
        constructor(authorName: String?, post: String?) {
            this.authorName = authorName
            this.post = post
        }

        @SuppressLint("CheckResult")
        fun publish() {
            REGISTER_FOR_SUBSCRIPTION.publish()
        }

        companion object {
            var REGISTER_FOR_SUBSCRIPTION: Observable<Tutorial> = Observable.just(Tutorial())
        }
    }

    fun newsPublish(view: View?) {
        Tutorial("Tutorial Pause", "........").publish()

        Tutorial.REGISTER_FOR_SUBSCRIPTION.subscribe{
            Log.d("RxApproach", "published tutorial $it")
        }
    }

    companion object {
        fun sendEmail(user: User) {
            Log.d("RxApproach", "Email send: " + user.name)
        }
    }
}
