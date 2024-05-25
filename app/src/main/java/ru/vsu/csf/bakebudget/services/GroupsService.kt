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

@OptIn(DelicateCoroutinesApi::class)
fun changeRole(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    isPro: MutableState<Boolean>,
    userRole: MutableState<String>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.changeRole("Bearer ".plus(jwtToken.value))
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
    jwtToken: MutableState<String>,
    code : MutableState<String>,
    userRole: MutableState<String>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.createCode("Bearer ".plus(jwtToken.value))
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
    jwtToken: MutableState<String>,
    code : MutableState<String>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.getCode("Bearer ".plus(jwtToken.value))
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
    jwtToken: MutableState<String>,
    code : MutableState<String>,
    generatedCode : MutableState<String>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.setCode(code.value, "Bearer ".plus(jwtToken.value))
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