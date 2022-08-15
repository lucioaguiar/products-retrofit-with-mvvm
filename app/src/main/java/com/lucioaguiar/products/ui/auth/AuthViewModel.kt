package com.lucioaguiar.products.ui.auth

import androidx.lifecycle.ViewModel
import com.lucioaguiar.products.di.HelloRepository

class AuthViewModel(val helloRepository: HelloRepository) : ViewModel() {


    fun say(){
        helloRepository.giveHello()
    }

}