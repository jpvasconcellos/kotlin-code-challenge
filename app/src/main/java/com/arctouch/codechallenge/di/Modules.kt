package com.arctouch.codechallenge.di

import com.arctouch.codechallenge.data.Repository
import com.arctouch.codechallenge.data.RepositoryImpl
import com.arctouch.codechallenge.feature.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    single<Repository> { RepositoryImpl() }
}