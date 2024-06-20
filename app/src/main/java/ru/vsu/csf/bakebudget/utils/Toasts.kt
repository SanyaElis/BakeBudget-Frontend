package ru.vsu.csf.bakebudget.utils

import android.content.Context
import android.widget.Toast

private var toast: Toast? = null

fun dataIncorrectToast(context: Context) {
    if (toast != null) {
        toast!!.cancel();
    }
    toast = Toast.makeText(
        context,
        "Некорректные данные" + "\n" + "Название должно быть не короче 2 символов" + "\n" + "Вес от 1 до 100000" + "\n" + "Стоимость от 0 до 1000000",
        Toast.LENGTH_LONG
    )
    toast!!.show()
}

fun dataIncorrectToastProduct(context: Context) {
    if (toast != null) {
        toast!!.cancel();
    }
    toast = Toast.makeText(
        context,
        "Некорректные данные" + "\n" + "Название должно быть не короче 2 символов" + "\n" + "Вес от 1 до 100000",
        Toast.LENGTH_LONG
    )
    toast!!.show()
}

fun dataIncorrectToastOutgoing(context: Context) {
    if (toast != null) {
        toast!!.cancel();
    }
    toast = Toast.makeText(
        context,
        "Некорректные данные" + "\n" + "Название должно быть не короче 2 символов" + "\n" + "Стоимость от 0 до 1000000",
        Toast.LENGTH_LONG
    )
    toast!!.show()
}
fun sameOrder(context: Context) {
    Toast.makeText(
        context,
        "Вы недавно добавляли такой же заказ. Уверены, что хотите создать еще один? Если да, то нажмите еще раз",
        Toast.LENGTH_LONG
    ).show()
}

fun sameName(context: Context) {
    if (toast != null) {
        toast!!.cancel();
    }
    toast = Toast.makeText(
        context,
        "Все имена должны быть уникальными!",
        Toast.LENGTH_LONG
    )
    toast!!.show()
}

fun sameNameProduct(context: Context) {
    if (toast != null) {
        toast!!.cancel();
    }
    toast = Toast.makeText(
        context,
        "Продукт с таким названием уже существует!",
        Toast.LENGTH_LONG
    )
    toast!!.show()
}

fun successfulProduct(context: Context) {
    if (toast != null) {
        toast!!.cancel();
    }
    toast = Toast.makeText(
        context,
        "Продукт успешно создан!",
        Toast.LENGTH_LONG
    )
    toast!!.show()
}

fun successfulProductEdit(context: Context) {
    if (toast != null) {
        toast!!.cancel();
    }
    toast = Toast.makeText(
        context,
        "Продукт успешно изменен!",
        Toast.LENGTH_LONG
    )
    toast!!.show()
}
fun codeAlreadyGenerated(context: Context) {
    if (toast != null) {
        toast!!.cancel();
    }
    toast = Toast.makeText(
        context,
        "Код уже сгенерирован",
        Toast.LENGTH_LONG
    )
    toast!!.show()
}

fun linkApproveFailed(context: Context) {
    if (toast != null) {
        toast!!.cancel();
    }
    toast = Toast.makeText(
        context,
        "Вы не перешли по ссылке на почте!",
        Toast.LENGTH_LONG
    )
    toast!!.show()
}

fun codeCopied(context: Context) {
    if (toast != null) {
        toast!!.cancel();
    }
    toast = Toast.makeText(
        context,
        "Код скопирован",
        Toast.LENGTH_LONG
    )
    toast!!.show()
}

fun orderCreated(context: Context) {
    if (toast != null) {
        toast!!.cancel();
    }
    toast = Toast.makeText(
        context,
        "Заказ создан",
        Toast.LENGTH_LONG
    )
    toast!!.show()
}