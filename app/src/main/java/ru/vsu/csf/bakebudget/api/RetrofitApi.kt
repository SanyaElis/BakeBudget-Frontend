package ru.vsu.csf.bakebudget.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.request.CalculationRequestModel
import ru.vsu.csf.bakebudget.models.request.IngredientInProductRequestModel
import ru.vsu.csf.bakebudget.models.request.IngredientRequestModel
import ru.vsu.csf.bakebudget.models.request.OrderRequestModel
import ru.vsu.csf.bakebudget.models.request.OutgoingRequestModel
import ru.vsu.csf.bakebudget.models.request.ProductRequestModel
import ru.vsu.csf.bakebudget.models.request.ReportRequestModel
import ru.vsu.csf.bakebudget.models.request.UserSignUpRequestModel
import ru.vsu.csf.bakebudget.models.response.UserSignInResponseModel
import ru.vsu.csf.bakebudget.models.request.UserSignInRequestModel
import ru.vsu.csf.bakebudget.models.response.CalculationResponseModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.OrderResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.models.response.ReportIncomeResponseModel
import ru.vsu.csf.bakebudget.models.response.ReportOrdersResponseModel

interface RetrofitAPI {
    @POST("auth/signup")
    suspend fun register(@Body userSignUpRequestModel: UserSignUpRequestModel?): Response<Void>?

    @POST("auth/signin")
    suspend fun login(@Body userModel: UserSignInRequestModel?): Response<UserSignInResponseModel?>?

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

    @HTTP(method = "DELETE", path = "products/deleteIngredient/{ingredientId}/{productId}", hasBody = true)
    suspend fun deleteIngredientInProduct(@Path("ingredientId") ingredientId: Int, @Path("productId") productId: Int, @Header("Authorization") authorization: String): Response<Void>?

    //___________________________________________________________________________________________________________________________________

    @GET("outgoings/findAll/{productId}")
    suspend fun findAllOutgoingsInProduct(@Path("productId") productId: Int, @Header("Authorization") authorization: String): Response<List<OutgoingModel>?>?

    @POST("outgoings/create/{productId}")
    suspend fun createOutgoing(@Path("productId") productId: Int, @Body outgoingModel: OutgoingRequestModel?, @Header("Authorization") authorization: String): Response<OutgoingModel?>?

    @PUT("outgoings/update/{id}")
    suspend fun updateOutgoing(@Path("id") id: Int, @Body outgoingModel: OutgoingRequestModel?, @Header("Authorization") authorization: String): Response<Void>?

    @DELETE("outgoings/delete/{id}")
    suspend fun deleteOutgoing(@Path("id") id: Int, @Header("Authorization") authorization: String): Response<Void>?

    //___________________________________________________________________________________________________________________________________

    @GET("orders/findAll")
    suspend fun findAllOrders(@Header("Authorization") authorization: String): Response<List<OrderResponseModel>?>?

    @POST("orders/calculate")
    suspend fun calculate(@Body calculationRequestModel: CalculationRequestModel, @Header("Authorization") authorization: String): Response<CalculationResponseModel?>?

    @POST("orders/create")
    suspend fun createOrder(@Body orderRequestModel: OrderRequestModel, @Header("Authorization") authorization: String): Response<OrderResponseModel?>?

    @PUT("orders/setStatus/{id}")
    suspend fun setStatus(@Path("id") id: Int, @Query("status") status: String, @Header("Authorization") authorization: String): Response<Void>?

    //___________________________________________________________________________________________________________________________________

    @GET("getGroupCode")
    suspend fun getCode(@Header("Authorization") authorization: String): Response<String>?

    @PUT("changeRole")
    suspend fun changeRole(@Header("Authorization") authorization: String): Response<Void>?

    @PUT("createGroupCode")
    suspend fun createCode(@Header("Authorization") authorization: String): Response<String>?

    //_______

    @PUT("setGroupCode")
    suspend fun setCode( @Query("groupCode") groupCode: String, @Header("Authorization") authorization: String): Response<String>?

    //___________________________________________________________________________________________________________________________________

    @POST("report/calculateByOrderSelf")
    suspend fun reportOrdersSelf(@Body reportRequestModel: ReportRequestModel, @Header("Authorization") authorization: String): Response<ReportOrdersResponseModel?>?

    @POST("report/calculateByIncomeSelf")
    suspend fun reportIncomeSelf(@Body reportRequestModel: ReportRequestModel, @Header("Authorization") authorization: String): Response<ReportIncomeResponseModel?>?

    @POST("report/calculateByOrderGroup")
    suspend fun reportOrdersGroup(@Body reportRequestModel: ReportRequestModel, @Header("Authorization") authorization: String): Response<ReportOrdersResponseModel?>?

    @POST("report/calculateByIncomeGroup")
    suspend fun reportIncomeGroup(@Body reportRequestModel: ReportRequestModel, @Header("Authorization") authorization: String): Response<ReportIncomeResponseModel?>?

    //___________________________________________________________________________________________________________________________________


}