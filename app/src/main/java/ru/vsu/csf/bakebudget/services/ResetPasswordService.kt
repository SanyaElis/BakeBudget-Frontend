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
import ru.vsu.csf.bakebudget.models.request.PasswordResetRequestModel
import ru.vsu.csf.bakebudget.utils.linkApproveFailed

private var toast: Toast? = null

@OptIn(DelicateCoroutinesApi::class)
fun forgotPassword(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    email: String
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.forgotPassword(email)
        onResultForgotPassword(ctx, res)
    }
}

private fun onResultForgotPassword(
    ctx: Context,
    result: Response<Void>?
) {
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + "Deleted ingredient: " + "\n" + ingredient,
//        Toast.LENGTH_SHORT
//    ).show()
    val eventParameters1 = "{\"button_clicked\":\"reset password\"}"
    AppMetrica.reportEvent(
        "Letter sent to email",
        eventParameters1
    )
}

@OptIn(DelicateCoroutinesApi::class)
fun resetPassword(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    email: String,
    password: String,
    changed: MutableState<Boolean>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.resetPassword(PasswordResetRequestModel(email, password))
        onResultResetPassword(ctx, res, changed)
    }
}

private fun onResultResetPassword(
    ctx: Context,
    result: Response<Void>?,
    changed: MutableState<Boolean>
) {
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + "Deleted ingredient: " + "\n" + ingredient,
//        Toast.LENGTH_SHORT
//    ).show()
    if (!result!!.isSuccessful) {
        linkApproveFailed(ctx)
    } else {
        val eventParameters1 = "{\"password_reset\":\"password reset\"}"
        AppMetrica.reportEvent(
            "Password changed",
            eventParameters1
        )
        Toast.makeText(
        ctx,
        "Пароль успешно изменен",
        Toast.LENGTH_SHORT
    ).show()
        changed.value = true
    }
}