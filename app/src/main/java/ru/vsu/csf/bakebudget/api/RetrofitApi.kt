package ru.vsu.csf.bakebudget.api

import okhttp3.internal.http.hasBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.request.IngredientInProductRequestModel
import ru.vsu.csf.bakebudget.models.request.IngredientRequestModel
import ru.vsu.csf.bakebudget.models.request.ProductRequestModel
import ru.vsu.csf.bakebudget.models.request.UserSignUpRequestModel
import ru.vsu.csf.bakebudget.models.response.UserSignInResponseModel
import ru.vsu.csf.bakebudget.models.request.UserSignInRequestModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel

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

    @DELETE("ingredients/delete/{id}")
    suspend fun deleteIngredient(@Path("id") id: Int, @Header("Authorization") authorization: String): Response<Void>?

    //___________________________________________________________________________________________________________________________________


    @GET("products/findAll")
    suspend fun findAllProducts(@Header("Authorization") authorization: String): Response<List<ProductResponseModel>?>?

    @GET("products/findAllIngredients/{id}")
    suspend fun findAllIngredientsInProduct(@Path("id") id: Int, @Header("Authorization") authorization: String): Response<List<IngredientInProductRequestModel>?>?

    @POST("products/create")
    suspend fun createProduct(@Body productModel: ProductRequestModel?, @Header("Authorization") authorization: String): Response<ProductResponseModel?>?

    @POST("products/addIngredient")
    suspend fun addIngredientToProduct(@Body ingredientModel: IngredientInProductRequestModel?, @Header("Authorization") authorization: String): Response<Void>?

    @PUT("products/updateIngredient")
    suspend fun updateIngredientInProduct(@Body ingredientModel: IngredientInProductRequestModel?, @Header("Authorization") authorization: String): Response<Void>?

    @PUT("products/update/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body productModel: ProductRequestModel?, @Header("Authorization") authorization: String): Response<Void>?

    @HTTP(method = "DELETE", path = "products/deleteIngredient", hasBody = true)
    suspend fun deleteIngredientInProduct(@Body ingredientModel: IngredientInProductRequestModel?, @Header("Authorization") authorization: String): Response<Void>?
}