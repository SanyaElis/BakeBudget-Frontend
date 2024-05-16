package ru.vsu.csf.bakebudget.utils

import android.content.Context
import android.widget.Toast

fun dataIncorrectToast(context: Context) {
    Toast.makeText(
        context,
        "Некорректные данные" + "\n" + "Название должно быть не короче 2 символов" + "\n" + "Вес от 1 до 100000" + "\n" + "Стоимость от 0 до 1000000",
        Toast.LENGTH_LONG
    ).show()
}