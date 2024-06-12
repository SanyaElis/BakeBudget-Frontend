package ru.vsu.csf.bakebudget.services

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.compose.runtime.MutableState
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSink
import okio.source
import retrofit2.Response
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.getToken
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.request.IngredientInProductRequestModel
import ru.vsu.csf.bakebudget.models.request.ProductRequestModel
import ru.vsu.csf.bakebudget.models.response.ImageResponseModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.utils.sameName
import java.io.File


@OptIn(DelicateCoroutinesApi::class)
fun findAllProducts(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    productsResponse: MutableList<ProductResponseModel>,
    orders: MutableList<OrderModel>,
    isDataReceivedOrders : MutableState<Boolean>,
    productsAll: MutableList<ProductModel>,
    orders0: MutableList<OrderModel>, orders1: MutableList<OrderModel>, orders2: MutableList<OrderModel>, orders3: MutableList<OrderModel>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.findAllProducts("Bearer ".plus(getToken(ctx)))
        onResultFindAll(res, productsResponse, ctx, retrofitAPI, orders, isDataReceivedOrders, productsAll, orders0, orders1, orders2, orders3)
    }
}

private fun onResultFindAll(
    result: Response<List<ProductResponseModel>?>?,
    productsResponse: MutableList<ProductResponseModel>,
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    orders: MutableList<OrderModel>,
    isDataReceivedOrders : MutableState<Boolean>,
    productsAll: MutableList<ProductModel>,
    orders0: MutableList<OrderModel>, orders1: MutableList<OrderModel>, orders2: MutableList<OrderModel>, orders3: MutableList<OrderModel>
) {
    if (result!!.body() != null) {
        if (result.body()!!.isNotEmpty()) {
            for (prod in result.body()!!) {
                productsResponse.add(prod)
            }
        }
        findAllOrders(ctx, retrofitAPI, orders, productsAll, orders0, orders1, orders2, orders3, isDataReceivedOrders)
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
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
    if (result!!.body() != null) {
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
            uploadPicture(ctx, retrofitAPI, product, selectedImageUri.value!!)
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
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
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
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
    if (result!!.body() != null) {
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
    productId : Int,
    selectedImageUri: MutableState<Uri?>,
    product1: ProductModel
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.updateProduct(productId, product, "Bearer ".plus(getToken(ctx)))
        onResultUpdateProduct(res, ctx, selectedImageUri, retrofitAPI, product1)
    }
}

private fun onResultUpdateProduct(
    result: Response<Void>?,
    ctx: Context,
    selectedImageUri: MutableState<Uri?>,
    retrofitAPI: RetrofitAPI,
    product: ProductModel
) {
    if (selectedImageUri.value != null) {
        if (product.url != null) {
            updatePicture(ctx, retrofitAPI, product, selectedImageUri.value!!)
        } else {
            uploadPicture(ctx, retrofitAPI, product, selectedImageUri.value!!)
        }
    }
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
}

//TODO:same product add message

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
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
    if (result!!.code() == 409) {
        sameName(ctx)
    } else {
        val eventParameters2 = "{\"button_clicked\":\"update ingredient in product\"}"
        AppMetrica.reportEvent(
            "Ingredient in product updated",
            eventParameters2
        )
    }
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
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
    val eventParameters1 = "{\"button_clicked\":\"delete ingredient in product\"}"
    AppMetrica.reportEvent(
        "Ingredient in product deleted",
        eventParameters1
    )
}


//fun bitmapToString(bitmap: Bitmap): String = runBlocking {
//    return@runBlocking withContext(Dispatchers.Default) {
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
//        val encodedString: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
//        return@withContext encodedString
//    }
//}
//fun uriToString(context: Context, imageUri: Uri?): String? {
//    var inputStream: InputStream? = null
//    var bitmap: Bitmap? = null
//    var string: String? =null
//
//    try {
//        inputStream = imageUri?.let { context.contentResolver.openInputStream(it) }
//        bitmap = BitmapFactory.decodeStream(inputStream)
//        string = bitmapToString(bitmap)
//    } finally {
//        inputStream?.close()
//    }
//
//    return string
//}
//fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
//    val bytes = ByteArrayOutputStream()
//    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//    val path =
//        MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
//    return Uri.parse(path)
//}

@OptIn(DelicateCoroutinesApi::class)
fun uploadPicture(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    product: ProductModel,
    uri: Uri
) {
    GlobalScope.launch(Dispatchers.Main) {
//        val imageBytes = Base64.decode(uriToString(ctx, uri)!!, 0)
//        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//        product.uri = getImageUri(ctx, image)
        val file = File(uri.path!!)
//        val ext =  uri.toString().substring(uri.toString().lastIndexOf(".") + 1)
        val requestBody = InputStreamRequestBody(ctx.contentResolver, uri)
        val filePart = MultipartBody.Part.createFormData("file", "jpg", requestBody)
        val res =
            retrofitAPI.uploadPicture(
                product.id,
                filePart
                ,
//                uriToString(ctx, uri)!!,
                "Bearer ".plus(getToken(ctx))
            )
        onResultUploadPicture(res, ctx, product, uri, retrofitAPI)
    }
}

class InputStreamRequestBody(
    private val contentResolver: ContentResolver,
    private val uri: Uri
): RequestBody() {

    override fun contentType(): MediaType? =
        contentResolver.getType(uri)?.toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        contentResolver.openInputStream(uri)?.source()?.use(sink::writeAll)
    }

    override fun contentLength(): Long =
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val sizeColumnIndex: Int = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            cursor.getLong(sizeColumnIndex)
        } ?: super.contentLength()

}

private fun onResultUploadPicture(
    result: Response<Void>?,
    ctx: Context,
    product: ProductModel,
    uri: Uri,
    retrofitAPI: RetrofitAPI
) {
    getPicture(ctx, retrofitAPI, product)
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
    product.uri = uri
}

@OptIn(DelicateCoroutinesApi::class)
fun getPicture(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    product: ProductModel
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.getPicture(
                product.id,
                "Bearer ".plus(getToken(ctx))
            )
        onResultGetPicture(res, ctx, product)
    }
}

private fun onResultGetPicture(
    result: Response<ImageResponseModel>?,
    ctx: Context,
    product: ProductModel
) {
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
    if (result!!.body() != null) {
        product.url = result.body()!!.link
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun reloadPicture(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    product: ProductModel,
    uri: Uri
) {
    GlobalScope.launch(Dispatchers.Main) {
//        val imageBytes = Base64.decode(uriToString(ctx, uri)!!, 0)
//        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//        product.uri = getImageUri(ctx, image)
        val file = File(uri.path!!)
//        val ext =  uri.toString().substring(uri.toString().lastIndexOf(".") + 1)
        val requestBody = InputStreamRequestBody(ctx.contentResolver, uri)
        val filePart = MultipartBody.Part.createFormData("file", "jpg", requestBody)
        val res =
            retrofitAPI.reloadPicture(
                product.id,
                filePart
                ,
//                uriToString(ctx, uri)!!,
                "Bearer ".plus(getToken(ctx))
            )
        onResultReloadPicture(res, ctx, product, uri, retrofitAPI)
    }
}
private fun onResultReloadPicture(
    result: Response<Void>?,
    ctx: Context,
    product: ProductModel,
    uri: Uri,
    retrofitAPI: RetrofitAPI
) {
    getPicture(ctx, retrofitAPI, product)
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
    product.uri = uri
}

@OptIn(DelicateCoroutinesApi::class)
fun updatePicture(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    product: ProductModel,
    uri: Uri
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.deletePicture(
                product.id,
                "Bearer ".plus(getToken(ctx))
            )
        onResultUpdatePicture(res, ctx, product, uri, retrofitAPI)
    }
}
private fun onResultUpdatePicture(
    result: Response<Void>?,
    ctx: Context,
    product: ProductModel,
    uri: Uri,
    retrofitAPI: RetrofitAPI
) {
    uploadPicture(ctx, retrofitAPI, product, uri)
}

