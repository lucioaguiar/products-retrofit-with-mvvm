package com.lucioaguiar.products.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.lucioaguiar.products.data.repositories.AuthRepository
import com.lucioaguiar.products.data.repositories.AuthRepositoryImp
import com.lucioaguiar.products.rest.RetrofitService
import com.lucioaguiar.products.ui.auth.AuthViewModel
import com.lucioaguiar.products.util.SharedPreferenceTypes
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { RetrofitService.getInstance() }
    single<SharedPreferences> { androidContext().getSharedPreferences(SharedPreferenceTypes.CONFIG, Context.MODE_PRIVATE) }
    single { Gson() }
    single<AuthRepository> { AuthRepositoryImp(get(), get(), get()) }
    viewModel { AuthViewModel(get()) }
}