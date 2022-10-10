package com.lucioaguiar.products.data.repositories

import android.content.SharedPreferences
import android.os.RemoteException
import android.util.Log
import com.google.gson.Gson
import com.lucioaguiar.products.data.models.SessionJWT
import com.lucioaguiar.products.rest.RetrofitService
import com.lucioaguiar.products.util.SharedPreferenceConstants
import com.lucioaguiar.products.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    override suspend fun loginUser(email: String, password: String) : UiState<String> {

        return withContext(Dispatchers.IO) {
            try {
                val data = retrofitService.login(email, password)
                sharedPreferences.edit().putString(SharedPreferenceConstants.USER_SESSION, gson.toJson(data)).apply()
                UiState.Success("Login successfully!")
            } catch (exception: RemoteException) {
                UiState.Failure("Você está offline, conecte-se antes de continuar.")
            } catch (exception: Exception) {
                UiState.Failure("Failed Login")
            }
        }

//        retrofitService.login(email, password).enqueue(object : Callback<SessionJWT> {
//            override fun onResponse(call: Call<SessionJWT>, response: Response<SessionJWT>) {
//                response.body()?.let {  sessionJWT ->
//                    if(sessionJWT.status == "success"){
//                        sharedPreferences.edit().putString(SharedPreferenceConstants.USER_SESSION, gson.toJson(response.body())).apply()
//                        result.invoke(UiState.Success("Login successfully!"))
//                    }
//                }
//                response.errorBody()?.let {
//                    result.invoke(UiState.Failure("Incorrect email or password"))
//                }
//
//            }
//            override fun onFailure(call: Call<SessionJWT>, t: Throwable) {
//                Log.i("retorno", t.message.toString())
//                result.invoke(UiState.Failure("Failed Login"))
//            }
//        })
    }

    override fun getSession(result: (SessionJWT?) -> Unit) {
        val userStr = sharedPreferences.getString(SharedPreferenceConstants.USER_SESSION, null)
        if (userStr == null){
            result.invoke(null)
        }else{
            val sessionJWT = gson.fromJson(userStr, SessionJWT::class.java)
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
            override fun onFailure(call: Call<Response<String>>, t: Throwable) {}

        })
    }

}