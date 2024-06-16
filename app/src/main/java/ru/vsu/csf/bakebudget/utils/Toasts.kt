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

fun sameOrder(context: Context) {
    Toast.makeText(
        context,
        "Вы недавно добавляли такой же заказ. Уверены, что хотите создать еще один? Если да, то нажмите еще раз",
        Toast.LENGTH_LONG
    ).show()
}

fun sameName(context: Context) {
    Toast.makeText(
        context,
        "Все имена должны быть уникальными!",
        Toast.LENGTH_LONG
    ).show()
}

fun sameNameProduct(context: Context) {
    Toast.makeText(
        context,
        "Продукт с таким названием уже существует!",
        Toast.LENGTH_LONG
    ).show()
}

fun successfulProduct(context: Context) {
    Toast.makeText(
        context,
        "Продукт успешно создан!",
        Toast.LENGTH_LONG
    ).show()
}

fun successfulProductEdit(context: Context) {
    Toast.makeText(
        context,
        "Продукт успешно изменен!",
        Toast.LENGTH_LONG
    ).show()
}
fun codeAlreadyGenerated(context: Context) {
    Toast.makeText(
        context,
        "Код уже сгенерирован",
        Toast.LENGTH_LONG
    ).show()
}

fun linkApproveFailed(context: Context) {
    Toast.makeText(
        context,
        "Вы не перешли по ссылке на почте!",
        Toast.LENGTH_LONG
    ).show()
}

fun codeCopied(context: Context) {
    Toast.makeText(
        context,
        "Код скопирован",
        Toast.LENGTH_LONG
    ).show()
}

fun orderCreated(context: Context) {
    Toast.makeText(
        context,
        "Заказ создан",
        Toast.LENGTH_LONG
    ).show()
}