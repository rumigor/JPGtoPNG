package ru.rumigor.jpgtopng.mvp

import android.net.Uri
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MainView: MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun loadImage ()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun transformImage(uri: Uri)
}