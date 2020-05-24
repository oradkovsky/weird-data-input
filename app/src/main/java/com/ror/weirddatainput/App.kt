package com.ror.weirddatainput

import android.app.Application
import com.ror.weirddatainput.di.appModule
import com.ror.weirddatainput.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(object : Timber.DebugTree() {
            public override fun createStackElementTag(element: StackTraceElement): String? {
                return "xxx(" + element.fileName + ":" + element.lineNumber + ")#" + element.methodName
            }
        })

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            fragmentFactory()
            modules(appModule + viewModelsModule)
        }
    }
}