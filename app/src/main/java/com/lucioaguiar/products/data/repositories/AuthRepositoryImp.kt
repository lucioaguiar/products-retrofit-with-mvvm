package com.lucioaguiar.products.data.repositories

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.lucioaguiar.products.data.models.SessionJWT
import com.lucioaguiar.products.data.models.User
import com.lucioaguiar.products.util.SharedPreferenceConstants
import com.lucioaguiar.products.util.UiState
import com.ocanha.mvvmrecyclerviewcomretrofitemkotlin.rest.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepositoryImp(
    val retrofitService: RetrofitService,
    val sharedPreferences: SharedPreferences,
    val gson: Gson) : AuthRepository{

    override fun registerUser(
        name: String,
        email: String,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        retrofitService.register(name, email, password).enqueue(object : Callback<SessionJWT> {
            override fun onResponse(call: Call<SessionJWT>, response: Response<SessionJWT>) {
                sharedPreferences.edit().putString(SharedPreferenceConstants.USER_SESSION, gson.toJson(response.body())).apply()
                result.invoke(UiState.Success("Successfully registered!"))
            }
            override fun onFailure(call: Call<SessionJWT>, t: Throwable) {
                result.invoke(UiState.Failure("Registration failed"))
            }
        })
    }

    override fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        retrofitService.login(email, password).enqueue(object : Callback<SessionJWT> {
            override fun onResponse(call: Call<SessionJWT>, response: Response<SessionJWT>) {
                response.body()?.let {  sessionJWT ->
                    if(sessionJWT.status == "success"){
                        sharedPreferences.edit().putString(SharedPreferenceConstants.USER_SESSION, gson.toJson(response.body())).apply()
                        result.invoke(UiState.Success("Login successfully!"))
                    }
                }
                response.errorBody()?.let {
                    result.invoke(UiState.Failure("Incorrect email or password"))
                }

            }
            override fun onFailure(call: Call<SessionJWT>, t: Throwable) {
                result.invoke(UiState.Failure("Failed Login"))
            }
        })
    }

    override fun getSession(result: (SessionJWT?) -> Unit) {
        val user_str = sharedPreferences.getString(SharedPreferenceConstants.USER_SESSION, null)
        if (user_str == null){
            result.invoke(null)
        }else{
            val sessionJWT = gson.fromJson(user_str, SessionJWT::class.java)
            result.invoke(sessionJWT)
        }
    }

    override fun logout(sessionJWT: SessionJWT, result: () -> Unit) {
        retrofitService.logout(sessionJWT.authorisation.token).enqueue(object : Callback<Response<String>>{
            override fun onResponse(
                call: Call<Response<String>>,
                response: Response<Response<String>>
            ) {
                sharedPreferences.edit().putString(SharedPreferenceConstants.USER_SESSION,null).apply()
                result.invoke()
            }

            override fun onFailure(call: Call<Response<String>>, t: Throwable) {

            }

        })
    }

}