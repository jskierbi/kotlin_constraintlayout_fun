package com.jskierbi.playground_contstraintlayout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.schedulers.Schedulers
import rx.schedulers.Schedulers.io
import rx.subscriptions.Subscriptions
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

  var progressSubscription = Subscriptions.empty()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    uiBtnRegister.setOnClickListener { uiStartLoading() }
    uiProgressBar.visibility = View.GONE
  }

  fun uiStartLoading() {
    enableUi(false)
    progressSubscription.unsubscribe()
    progressSubscription = Observable.interval(10, TimeUnit.MILLISECONDS, io())
        .take(300)
        .observeOn(mainThread())
        .doOnUnsubscribe { enableUi(true) }
        .subscribe { uiProgressBar.progress = it.toInt() }
  }

  fun enableUi(flg: Boolean) {
    if (!flg) uiProgressBar.apply {
        max = 300
        progress = 0
        visibility = View.VISIBLE
    }
    uiProgressBar.visibility = if (flg) View.GONE else View.VISIBLE
    uiInputEmail.isEnabled = flg
    uiInputName.isEnabled = flg
    uiInputPassword.isEnabled = flg
    uiBtnRegister.isEnabled = flg
  }
}
