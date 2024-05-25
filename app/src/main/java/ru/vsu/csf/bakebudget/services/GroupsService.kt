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
import ru.vsu.csf.bakebudget.getToken

@OptIn(DelicateCoroutinesApi::class)
fun changeRole(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    isPro: MutableState<Boolean>,
    userRole: MutableState<String>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.changeRole("Bearer ".plus(getToken(ctx)))
        onResultChangeRole(res, ctx, isPro, userRole)
    }
}

private fun onResultChangeRole(
    result: Response<Void>?,
    context: Context,
    isPro: MutableState<Boolean>,
    userRole: MutableState<String>
) {
    if (result != null) {
        if (result.isSuccessful) {
            Toast.makeText(context, "Response Code : " + result.code() + "\n" + "Role changed",
                Toast.LENGTH_SHORT
            ).show()
            isPro.value = userRole.value == "ROLE_ADVANCED_USER"
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun createCode(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    code : MutableState<String>,
    userRole: MutableState<String>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.createCode("Bearer ".plus(getToken(ctx)))
        onResultCreateCode(res, ctx, code, userRole)
    }
}

private fun onResultCreateCode(
    result: Response<String>?,
    context: Context,
    code : MutableState<String>,
    userRole: MutableState<String>
) {
    if (result != null) {
        if (result.isSuccessful) {
            Toast.makeText(context, "Code : " + result.body(),
                Toast.LENGTH_SHORT
            ).show()
            code.value = result.body()!!
            userRole.value = "ROLE_ADVANCED_USER"
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun getCode(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    code : MutableState<String>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.getCode("Bearer ".plus(getToken(ctx)))
        onResultGetCode(res, ctx, code)
    }
}

private fun onResultGetCode(
    result: Response<String>?,
    context: Context,
    code : MutableState<String>
) {
    if (result != null) {
        if (result.isSuccessful) {
            Toast.makeText(context, "Code : " + result.body(),
                Toast.LENGTH_SHORT
            ).show()
            code.value = result.body()!!
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun setCode(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    code : MutableState<String>,
    generatedCode : MutableState<String>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.setCode(code.value, "Bearer ".plus(getToken(ctx)))
        onResultSetCode(res, ctx, code, generatedCode)
    }
}

private fun onResultSetCode(
    result: Response<String>?,
    context: Context,
    code : MutableState<String>,
    generatedCode : MutableState<String>
) {
    if (result != null) {
        if (result.isSuccessful) {
            Toast.makeText(context, "Code : " + result.body(),
                Toast.LENGTH_SHORT
            ).show()
            generatedCode.value = result.body()!!
        } else {
            Toast.makeText(context, "Такой группы не существует или вы уже состоите в группе",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}