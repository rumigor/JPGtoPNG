package ru.rumigor.jpgtopng.mvp

import android.net.Uri
import io.reactivex.disposables.CompositeDisposable

import io.reactivex.rxkotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.jpgtopng.data.Converter
import ru.rumigor.jpgtopng.scheduler.Schedulers
import java.net.URI

class MainPresenter (
    private val converter: Converter,
    private val schedulers: Schedulers
        ) : MvpPresenter<MainView>() {

    private val disposables = CompositeDisposable()

    fun transform(uri:Uri){
        disposables+=
            converter
                .convert(uri)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(viewState::transformImage)

    }

    override fun onDestroy() {
        disposables.clear()
    }

}