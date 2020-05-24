package com.ror.weirddatainput.data

import androidx.lifecycle.MutableLiveData
import com.ror.weirddatainput.repositories.Loadable
import io.reactivex.Completable
import java.io.Serializable
import java.util.concurrent.TimeUnit

class PostmanEchoRepository(
    private val restApi: PostmanEcho,
    private val subscriptionTransformer: SubscriptionTransformer
) {
    val dummyLiveData: MutableLiveData<Loadable<DummyData>> = MutableLiveData()

    fun load(): Completable {
        return restApi.getDummyData("dummy value 1", "dummy value 2")
            .delay(2, TimeUnit.SECONDS) //debug only
            .compose(subscriptionTransformer.getSingleTransformer())
            .doOnSubscribe { dummyLiveData.value = Loadable.loading() }
            .doOnSuccess {
                dummyLiveData.value = Loadable.success(DummyData(it.args.foo1, it.args.foo2))
            }
            .ignoreElement()
            .onErrorComplete {
                dummyLiveData.value = Loadable.error(it)
                true
            }
    }
}

data class DummyData(val foo1: String, val foo2: String) : Serializable