package com.vivek.rxapplication

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rx.replayingShare
import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
//        setSupportActionBar(toolbar)

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//
//
//            createRxStream()
//            //startRStream1()
////            startRStream()
//        }


        //hot & cold & backpressure
//        backPressure()
//        coldObservables()
//        coldObservables2()
//        hotObservables()
//        doOn()
//        replayHot()
//        hotPublish()
//        replayRefCount()
//        replayingShare()
//        replayingShare_observer()
//        flatMapExample()
//        flatMapExampleWithoutList()
//        concatMapExample()
//        concatMapEagerExample()
//        switchMapExample()
//        combineLatestExample()
//        mergeExample()
//        mergeCompletableExample()
//        concatExample()
//        zipExample()
//        groupByExample()

    }

    fun flatMapExample(v: View) {

        val items = listOf("a", "b", "c", "d", "e", "f")
        val d = Observable.fromIterable(items)
                .flatMap { item: String ->
                    val delay = Random.nextLong(10)

                    Observable.just("$item - $delay")
                            .delay(delay, TimeUnit.SECONDS, Schedulers.computation())
                }
                .toList()
                .doOnSuccess { Log.d("Main", "Rx - $it") }
                .subscribeOn(Schedulers.io()).subscribe()

    }

    fun flatMapExampleWithoutList(v: View) {

        val items = listOf("a", "b", "c", "d", "e", "f")

        val d = Observable.fromIterable(items)
                .flatMap { item ->
                    val delay = Random.nextLong(10)
                    Observable.just("$item - x - $delay")
                            .delay(delay, TimeUnit.SECONDS, Schedulers.computation())
                }
                .doOnNext { Log.d("Main", "Rx - $it") }
                .subscribeOn(Schedulers.io()).subscribe()

    }

    fun concatMapExample(v: View) {

        val items = listOf("a", "b", "c", "d", "e", "f")
        Log.d("Main", "Rx - Start")
        val d = Observable.fromIterable(items)
                .concatMap { item ->
                    val delay = Random.nextLong(10)
                    Observable.just("$item - $delay")
                            .delay(delay, TimeUnit.SECONDS, Schedulers.computation())
                }
                .toList()
                .doOnSuccess { Log.d("Main", "Rx - $it") }
                .subscribeOn(Schedulers.io()).subscribe()

    }

    fun concatMapEagerExample(v: View) {

        val items = listOf("a", "b", "c", "d", "e", "f")
        Log.d("Main", "Rx - Start")
        val d = Observable.fromIterable(items)
                .concatMapEager { item ->
                    val delay = Random.nextLong(10)
                    Observable.just("$item - $delay")
                            .delay(delay, TimeUnit.SECONDS, Schedulers.computation())
                }
                .toList()
                .doOnSuccess { Log.d("Main", "Rx - $it") }
                .subscribeOn(Schedulers.io()).subscribe()

    }

    fun switchMapExample(v: View) {
        val items = listOf("a", "b", "c", "d", "e", "f")
        Log.d("Main", "Rx - Start")
        val d = getSwitchObservableSource()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .switchMap { item ->

                    val delay = Random.nextLong(1, 3)
                    Log.d("Main", "value - $item - $delay")
                    Observable.just("$item - $delay")
                            .delay(delay, TimeUnit.SECONDS, Schedulers.computation())
                }
                .toList()
                .doOnSuccess { Log.d("Main", "Rx - $it") }
                .subscribeOn(Schedulers.io()).subscribe()


    }

    private fun getSwitchObservableSource(): Observable<String> {
        return Observable.create { emitter ->
            emitter.onNext("a")
            Thread.sleep(1000)
            emitter.onNext("b")
            Thread.sleep(3000)
            emitter.onNext("c")

            Thread.sleep(2000)

            emitter.onComplete()
        }
    }


    fun switchMapExample2(v: View) {

        val items = listOf("a", "b", "c", "d", "e", "f")
        Log.d("Main", "Rx - Start")
        val sourceSubject = BehaviorRelay.create<String>()
        sourceSubject.accept("a")
        sourceSubject.accept("b")
        sourceSubject.accept("c")
        sourceSubject.accept("d")
        sourceSubject.accept("e")
        sourceSubject.accept("f")

        val d = sourceSubject
                .flatMap { item ->
                    val delay = Random.nextLong(5)
                    BehaviorRelay.createDefault("$item - $delay").delay(delay, TimeUnit.SECONDS)
                }
                .subscribe(
                        {
                            Log.d("Main", "Rx - $it")
                        },
                        {

                        }
                )

    }


    fun combineLatestExample(v: View) {


        val observable1 = Observable.intervalRange(2, 5, 2, 3, TimeUnit.SECONDS)
                .doOnNext { Log.d("Main", "Rx - o1 emit $it") }

        val observable2 = Observable.intervalRange(2, 4, 2, 4, TimeUnit.SECONDS)
                .doOnNext { Log.d("Main", "Rx - o2 emit $it") }

        val d = Observable.combineLatest(
                observable1,
                observable2,
                BiFunction<Long, Long, String> { count1, count2 ->
                    "Refreshed observable1 : $count1 , Refreshed observable2 : $count2"
                }

        ).subscribeOn(Schedulers.io()).subscribe { Log.d("Main", "Rx - $it") }

        Thread.sleep(10000)

        d.dispose()
    }

    fun mergeExample(v: View) {
        val observable1 = Observable.interval(2, 3, TimeUnit.SECONDS)
                .doOnNext { Log.d("Main", "Rx - o1 emit $it") }
        val observable2 = Observable.interval(2, 4, TimeUnit.SECONDS)
                .doOnNext { Log.d("Main", "Rx - o2 emit $it") }

        val d = Observable.merge(
                observable1, observable2
        )

                .subscribeOn(Schedulers.io()).subscribe(
                        { Log.d("Main", "Rx - $it") },
                        {

                        })
        Thread.sleep(10000)

        d.dispose()
    }

    fun mergeCompletableExample(v: View) {

        val o1 = Observable.intervalRange(1, 4, 1, 1, TimeUnit.SECONDS)
        val c4 = Completable.fromObservable(o1).doOnComplete { Log.d("Main", "Rx - c4 complete") }

        val c1 = Completable.complete().doOnComplete { Log.d("Main", "Rx - c1 complete") }

        val c2 = Completable.complete().doOnComplete { Log.d("Main", "Rx - c2 complete") }

        val c3 = Completable.complete().doOnComplete { Log.d("Main", "Rx - c3 complete") }


        val d1 = Completable.merge(listOf(c1, c2, c3, c4))
                .subscribeOn(Schedulers.io()).subscribe {
                    Log.d("Main", "Rx - merge complete")
                }
        Thread.sleep(10000)


        d1.dispose()

    }

    fun concatExample(v: View) {
        val observable1 = Observable.intervalRange(1, 3, 0, 1, TimeUnit.SECONDS)
                .doOnNext { Log.d("Main", "Rx - o1 emit $it") }
                .doOnComplete { Log.d("Main", "Rx - o1 complete") }

        val observable2 = Observable.intervalRange(1, 3, 3, 1, TimeUnit.SECONDS)
                .doOnNext { Log.d("Main", "Rx - o2 emit $it") }
                .doOnComplete { Log.d("Main", "Rx - o2 complete") }

        val d = Observable.concat(
                observable1, observable2
        ).subscribeOn(Schedulers.io()).subscribe { Log.d("Main", "Rx - $it") }
        Thread.sleep(10000)

        d.dispose()
    }


    fun zipExample(v: View) {
        val observable1 = Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS)
                .doOnNext { Log.d("Main", "Rx - o1 emit $it") }
                .doOnComplete { Log.d("Main", "Rx - o1 complete") }
                .doOnDispose { Log.d("Main", "Rx - o1 dispose") }

        val observable2 = Observable.intervalRange(10, 8, 2, 2, TimeUnit.SECONDS)
                .doOnNext { Log.d("Main", "Rx - o2 emit $it") }
                .doOnComplete { Log.d("Main", "Rx - o2 complete") }
                .doOnDispose { Log.d("Main", "Rx - o2 dispose") }

        val d = Observable.zip(
                observable1, observable2,
                BiFunction<Long, Long, Long> { o1, o2 ->

                    Log.d("Main", "Rx - o1 - $o1 and o2 - $o2")
                    o1 + o2
                }
        ).subscribeOn(Schedulers.io()).subscribe { Log.d("Main", "Rx - $it") }
        Thread.sleep(60000)

        d.dispose()
    }


    data class DateModel(val date: Int, val value: String)


    fun groupByExample(v: View) {

        val dateList = listOf(
                DateModel(18, "1"),
                DateModel(19, "2"),
                DateModel(19, "3"),
                DateModel(18, "1"),
                DateModel(19, "2"),
                DateModel(19, "3")
        )
        val d =
                Observable.fromIterable(dateList)
                        .doOnNext { Log.d("Main", "Rx - after fromIterable : $it") }
                        .groupBy { it.date }.doOnNext {
                            Log.d("Main", "Rx - after groupBy : ${it.key}")
                        }
                        .flatMapSingle {
                            it.toList()
                        }.doOnNext {
                            Log.d("Main", "Rx - after flatMapSingle : $it")
                        }
                        .subscribeOn(Schedulers.io()).subscribe { Log.d("Main", "Rx - dateSize ${it.size}") }


    }


    //

    fun createRxStream(v: View) {

        val observable = getObservable()
        val observer = getObserver()
        observable.subscribeOn(Schedulers.io()).subscribe(observer)

        observable.subscribeOn(Schedulers.io()).subscribe(
                {

                },
                {

                },
                {
                },
                {

                }
        )


        val observable1 = Observable.just(1, 2, 3)
        observable1.subscribeOn(Schedulers.io()).subscribeBy(
                onNext = { Log.d("Main", "kotlin rx - $it") },
                onComplete = { Log.d("Main", "kotlin rx - complete") }
        )

    }

    fun startRStream1(v: View) {

        val numbers = Observable.range(1, 6)

        val strings = Observable.just(
                "One", "Two", "Three",

                "Four", "Five", "Six"
        )

        val zipped = Observable.zip(strings, numbers, BiFunction<String, Int, Int> { s, i ->
            i
        }).subscribeOn(Schedulers.io()).subscribe(::println)
    }

    fun startRStream(v: View) {

        val numbers = Observable.range(1, 6)

        val strings = Observable.just(
                "One", "Two", "Three",

                "Four", "Five", "Six"
        )

        val zipped = Observables.zip(strings, numbers) { s, n -> "$s $n" }.subscribeOn(Schedulers.io()).subscribe(::println)
    }

    fun getObserver(): Observer<Int> {

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

    fun getObservable(): Observable<Int> {
        return Observable.just(1, 2, 3, 4, 5)
    }


    // hot & cold & backpressure

    fun coldObservables(v: View) {

        val coldObservable = Observable.interval(100, TimeUnit.MILLISECONDS).doOnNext {
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

        val d1 = coldObservable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(sub1)
        val d2 = coldObservable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(sub2)
        //not calling dispose

        Thread.sleep(5000)
        d1.dispose()
        d2.dispose()


    }

    fun coldObservables2(v: View) {
        val coldObservable = Observable.interval(100, TimeUnit.MILLISECONDS).doOnNext {
            Log.d("Main", "t - doOnNext ${it}")
        }

        val sub1 = Consumer<Long>(
                {
                    Log.d("Main", "t - sub1 - onNext ${it}")
                }
        )

        val d1 = coldObservable.subscribeOn(Schedulers.io()).subscribe(sub1)

        Thread.sleep(5000)

        val sub2 = Consumer<Long>(
                {
                    Log.d("Main", "t - sub2 - onNext ${it}")
                }
        )

        val d2 = coldObservable.subscribeOn(Schedulers.io()).subscribe(sub2)

        Thread.sleep(5000)

        d1.dispose()
        d2.dispose()
    }

    fun replayingShare_observer(v: View) {

        val observable: Observable<Long> =
                Observable.interval(100, TimeUnit.MILLISECONDS).doOnNext {
                    Log.d("Main", "t - doOnNext ${it}")
                }


        val con = observable.replayingShare()

        Thread.sleep(5000)

        val sub1 = object : Observer<Long> {
            override fun onComplete() {
                Log.d("Main", "t - sub1 - onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Log.d("Main", "t - sub1 - onSubscribe")
            }

            override fun onNext(t: Long) {
                Log.d("Main", "t - sub1 - onNext ${t}")
            }

            override fun onError(e: Throwable) {
                Log.d("Main", "t - sub1 - onError ${e}")
            }

        }

        con.subscribeOn(Schedulers.io()).subscribe(sub1)

        val s = Observable.just(1L).subscribeOn(Schedulers.io()).subscribe(sub1)

        Thread.sleep(5000)


        val sub2 = object : Observer<Long> {
            override fun onComplete() {
                Log.d("Main", "t - sub2 - onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Log.d("Main", "t - sub2 - onSubscribe")
            }

            override fun onNext(t: Long) {
                Log.d("Main", "t - sub2 - onNext ${t}")
            }

            override fun onError(e: Throwable) {
                Log.d("Main", "t - sub2 - onError ${e}")
            }

        }

        Thread.sleep(2000)
        con.subscribeOn(Schedulers.io()).subscribe(sub2)


        Thread.sleep(20000)
    }

    fun replayingShare(v: View) {

        val observable: Observable<Long> =
                Observable.interval(100, TimeUnit.MILLISECONDS).doOnNext {
                    Log.d("Main", "t - doOnNext ${it}")
                }


        val con = observable.replayingShare()

        Thread.sleep(5000)

        val sub1 = Consumer<Long>(
                {
                    Log.d("Main", "t - sub1 - onNext ${it}")
                }
        )

        val d1 = con.subscribeOn(Schedulers.io()).subscribe(sub1)


        Thread.sleep(5000)

        d1.dispose()

        val sub3 = Consumer<Long>(
                {
                    Log.d("Main", "t - sub3 - onNext ${it}")
                })

        Thread.sleep(2000)
        val d3 = con.subscribeOn(Schedulers.io()).subscribe(sub3)

        Thread.sleep(20000)

        d3.dispose()

    }

    fun replayRefCount(v: View) {

        val random = Random
        val observable = Observable.interval(100, TimeUnit.MILLISECONDS).doOnNext {
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
        val d1 = con.subscribeOn(Schedulers.io()).subscribe(sub1)
        val d2 = con.subscribeOn(Schedulers.io()).subscribe(sub2)

        Thread.sleep(5000)

        d2.dispose()

        Thread.sleep(5000)

        d1.dispose()

        Thread.sleep(5000)

        val sub3 = Consumer<Long>(
                {
                    Log.d("Main", "t - sub3 - onNext ${it}")
                })
        val d3 = con.subscribeOn(Schedulers.io()).subscribe(sub3)

        Thread.sleep(5000)

        d3.dispose()

    }

    fun hotPublish(v: View) {

        val random = Random
        val observable = Observable.create<Long> {

            emitter ->
            Observable.interval(
                    1000, TimeUnit.MILLISECONDS
            ).subscribeOn(Schedulers.io()).subscribe {
                Log.d("Main", "t - emitting ${it}")
                emitter.onNext(random.nextLong())
            }

        }
//
        val con = observable.publish()

        //sub1
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
        val d1 = con.subscribeOn(Schedulers.io()).subscribe(sub1)
        val d2 = con.subscribeOn(Schedulers.io()).subscribe(sub2)

        Thread.sleep(5000)

        d2.dispose()

        Thread.sleep(5000)

        d1.dispose()

        while (true) {
        }
    }

    fun replayHot(v: View) {

        val random = Random
        val observable = Observable.create<Long> {

            emitter ->
            Observable.interval(
                    1000, TimeUnit.MILLISECONDS
            ).subscribeOn(Schedulers.io()).subscribe {
                Log.d("Main", "t - emitting ${it}")
                emitter.onNext(random.nextLong())
            }

        }
        val con = observable.replay(1)
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
        val d1 = con.subscribeOn(Schedulers.io()).subscribe(sub1)

        Thread.sleep(5000)

        val d2 = con.subscribeOn(Schedulers.io()).subscribe(sub2)


        Thread.sleep(5000)
        d2.dispose()
        d1.dispose()

        while (true) {
        }
    }

    fun doOn(v: View) {

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
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io()).subscribe(
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

    fun backPressure(v: View) {
        val flowable = Flowable.just(1, 2, 3, 4)
        val observable: PublishSubject<Int> = PublishSubject.create<Int>()
        val s = observable
//                .observeOn(Schedulers.io())
//                .subscribeOn(Schedulers.io())
//                .doOnNext {
//                    Log.d("Main", "t - doOnNext ${it}")
//                }.doOnComplete { Log.d("Main", "t - onComplete") }
//                .doOnError {Log.d("Main", "t - onError")  }
//                .doOnDispose { Log.d("Main", "t - onDispose")  }
//                .toFlowable(BackpressureStrategy.MISSING)
                .toFlowable(BackpressureStrategy.BUFFER)
//                .toFlowable(BackpressureStrategy.DROP)
//                .toFlowable(BackpressureStrategy.LATEST)
//                .toFlowable(BackpressureStrategy.ERROR)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io()).subscribe(getFlowableObserver())


        for (i in 0..300) {
            observable.onNext(i)
        }
    }

    private fun getFlowableObserver(): Subscriber<Int> {
//             vvvvvvvvvv ---------------------------------------------
        return object : Subscriber<Int> {
            override fun onSubscribe(d: Subscription) {
                Log.d("FragmentActivity.TAG", "onSubscribe")
                // request is required to start items flowing!
                // vvvvvvvvvvvvvvvvvvvvvvvvv --------------------------------
                d.request(Long.MAX_VALUE)
            }

            //          vvvvvv --------------------------------------------
            override fun onNext(integer: Int) {
                Log.d("FragmentActivity.TAG", "onSuccess: $integer")
            }

            override fun onError(e: Throwable) {
                Log.e("FragmentActivity.TAG", "onError: " + e.message)
            }

            //          vvvvvvvvvv ----------------------------------------
            fun onComplete(e: Throwable?) {
                Log.e("FragmentActivity.TAG", "onComplete")
            }

            override fun onComplete() {
                Log.e("FragmentActivity.TAG", "onComplete -2 ")
            }
        }
    }

    fun hotObservables(v: View) {

        val observable = Observable.interval(1, TimeUnit.SECONDS)
        val connectableObservable = observable.publish()
        connectableObservable.connect()

        connectableObservable.subscribeOn(Schedulers.io()).subscribe(
                {
                    Log.d("Main", "observer 1 - $it")
                }
        )

        Thread.sleep(5000)

        connectableObservable.subscribeOn(Schedulers.io()).subscribe(
                {
                    Log.d("Main", "observer 2 - $it")
                }
        )

        Thread.sleep(5000)

        connectableObservable.subscribeOn(Schedulers.io()).subscribe(
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
