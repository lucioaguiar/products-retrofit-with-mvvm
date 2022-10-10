package com.lucioaguiar.products.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucioaguiar.products.data.models.SessionJWT
import com.lucioaguiar.products.data.models.User
import com.lucioaguiar.products.data.repositories.AuthRepository
import com.lucioaguiar.products.util.UiState
import kotlinx.coroutines.launch

class AuthViewModel(val repository: AuthRepository) : ViewModel() {

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>> = _register

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>> = _login

    fun register(
        name: String,
        email: String,
        password: String
    ) {
        _register.value = UiState.Loading
        repository.registerUser(
            name,
            email,
            password
        ) { _register.value = it }
    }

    fun login(
        email: String,
        password: String
    ) {
        _login.value = UiState.Loading
        viewModelScope.launch {
            _login.value = repository.loginUser(email, password)
        }
    }

    fun getSession(result: (SessionJWT?) -> Unit){
        repository.getSession(result)
    }

    fun logout(sessionJWT: SessionJWT, result: () -> Unit){
        repository.logout(sessionJWT, result)
    }

}