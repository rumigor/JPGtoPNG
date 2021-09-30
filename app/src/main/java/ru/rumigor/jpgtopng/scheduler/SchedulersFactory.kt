package ru.rumigor.jpgtopng.scheduler

object SchedulersFactory {
    fun create(): Schedulers = DefaultSchedulers()
}