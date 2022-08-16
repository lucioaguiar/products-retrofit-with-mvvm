package com.lucioaguiar.products.data.repositories

import com.lucioaguiar.products.data.models.SessionJWT
import com.lucioaguiar.products.data.models.User
import com.lucioaguiar.products.util.UiState

interface AuthRepository {
    fun registerUser(name: String, email: String, password: String, result: (UiState<String>) -> Unit)
    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun getSession(result: (SessionJWT?) -> Unit)
    fun logout(sessionJWT: SessionJWT, result: () -> Unit)
}