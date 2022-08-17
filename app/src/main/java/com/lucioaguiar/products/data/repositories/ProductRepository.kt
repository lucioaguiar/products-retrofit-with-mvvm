package com.lucioaguiar.products.data.repositories

import com.lucioaguiar.products.data.models.Image
import com.lucioaguiar.products.data.models.Product
import com.lucioaguiar.products.data.models.SessionJWT
import com.lucioaguiar.products.util.UiState
import okhttp3.MultipartBody

interface ProductRepository {
    fun getProducts(sessionJWT: SessionJWT, result: (UiState<List<Product>>) -> Unit)
    fun delete(id: Int, sessionJWT: SessionJWT, result: (UiState<Product>) -> Unit)
    fun store(name: String, description: String, image: String?, sessionJWT: SessionJWT, result: (UiState<Product>) -> Unit)
    fun update(id: Int, name: String, description: String, image: String?, sessionJWT: SessionJWT, result: (UiState<Product>) -> Unit)
    fun upload(image: MultipartBody.Part, sessionJWT: SessionJWT, result: (UiState<Image>) -> Unit)
}