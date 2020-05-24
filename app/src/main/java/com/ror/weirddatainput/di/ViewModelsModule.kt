package com.ror.weirddatainput.di

import androidx.lifecycle.SavedStateHandle
import com.ror.weirddatainput.SampleFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { (handle: SavedStateHandle) -> SampleFragmentViewModel(handle, get()) }
}