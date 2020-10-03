package com.vivek.rxapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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


        val button = Button(this)

        button.setOnClickListener { it -> it.animation }

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

        coldObservableExample()

        //four type of Observable data types
        //Observable
        //Single
        //Maybe
        //Flowable
        //Completable

        //SINGLE --Server API request -> //error //success

        //OBSERVABLE -- web socket ->10,....12......20 temperature published by appliance ....onComplete

        //Maybe  -- 1,2

        //Flowable --


        // Observable source - 1,2,3,4.....400, 401....900, 999,1000   -
        //onSubscribe //server-1, server-2, server-3...........server-1000
//        Observable.just(10).toFlowable(BackpressureStrategy.LATEST)
//        Observable.just(10).toFlowable(BackpressureStrategy.BUFFER)
//        Observable.just(10).toFlowable(BackpressureStrategy.DROP)
//        Observable.just(10).toFlowable(BackpressureStrategy.MISSING)
//        Observable.just(10).toFlowable(BackpressureStrategy.ERROR)


        /*    val s: Completable = Observable.just(10, 20,30)
                .ignoreElements()

            Single.just(10).toCompletable()*/


/*

        class User(val name: String, val online: Observable<Boolean>)
        class UserNonReactive(val name: String, val online: Boolean)


        val user = User("sushant", getOnlineStatus() )
       val withoutFlat =  Observable.just(user)
            .subscribe(
            {
                Log.d("TAG", "${it.name} - onlineObservable ${it.online}")

                it.online.subscribe {
                    Log.d("TAG", "online $it")

                    //
                }
            }
        )

        val ss = Observable.just(user)
            .flatMap {
                val name = it.name
                it.online.flatMap { online ->
                    Observable.just(UserNonReactive(name, online))
                }
            }
            .subscribe(
                {
                    Log.d("TAG", "${it.name} - onlineObservable ${it.online}")

                }
            )

*/


        /* Completable.create {

         }.subscribe(
             {
                // no data is received, just confirmation. logout, login 200, createUser 201
             },
             {

             }
         )

         Observable.just(10,20).firstElement()

         val dd = Single.just(listOf(10,20,30))
             .map {
                     list: List<Int> ->
                 list.map {
                     it * 100
                 }
             }
             .subscribe(
             {
                 receivedList: List<Int> ->
                 Log.d("TAG", "Single OnSuccess called in observer $receivedList")

                 receivedList.map { item ->
                     Log.d("TAG", "printing all elements $item")
                 }

             },
             {
                 Log.e("TAG", "Single onError called in observer")
             }
         )*/
/*
        val d = Observable.just(10,20, 30).subscribe(
            {
                Log.d("TAG", "Observable onNext called in observer $it")
            },
            {
                Log.e("TAG", "Observable onError called in observer")
            }
        )*/

        /*  Single.create<String> {

  //            it.onSuccess("SUCCESS..no more OnNext")
              it.onError(IllegalStateException())

          }.doOnSuccess { Log.d("TAG", "Single DOOnSuccess $it") }
              .subscribe(
              {
                  Log.d("TAG", "Single OnSuccess called in observer $it")
              },
              {
                  Log.e("TAG", "Single onError called in observer")
              }
          )
  */

        /*
        Observable.create<String> {

           *//* it.onNext("onNext 1")
            it.onNext("onNext 2")*//*

            it.onComplete() // or it.onError()


        }
        Maybe.create<String> {  }
        Completable.create {  }


        Observable.just(newsPublish(View(this)))

        val d = Observable.create<String> { emitterOfCreate: ObservableEmitter<String> ->

            emitterOfCreate.onNext("1")

            emitterOfCreate.onNext("10")

            emitterOfCreate.onNext("20")
            emitterOfCreate.onComplete()
        }.doOnNext { Log.d("TAG", "CREATE on next $it") }
            .doOnComplete { Log.d("TAG", "CREATE on Complete ") }
            .doOnError { Log.d("TAG", "CREATE on Error $it") }
            .doOnSubscribe { Log.d("TAG", "CREATE on Subscribe $it") }
            .subscribe(
                {
                    Log.d("TAG", "on next $it")
                },
                {
                    Log.e("TAG", "on error $it")
                },
                {
                    Log.e("TAG", "on complete")
                },
                {
                    Log.e("TAG", "on subscribe $it ")
                }
            )
*/

        /*   Tutorial("Tutorial Pause", "........").publish()

   //        Tutorial.REGISTER_FOR_SUBSCRIPTION.subscribe {
   //            Log.d("RxApproach", "published tutorial $it")
   //        }

           //Observable
           //Observer

           val myList = listOf<Int>(10, 20, 30)
           val transformedList = myList
               .map { it.toString()+"ABC" }
               .distinct()
           //water -> blue_water
           // blue_water <-> fish
           // 4 blue_water <-> 4 fish

           //"water", "fish", "water", "fish", "water", "fish", "water", "fish"

           // "water", "water", "water", "water", "water"

           val listOfInt = listOf<Int>(1,2,3,4)
           Observable.fromIterable(listOfInt).subscribe{
               //print
           }

           val arrayOfInt = arrayOf(1,2,3,4)
           Observable.fromArray(arrayOfInt).forEach {

           }*/


/*

        val ssss: Disposable = Observable.just("water", "water", "stone","water", "water")
            .map {
                Log.d("TAG", "ssss original item $it")
                val transformed = it.toInt()
                Log.d("TAG", "ssss transformed item $transformed")
                return@map transformed
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                //onNext
                Log.d("RxApproach", "ssss OnNext $it")
                //sushantXYZ yakubXYZ
            }*/


        /*val mechDeptStudentList = listOf(
            Student("sachin", "MECH", 1),
            Student("pravin", "MECH", 2),
            Student("kk", "MECH", 3)
        )

        val cseDeptStudentList = listOf(
            Student("sushant", "CSE", 1),
            Student("mayur", "CSE", 2),
            Student("atul", "CSE", 3)
        )

        val allCollegeStudentList: List<List<Student>> =
            listOf(mechDeptStudentList, cseDeptStudentList, listOf(Student("", "", 1)))

        Log.d("TAG", "Flatten use $allCollegeStudentList")

        Log.d("TAG", "Flatten use ${allCollegeStudentList.flatten()}")

//String result = ...


        val result = allCollegeStudentList.flatMap { list ->
            list.map { student ->
                Student("Mr." + student.name, student.dept, student.rollNO)
            }
        }

        Log.d("TAG", result.toString())

        val l1 = listOf<Int>(1, 2, 3)
        val l2 = listOf<Int>(10, 11, 12)

        val combined = listOf(l1, l2)
        val intResult = combined.flatMap {
            it.map {
                it * 10
            }
        }

        Log.d("TAG", "int result : $intResult")


        Observable.just(1, "Hellow", intResult).subscribe {
            Log.d("TAG", "111 after subscribe $it")
        }

        Observable.just(allCollegeStudentList, "1").subscribe {

            Log.d("TAG", "after subscribe $it")
        }*/

        //output - Unit


        /*val s = BehaviorSubject.createDefault("A")

        s.onNext("B")
        s.subscribe {
            Log.d("RxApproach", "OnNext $it")
        }

        Thread.sleep(2000)
        s.onNext("C")
        Thread.sleep(2000)
        s.onNext("D")

        val ss = PublishSubject.create<Int>()
        ss.onNext(10)

        ss.subscribe {
            Log.d("RxApproach", "OnNext int $it")
        }

        ss.onNext(20)

        ss.onNext(30)


        val sss = ReplaySubject.create<Int>()
        ss.onNext(100)
        ss.onNext(101)
        ss.onNext(102)

        sss.subscribe {
            Log.d("RxApproach", "OnNext replay int $it")
        }

        ss.onNext(200)

        ss.onNext(300)*/
        /*
        *  @Override
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
        }*/

    }

    private fun coldObservableExample() {

        val random = Math.random()

        val observableSource = Observable.create<Double> { emitter -> // cold observable
            emitter.onNext(random) // data is produced internally
        }.doOnSubscribe {
            Log.d("TAG", " doOnSubscribe")
        }.doOnNext {
            Log.d("TAG", " doOnNext")
        }

        observableSource.subscribe {
            Log.d("TAG", "SUB1 on Next - $it") //
        }

        observableSource.subscribe {
            Log.d("TAG", "SUB2 on Next - $it") //
        }
    }

    private fun getOnlineStatus(): Observable<Boolean> {
        return Observable.create<Boolean> {

            it.onNext(false)

            Thread.sleep(2000)
            it.onNext(true)

            Thread.sleep(2000)
            it.onNext(false)
        }
    }

    private fun validate(username: String, password: String): Boolean {

        val disposable = Observable.just(username, password)
            .subscribe(
                {
                    if (!(it != null && it.isNotEmpty() && it.length > 3)) {
                        isValid = false
                    } else {
                        isValid = true
                    }
                }
            )
        return isValid
    }


    private fun numericName(it: String): Boolean {
        TODO("Not yet implemented")
    }

    private fun validateName(it: String) = it != "sushant"

    companion object {
        fun sendEmail(user: User) {
            Log.d("RxApproach", "Email send: " + user.name)
        }

        private var isValid = true
    }

    // int a = 1
    // int b = 2
    //
    // if(a == b) -> false

    // Student s1 = new Student(...) ... equals(), toString()
    // Student s2 = new Student(...)
    // if (s1 == s2) ..
    class Student11(var name: String, var dept: String, var rollNO: Int)

    data class Student(var name: String, var dept: String, var rollNO: Int)
    //getter setter

    override fun onDestroy() {
        super.onDestroy()

    }

}

