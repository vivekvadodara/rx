package com.vivek.rxapplication

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rx.replayingShare
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()


            createRxStream()
            //startRStream1()
//            startRStream()
        }


        //hot & cold & backpressure

                backPressure()
//        coldObservables()
//        coldObservables2()
//        hotObservables()
//        doOn()
//        replayHot()
//        hotPublish()
//        replayRefCount()
//        replayingShare()


    }

    private fun createRxStream() {

        val observable = getObservable()
        val observer = getObserver()
        observable.subscribe(observer)

    }

    private fun startRStream1() {

        val numbers = Observable.range(1, 6)

        val strings = Observable.just(
            "One", "Two", "Three",

            "Four", "Five", "Six"
        )

        val zipped = Observable.zip(strings, numbers, BiFunction<String, Int, Int> { s, i ->
            i
        }).subscribe(::println)
    }

    private fun startRStream() {

        val numbers = Observable.range(1, 6)

        val strings = Observable.just(
            "One", "Two", "Three",

            "Four", "Five", "Six"
        )

        val zipped = Observables.zip(strings, numbers) { s, n -> "$s $n" }.subscribe(::println)
    }

    private fun getObserver(): Observer<Int> {

        return object : Observer<Int> {
            override fun onComplete() {
                Log.d("Main", "OnComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Log.d("Main", "onSubscribe")
            }

            override fun onNext(t: Int) {
                Log.d("Main", "onNext - $t")
            }

            override fun onError(e: Throwable) {
                Log.d("Main", "onError - ${e.message}")
            }

        }
    }

    private fun getObservable(): Observable<Int> {
        return Observable.just(1, 2, 3, 4, 5)
    }


    // hot & cold & backpressure

    private fun coldObservables() {

        val coldObservable = Observable.interval(1000, TimeUnit.MILLISECONDS).doOnNext {
            Log.d("Main", "t - doOnNext ${it}")
        }


        val sub1 = Consumer<Long>(
            {
                Log.d("Main", "t - sub1 - onNext ${it}")
            }
        )

        val sub2 = Consumer<Long>(
            {
                Log.d("Main", "t - sub2 - onNext ${it}")
            }
        )

        coldObservable.subscribe(sub1)
        coldObservable.subscribe(sub2)
        //not calling dispose

        while (true) {
        }

    }

    private fun coldObservables2() {
        val coldObservable = Observable.interval(1000, TimeUnit.MILLISECONDS).doOnNext {
            Log.d("Main", "t - doOnNext ${it}")
        }

        val sub1 = Consumer<Long>(
            {
                Log.d("Main", "t - sub1 - onNext ${it}")
            }
        )

        val d1 = coldObservable.subscribe(sub1)

        Thread.sleep(5000)

        val sub2 = Consumer<Long>(
            {
                Log.d("Main", "t - sub2 - onNext ${it}")
            }
        )

        val d2 = coldObservable.subscribe(sub2)

        Thread.sleep(5000)

        d1.dispose()
        d2.dispose()

        while (true) {
        }

    }

    private fun replayingShare() {

        val observable: Observable<Long> =
            Observable.interval(1000, TimeUnit.MILLISECONDS).doOnNext {
                Log.d("Main", "t - doOnNext ${it}")
            }


        val con = observable.replayingShare()


        val sub1 = Consumer<Long>(
            {
                Log.d("Main", "t - sub1 - onNext ${it}")
            }
        )

        val sub2 = Consumer<Long>(
            {
                Log.d("Main", "t - sub2 - onNext ${it}")
            }
        )
        val d1 = con.subscribe(sub1)
        val d2 = con.subscribe(sub2)

        Thread.sleep(5000)

        d2.dispose()

        Thread.sleep(5000)

        d1.dispose()

        Thread.sleep(5000)

        val sub3 = Consumer<Long>(
            {
                Log.d("Main", "t - sub3 - onNext ${it}")
            })
        val d3 = con.subscribe(sub3)

        Thread.sleep(5000)

        d3.dispose()

        while (true) {
        }
    }

    private fun replayRefCount() {

        val random = Random
        val observable = Observable.interval(1000, TimeUnit.MILLISECONDS).doOnNext {
            Log.d("Main", "t - doOnNext ${it}")
        }

        val con = observable.publish().refCount()


        val sub1 = Consumer<Long>(
            {
                Log.d("Main", "t - sub1 - onNext ${it}")
            }
        )

        val sub2 = Consumer<Long>(
            {
                Log.d("Main", "t - sub2 - onNext ${it}")
            }
        )
        val d1 = con.subscribe(sub1)
        val d2 = con.subscribe(sub2)

        Thread.sleep(5000)

        d2.dispose()

        Thread.sleep(5000)

        d1.dispose()

        Thread.sleep(5000)

        val sub3 = Consumer<Long>(
            {
                Log.d("Main", "t - sub3 - onNext ${it}")
            })
        val d3 = con.subscribe(sub3)

        Thread.sleep(5000)

        d3.dispose()

        while (true) {
        }
    }

    private fun hotPublish() {

        val random = Random
        val observable = Observable.create<Long> {

                emitter ->
            Observable.interval(
                1000, TimeUnit.MILLISECONDS
            ).subscribe {
                Log.d("Main", "t - emitting ${it}")
                emitter.onNext(random.nextLong())
            }

        }

        val con = observable.publish()
        con.connect()


        val sub1 = Consumer<Long>(
            {
                Log.d("Main", "t - sub1 - onNext ${it}")
            }
        )

        val sub2 = Consumer<Long>(
            {
                Log.d("Main", "t - sub2 - onNext ${it}")
            }
        )
        val d1 = con.subscribe(sub1)
        val d2 = con.subscribe(sub2)

        Thread.sleep(5000)

        d2.dispose()

        Thread.sleep(5000)

        d1.dispose()

        while (true) {
        }
    }

    private fun replayHot() {

        val random = Random
        val observable = Observable.create<Long> {

                emitter ->
            Observable.interval(
                1000, TimeUnit.MILLISECONDS
            ).subscribe {
                Log.d("Main", "t - emitting ${it}")
                emitter.onNext(random.nextLong())
            }

        }
        val con = observable.replay()
        con.connect()


        val sub1 = Consumer<Long>(
            {
                Log.d("Main", "t - sub1 - onNext ${it}")
            }
        )

        val sub2 = Consumer<Long>(
            {
                Log.d("Main", "t - sub2 - onNext ${it}")
            }
        )
        val d1 = con.subscribe(sub1)
        val d2 = con.subscribe(sub2)

        Thread.sleep(10000)

        d2.dispose()

        Thread.sleep(10000)

        d1.dispose()

        while (true) {
        }
    }

    private fun doOn() {

        val observable = PublishSubject.create<Int>()


        val s = observable
            .doOnNext {
                Log.d("Main", "t - doOnNext ${it}")
            }
            .onErrorResumeNext { throwable: Throwable ->

                Log.d("Main", "t - onErrorResumeNext ${throwable}")
                when (throwable) {
                    is NoSuchElementException -> Observable.just(200)
                    else -> Observable.error(throwable)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("Main", "t - onNext ${it}")

                },
                { t ->
                    Log.d("Main", "t - onError ${t.message}")
                }
            )


        for (i in 0..10) {
            observable.onNext(i)
            if (i == 6) {
                observable.onError(NoSuchElementException("t - error "))
            }
        }
    }

    private fun backPressure() {
        val observable = PublishSubject.create<Int>()


        val s = observable.doOnNext {
            Log.d("Main", "t - doOnNext ${it}")
        }
            .toFlowable(BackpressureStrategy.LATEST)
            .observeOn(AndroidSchedulers.mainThread()) // this is important
            .subscribe(
                {
                    Log.d("Main", "t - onNext ${it}")

                },
                { t ->
                    Log.d("Main", "t - onError ${t.message}")
                }
            )


        for (i in 0..1000) {
            observable.onNext(i)
        }
    }

    private fun hotObservables() {

        val observable = Observable.interval(1, TimeUnit.SECONDS)
        val connectableObservable = observable.publish()
        connectableObservable.connect()

        connectableObservable.subscribe(
            {
                Log.d("Main", "observer 1 - $it")
            }
        )

        Thread.sleep(5000)

        connectableObservable.subscribe(
            {
                Log.d("Main", "observer 2 - $it")
            }
        )

        Thread.sleep(5000)

        connectableObservable.subscribe(
            {
                Log.d("Main", "observer 3 - $it")
            }
        )

        while (true) {
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
