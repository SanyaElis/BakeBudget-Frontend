package ru.vsu.csf.bakebudget.services

import android.R.attr.data
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.MutableState
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.getToken
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.request.IngredientInProductRequestModel
import ru.vsu.csf.bakebudget.models.request.ProductRequestModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import java.io.File


@OptIn(DelicateCoroutinesApi::class)
fun findAllProducts(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    productsResponse: MutableList<ProductResponseModel>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.findAllProducts("Bearer ".plus(getToken(ctx)))
        onResultFindAll(res, productsResponse)
    }
}

private fun onResultFindAll(
    result: Response<List<ProductResponseModel>?>?,
    productsResponse: MutableList<ProductResponseModel>
) {
    if (result!!.body() != null) {
        if (result.body()!!.isNotEmpty()) {
            for (prod in result.body()!!) {
                productsResponse.add(prod)
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun createProduct(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    productRequestModel: ProductRequestModel,
    productId: MutableState<Int>,
    products: MutableList<ProductModel>,
    product: ProductModel,
    selectedImageUri: MutableState<Uri?>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.createProduct(productRequestModel, "Bearer ".plus(getToken(ctx)))
        onResultCreateProduct(res, ctx, productId, products, product, retrofitAPI, selectedImageUri)
    }
}

private fun onResultCreateProduct(
    result: Response<ProductResponseModel?>?,
    ctx: Context,
    productId: MutableState<Int>,
    products: MutableList<ProductModel>,
    product: ProductModel,
    retrofitAPI: RetrofitAPI,
    selectedImageUri: MutableState<Uri?>
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    if (result.body() != null) {
        productId.value = result.body()!!.id
        product.id = productId.value
        products.add(
            product
        )
        for (ingredient in product.ingredients) {
            ingredient.productId = productId.value
            addIngredientToProduct(
                ctx,
                retrofitAPI,
                ingredient
            )
        }
        if (selectedImageUri.value != null) {
//            uploadPicture(ctx, retrofitAPI, product, selectedImageUri.value!!)
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun addIngredientToProduct(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    ingredientInProductModel: IngredientInProductModel
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.addIngredientToProduct(
                IngredientInProductRequestModel(
                    ingredientInProductModel.ingredientId,
                    ingredientInProductModel.productId,
                    ingredientInProductModel.weight
                ), "Bearer ".plus(getToken(ctx))
            )
        onResultAddIngredientToProduct(res, ctx)
    }
}

fun onResultAddIngredientToProduct(
    result: Response<Void>?,
    ctx: Context,
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
}


@OptIn(DelicateCoroutinesApi::class)
fun findAllIngredientsInProduct(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    product: ProductModel,
    ingredientsResponse: MutableList<IngredientResponseModel>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.findAllIngredientsInProduct(product.id, "Bearer ".plus(getToken(ctx))
            )
        onResultFindAllIngredientsInProduct(res, ctx, product, ingredientsResponse)
    }
}

private fun onResultFindAllIngredientsInProduct(
    result: Response<List<IngredientInProductRequestModel>?>?,
    ctx: Context,
    product: ProductModel,
    ingredientsResponse: MutableList<IngredientResponseModel>
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    if (result.body() != null) {
        if (result.body()!!.isNotEmpty()) {
            for (ing in result.body()!!) {
                var ingName = ""
                for (ingr in ingredientsResponse) {
                    if (ingr.id == ing.ingredientId) {
                        ingName = ingr.name
                    }
                }
                product.ingredients.add(IngredientInProductModel(ing.ingredientId, product.id, ingName, ing.weight))
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun updateProduct(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    product: ProductRequestModel,
    productId : Int
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.updateProduct(productId, product, "Bearer ".plus(getToken(ctx)))
        onResultUpdateProduct(res, ctx)
    }
}

private fun onResultUpdateProduct(
    result: Response<Void>?,
    ctx: Context
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
}

@OptIn(DelicateCoroutinesApi::class)
fun updateIngredientInProduct(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    ingredientInProductModel: IngredientInProductModel
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.updateIngredientInProduct(
                IngredientInProductRequestModel(
                    ingredientInProductModel.ingredientId,
                    ingredientInProductModel.productId,
                    ingredientInProductModel.weight
                ), "Bearer ".plus(getToken(ctx))
            )
        onResultUpdateIngredientInProduct(res, ctx)
    }
}

private fun onResultUpdateIngredientInProduct(
    result: Response<Void>?,
    ctx: Context,
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    val eventParameters2 = "{\"button_clicked\":\"update ingredient in product\"}"
    AppMetrica.reportEvent(
        "Ingredient in product updated",
        eventParameters2
    )
}

@OptIn(DelicateCoroutinesApi::class)
fun deleteIngredientInProduct(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    ingredientInProductModel: IngredientInProductModel
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.deleteIngredientInProduct(
                ingredientInProductModel.ingredientId,
                ingredientInProductModel.productId,
                "Bearer ".plus(getToken(ctx))
            )
        onResultDeleteInProduct(res, ctx)
    }
}

private fun onResultDeleteInProduct(
    result: Response<Void>?,
    ctx: Context,
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    val eventParameters1 = "{\"button_clicked\":\"delete ingredient in product\"}"
    AppMetrica.reportEvent(
        "Ingredient in product deleted",
        eventParameters1
    )
}