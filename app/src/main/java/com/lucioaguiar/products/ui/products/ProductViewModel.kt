package com.lucioaguiar.products.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucioaguiar.products.data.models.Image
import com.lucioaguiar.products.data.models.Product
import com.lucioaguiar.products.data.models.SessionJWT
import com.lucioaguiar.products.data.repositories.ProductRepository
import com.lucioaguiar.products.util.UiState
import okhttp3.MultipartBody

class ProductViewModel(val repository: ProductRepository) : ViewModel() {

    private val _products = MutableLiveData<UiState<List<Product>>>()
    val products: LiveData<UiState<List<Product>>> = _products

    private val _delete = MutableLiveData<UiState<Product>>()
    val delete: LiveData<UiState<Product>> = _delete

    private val _store = MutableLiveData<UiState<Product>>()
    val store: LiveData<UiState<Product>> = _store

    private val _update = MutableLiveData<UiState<Product>>()
    val update: LiveData<UiState<Product>> = _update

    private val _upload = MutableLiveData<UiState<Image>>()
    val upload: LiveData<UiState<Image>> = _upload

    fun getProducts(sessionJWT: SessionJWT) {
        _products.value = UiState.Loading
        repository.getProducts(sessionJWT) { _products.value = it }
    }

    fun delete(
        id: Int,
        sessionJWT: SessionJWT
    ) {
        _delete.value = UiState.Loading
        repository.delete(id, sessionJWT) { _delete.value = it }
    }

    fun storeProduct(
        name: String,
        description: String,
        image: String?,
        sessionJWT: SessionJWT
    ) {
        _store.value = UiState.Loading
        repository.store(name, description, image, sessionJWT){ _store.value = it }
    }

    fun updateProduct(
        id: Int,
        name: String,
        description: String,
        image: String?,
        sessionJWT: SessionJWT,
    ) {
        _update.value = UiState.Loading
        repository.update(id, name, description, image, sessionJWT){ _update.value = it }
    }

    fun upload(
        image: MultipartBody.Part,
        sessionJWT: SessionJWT
    ){
        _upload.value = UiState.Loading
        repository.upload(image, sessionJWT){ _upload.value = it }
    }

}