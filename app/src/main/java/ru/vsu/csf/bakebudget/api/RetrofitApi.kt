package ru.vsu.csf.bakebudget.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.vsu.csf.bakebudget.models.request.IngredientRequestModel
import ru.vsu.csf.bakebudget.models.request.UserSignUpRequestModel
import ru.vsu.csf.bakebudget.models.response.UserSignInResponseModel
import ru.vsu.csf.bakebudget.models.request.UserSignInRequestModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel

interface RetrofitAPI {
    @POST("auth/signup")
    fun register(@Body userSignUpRequestModel: UserSignUpRequestModel?): Call<String?>?

    @POST("auth/signin")
    fun login(@Body userModel: UserSignInRequestModel?): Call<UserSignInResponseModel?>?

//___________________________________________________________________________________________________________________________________
    @GET("ingredients/findAll")
    suspend fun findAllIngredients(@Header("Authorization") authorization: String): Response<List<IngredientResponseModel>?>?

    @POST("ingredients/create")
    suspend fun createIngredient(@Body ingredientModel: IngredientRequestModel?, @Header("Authorization") authorization: String): Response<IngredientResponseModel?>?

    @PUT("ingredients/update/{id}")
    suspend fun updateIngredient(@Path("id") id: Int, @Body ingredientModel: IngredientRequestModel?, @Header("Authorization") authorization: String): Response<IngredientResponseModel?>?
}