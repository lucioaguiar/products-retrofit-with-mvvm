package com.lucioaguiar.products.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucioaguiar.products.data.models.Product
import com.lucioaguiar.products.data.models.SessionJWT
import com.lucioaguiar.products.data.repositories.ProductRepository
import com.lucioaguiar.products.util.UiState

class ProductViewModel(val repository: ProductRepository) : ViewModel() {

    private val _products = MutableLiveData<UiState<List<Product>>>()
    val products: LiveData<UiState<List<Product>>> = _products

    private val _delete = MutableLiveData<UiState<Product>>()
    val delete: LiveData<UiState<Product>> = _delete

    fun getProducts(sessionJWT: SessionJWT) {
        _products.value = UiState.Loading
        repository.getProducts(sessionJWT) { _products.value = it }
    }

    fun delete(id: Int, sessionJWT: SessionJWT) {
        repository.delete(id, sessionJWT) { _delete.value = it }
    }

}