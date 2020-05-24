package com.ror.weirddatainput

import androidx.lifecycle.*
import com.ror.weirddatainput.data.DummyData
import com.ror.weirddatainput.data.PostmanEchoRepository
import com.ror.weirddatainput.repositories.Loadable
import com.ror.weirddatainput.repositories.LoadableStatus
import io.reactivex.disposables.CompositeDisposable

class SampleFragmentViewModel(
    private val handle: SavedStateHandle,
    private val repository: PostmanEchoRepository
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val remoteLiveData: LiveData<Loadable<DummyData>> = repository.dummyLiveData
    private val localLiveData: MutableLiveData<Loadable<DummyData>> = MutableLiveData()

    val dummyData: MediatorLiveData<Loadable<DummyData>> = MediatorLiveData()
    val modified: MutableLiveData<Boolean> = MutableLiveData(false)
    val saveable: MutableLiveData<Boolean> = MutableLiveData()

    init {
        dummyData.addSource(remoteLiveData) {
            if (it.status == LoadableStatus.SUCCESS) {
                modified.value = false
            }

            val locallyStoredValue = handle.get<DummyData?>(KEY)

            if (locallyStoredValue == null) {
                localLiveData.value = it
            } else {
                reactToInput(locallyStoredValue.foo1, locallyStoredValue.foo2)
            }
        }

        dummyData.addSource(localLiveData) {
            val isModified = remoteLiveData.value?.data != localLiveData.value?.data

            modified.value = isModified
            saveable.value = it.status == LoadableStatus.SUCCESS && isModified

            handle[KEY] = it.data

            dummyData.value = it
        }

        startLoadingData()
    }

    fun reactToInput(field1: String, field2: String) {
        if (remoteLiveData.value?.status == LoadableStatus.SUCCESS) {
            remoteLiveData.value?.data?.let {
                val freshData = DummyData(field1, field2)

                when {
                    field1.isBlank() -> {
                        localLiveData.value = Loadable.error(RequiredField1(), freshData)
                    }
                    field2.isBlank() -> {
                        localLiveData.value = Loadable.error(RequiredField2(), freshData)
                    }
                    else -> {
                        localLiveData.value = Loadable.success(freshData)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun startLoadingData() {
        compositeDisposable.add(
            repository.load().subscribe()
        )
    }

    companion object {
        const val KEY = "the.key"
    }
}

class RequiredField1 : Throwable()

class RequiredField2 : Throwable()