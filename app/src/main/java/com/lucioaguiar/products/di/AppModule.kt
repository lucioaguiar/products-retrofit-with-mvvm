package com.lucioaguiar.products.di

import com.lucioaguiar.products.ui.auth.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

interface HelloRepository {
    fun giveHello(): String
}

class HelloRepositoryImpl() : HelloRepository {
    override fun giveHello() = "Hello Koin"
}


val appModule = module {
    single<HelloRepository> { HelloRepositoryImpl() }
    viewModel { AuthViewModel(get()) }
}