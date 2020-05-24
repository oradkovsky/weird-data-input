package com.ror.weirddatainput.data

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleTransformer


class SubscriptionTransformer(
    private val subscribeOnScheduler: Scheduler,
    private val observeOnScheduler: Scheduler
) {

    fun <T> getSingleTransformer(): SingleTransformer<T, T> {
        return SingleTransformer { single: Single<T> ->
            single
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
        }
    }
}