package com.lucioaguiar.products.data.repositories

import android.content.SharedPreferences
import android.os.RemoteException
import com.google.gson.Gson
import com.lucioaguiar.products.data.models.SessionJWT
import com.lucioaguiar.products.rest.RetrofitService
import com.lucioaguiar.products.util.SharedPreferenceConstants
import com.lucioaguiar.products.util.UiState
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
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
        password: String
    ): Flowable<SessionJWT> {
        return retrofitService.register(name, email, password)
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