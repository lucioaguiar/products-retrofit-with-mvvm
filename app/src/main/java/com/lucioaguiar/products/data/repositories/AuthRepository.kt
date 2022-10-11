package com.lucioaguiar.products.data.repositories


import com.lucioaguiar.products.data.models.SessionJWT
import com.lucioaguiar.products.util.UiState
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable

interface AuthRepository {
    fun registerUser(name: String, email: String, password: String): Flowable<SessionJWT>
    suspend fun loginUser(email: String, password: String): UiState<String>
    fun getSession(result: (SessionJWT?) -> Unit)
    fun logout(sessionJWT: SessionJWT, result: () -> Unit)
}