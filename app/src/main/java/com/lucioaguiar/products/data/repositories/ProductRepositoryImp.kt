package com.lucioaguiar.products.data.repositories

import android.util.Log
import com.lucioaguiar.products.data.models.Image
import com.lucioaguiar.products.data.models.Product
import com.lucioaguiar.products.data.models.SessionJWT
import com.lucioaguiar.products.rest.RetrofitService
import com.lucioaguiar.products.util.UiState
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepositoryImp(
    val retrofitService: RetrofitService) : ProductRepository {

    override fun getProducts(sessionJWT: SessionJWT, result: (UiState<List<Product>>) -> Unit) {
        retrofitService.getAllProducts(sessionJWT.authorisation.token)
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(
                    call: Call<List<Product>>,
                    response: Response<List<Product>>
                ) {
                    response.body()?.let {
                        result.invoke(UiState.Success(it))
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    result.invoke(UiState.Failure("getProducts failed"))
                }
            })
    }

    override fun delete(id: Int, sessionJWT: SessionJWT, result: (UiState<Product>) -> Unit) {
        retrofitService.delete(id, sessionJWT.authorisation.token)
            .enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    response.body()?.let {
                        result.invoke(UiState.Success(it))
                    }
                }
                override fun onFailure(call: Call<Product>, t: Throwable) {
                    result.invoke(UiState.Failure("Could not delete the product"))
                }
            })
        }

    override fun store(
        name: String,
        description: String,
        image: String?,
        sessionJWT: SessionJWT,
        result: (UiState<Product>) -> Unit
    ) {
        retrofitService.storeProduct(name, description, image, sessionJWT.authorisation.token).enqueue(object : Callback<Product>{
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                response.body()?.let {
                    result.invoke(UiState.Success(it))
                }
            }
            override fun onFailure(call: Call<Product>, t: Throwable) {
                result.invoke(UiState.Failure("Failed to register product"))
            }
        })
    }

    override fun update(
        id: Int,
        name: String,
        description: String,
        image: String?,
        sessionJWT: SessionJWT,
        result: (UiState<Product>) -> Unit
    ) {
        retrofitService.updateProduct(id, name, description, image, sessionJWT.authorisation.token).enqueue(object : Callback<Product>{
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                response.body()?.let {
                    result.invoke(UiState.Success(it))
                }
            }
            override fun onFailure(call: Call<Product>, t: Throwable) {
                result.invoke(UiState.Failure("Failed to update product"))
            }
        })
    }

    override fun upload(
        image: MultipartBody.Part,
        sessionJWT: SessionJWT,
        result: (UiState<Image>) -> Unit
    ) {
        retrofitService.upload(image, sessionJWT.authorisation.token).enqueue(object : Callback<Image>{
            override fun onResponse(call: Call<Image>, response: Response<Image>) {
                response.body()?.let {
                    result.invoke(UiState.Success(it))
                }
                Log.i("retorno", response.message())
            }
            override fun onFailure(call: Call<Image>, t: Throwable) {
                result.invoke(UiState.Failure("There was an error uploading the image"))
            }
        })
    }

}