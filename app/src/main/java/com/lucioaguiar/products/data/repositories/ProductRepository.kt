package com.lucioaguiar.products.data.repositories

import com.lucioaguiar.products.data.models.Product
import com.lucioaguiar.products.data.models.SessionJWT
import com.lucioaguiar.products.util.UiState

interface ProductRepository {
    fun getProducts(sessionJWT: SessionJWT, result: (UiState<List<Product>>) -> Unit)
    fun delete(id: Int, result: () -> Unit)

}