package com.lucioaguiar.products.rest


import com.lucioaguiar.products.data.models.Product
import com.lucioaguiar.products.data.models.SessionJWT
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {

    @FormUrlEncoded
    @POST("register")
    fun register(@Field("name") name: String, @Field("email") email: String, @Field("password") password: String): Call<SessionJWT>

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<SessionJWT>

    @FormUrlEncoded
    @POST("logout")
    fun logout(@Field("token") token: String): Call<Response<String>>

    @GET("products")
    fun getAllProducts(@Query("token") token: String): Call<List<Product>>

    @DELETE("product/{id}")
    fun delete(@Path("id") id: Int, @Query("token") token: String ): Call<Product>

    companion object {

        private val retrofitService: RetrofitService by lazy {

            val retrofit = Retrofit.Builder()
                .baseUrl("http://produtos.test/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(RetrofitService::class.java)

        }

        fun getInstance() : RetrofitService {
            return retrofitService
        }
    }

}