package com.lucioaguiar.products.rest

import com.lucioaguiar.products.data.models.Image
import com.lucioaguiar.products.data.models.Product
import com.lucioaguiar.products.data.models.SessionJWT
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {

    @FormUrlEncoded
    @POST("register")
    fun register(@Field("name") name: String, @Field("email") email: String, @Field("password") password: String): Flowable<SessionJWT>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(@Field("email") email: String, @Field("password") password: String): SessionJWT

    @FormUrlEncoded
    @POST("logout")
    fun logout(@Field("token") token: String): Call<Response<String>>

    @GET("products")
    fun getAllProducts(@Query("token") token: String): Call<List<Product>>

    @FormUrlEncoded
    @POST("product")
    fun storeProduct(@Field("name") name: String,
                     @Field("description") description: String,
                     @Field("image") image: String?,
                     @Field("token") token: String): Call<Product>

    @FormUrlEncoded
    @PUT("product/{id}")
    fun updateProduct(@Path("id") id: Int,
                      @Field("name") name: String,
                      @Field("description") description: String,
                      @Field("image") image: String?,
                      @Field("token") token: String): Call<Product>

    @Multipart
    @POST("upload")
    fun upload(@Part image: MultipartBody.Part, @Query("token") token: String): Call<Image>

    @DELETE("product/{id}")
    fun delete(@Path("id") id: Int, @Query("token") token: String ): Call<Product>

    companion object {

        private val retrofitService: RetrofitService by lazy {

            val retrofit = Retrofit.Builder()
                .baseUrl("https://products.lucioaguiar.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()

            retrofit.create(RetrofitService::class.java)

        }

        fun getInstance() : RetrofitService {
            return retrofitService
        }
    }

}