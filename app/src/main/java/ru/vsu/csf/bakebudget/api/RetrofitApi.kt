package ru.vsu.csf.bakebudget.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.vsu.csf.bakebudget.models.request.UserSignUpRequestModel
import ru.vsu.csf.bakebudget.models.response.UserSignInResponseModel
import ru.vsu.csf.bakebudget.models.request.UserSignInRequestModel

interface RetrofitAPI {
//    @FormUrlEncoded
    @POST("auth/signup")
    fun register(@Body userSignUpRequestModel: UserSignUpRequestModel?): Call<String?>?

    @POST("auth/signin")
    fun login(@Body userModel: UserSignInRequestModel?): Call<UserSignInResponseModel?>?
}