package ru.vsu.csf.bakebudget.services

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.getToken
import ru.vsu.csf.bakebudget.models.request.IngredientRequestModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel

@OptIn(DelicateCoroutinesApi::class)
fun findAllIngredients(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    ingredientsResponse: MutableList<IngredientResponseModel>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val token = getToken(ctx)
        val res = retrofitAPI.findAllIngredients("Bearer ".plus(token))
        onResultFindAll(res, ingredientsResponse)
    }
}

private fun onResultFindAll(
    result: Response<List<IngredientResponseModel>?>?,
    ingredientsResponse: MutableList<IngredientResponseModel>
) {
    if (result!!.body() != null) {
        if (result.body()!!.isNotEmpty()) {
            for (ing in result.body()!!) {
                ingredientsResponse.add(ing)
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun createIngredient(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    ingredientRequestModel: IngredientRequestModel,
    ingredientsResponse: MutableList<IngredientResponseModel>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.createIngredient(ingredientRequestModel, "Bearer ".plus(getToken(ctx)))
        onResultCreateIngredient(res, ctx, ingredientsResponse)
    }
}

private fun onResultCreateIngredient(
    result: Response<IngredientResponseModel?>?,
    ctx: Context,
    ingredientsResponse: MutableList<IngredientResponseModel>
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    if (result.isSuccessful) {
        if (result.body() != null) {
            ingredientsResponse.add(result.body()!!)
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun updateIngredient(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    ingredient: IngredientResponseModel,
    ingredientRequestModel: IngredientRequestModel
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.updateIngredient(ingredient.id, ingredientRequestModel, "Bearer ".plus(getToken(ctx)))
        onResultUpdate(res, ctx)
    }
}

private fun onResultUpdate(
    result: Response<IngredientResponseModel?>?,
    ctx: Context
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    val eventParameters2 = "{\"button_clicked\":\"uodate ingredient\"}"
    AppMetrica.reportEvent(
        "ingredient updated",
        eventParameters2
    )
}

@OptIn(DelicateCoroutinesApi::class)
fun deleteIngredient(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    ingredient: IngredientResponseModel
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.deleteIngredient(ingredient.id, "Bearer ".plus(getToken(ctx)))
        onResultDelete(ingredient, ctx, res)
    }
}

private fun onResultDelete(
    ingredient: IngredientResponseModel,
    ctx: Context,
    result: Response<Void>?
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + "Deleted ingredient: " + "\n" + ingredient,
        Toast.LENGTH_SHORT
    ).show()
    val eventParameters1 = "{\"button_clicked\":\"delete ingredient\"}"
    AppMetrica.reportEvent(
        "Ingredient deleted",
        eventParameters1
    )
}