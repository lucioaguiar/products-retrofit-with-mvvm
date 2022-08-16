package com.lucioaguiar.products.data.repositories

import com.lucioaguiar.products.data.models.Product
import com.lucioaguiar.products.data.models.SessionJWT
import com.lucioaguiar.products.rest.RetrofitService
import com.lucioaguiar.products.util.UiState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepositoryImp(
    val retrofitService: RetrofitService) : ProductRepository{

    override fun getProducts(sessionJWT: SessionJWT, result: (UiState<List<Product>>) -> Unit) {
        retrofitService.getAllProducts(sessionJWT.authorisation.token).enqueue(object : Callback<List<Product>>{
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                response.body()?.let {
                    result.invoke(UiState.Success(it))
                }
            }
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                result.invoke(UiState.Failure("getProducts failed"))
            }
        })
    }

    override fun delete(id: Int, result: () -> Unit) {


    }

}