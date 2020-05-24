package com.ror.weirddatainput.di

import com.ror.weirddatainput.data.PostmanEcho
import com.ror.weirddatainput.data.PostmanEchoRepository
import com.ror.weirddatainput.data.SubscriptionTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        val httpInterceptor = HttpLoggingInterceptor()
        httpInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(httpInterceptor).build()
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://postman-echo.com/")
            .build()
            .create(PostmanEcho::class.java)
    }

    single {
        PostmanEchoRepository(get(), get())
    }

    single {
        SubscriptionTransformer(Schedulers.io(), AndroidSchedulers.mainThread())
    }
}