package com.ror.weirddatainput.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PostmanEcho {
    @GET("get")
    fun getDummyData(
        @Query("foo1") param1: String,
        @Query("foo2") param2: String
    ): Single<DummyDataResponse>
}

data class DummyDataResponse(
    val args: DummyDataResponseArgs,
    val headers: Any,
    val url: String
)

data class DummyDataResponseArgs(
    val foo1: String,
    val foo2: String
)