package ru.vsu.csf.bakebudget.services

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.request.IngredientInProductRequestModel
import ru.vsu.csf.bakebudget.models.request.ProductRequestModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel

@OptIn(DelicateCoroutinesApi::class)
fun findAllProducts(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    productsResponse: MutableList<ProductResponseModel>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.findAllProducts("Bearer ".plus(jwtToken.value))
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
    jwtToken: MutableState<String>,
    productRequestModel: ProductRequestModel,
    productId: MutableState<Int>,
    products: MutableList<ProductModel>,
    product: ProductModel
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.createProduct(productRequestModel, "Bearer ".plus(jwtToken.value))
        onResultCreateProduct(res, ctx, productId, products, product, retrofitAPI, jwtToken)
    }
}

private fun onResultCreateProduct(
    result: Response<ProductResponseModel?>?,
    ctx: Context,
    productId: MutableState<Int>,
    products: MutableList<ProductModel>,
    product: ProductModel,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>
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
                jwtToken,
                ingredient
            )
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun addIngredientToProduct(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    ingredientInProductModel: IngredientInProductModel
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.addIngredientToProduct(
                IngredientInProductRequestModel(
                    ingredientInProductModel.ingredientId,
                    ingredientInProductModel.productId,
                    ingredientInProductModel.weight
                ), "Bearer ".plus(jwtToken.value)
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
    jwtToken: MutableState<String>,
    product: ProductModel,
    ingredientsResponse: MutableList<IngredientResponseModel>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.findAllIngredientsInProduct(product.id, "Bearer ".plus(jwtToken.value)
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
    jwtToken: MutableState<String>,
    product: ProductRequestModel,
    productId : Int
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.updateProduct(productId, product, "Bearer ".plus(jwtToken.value))
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