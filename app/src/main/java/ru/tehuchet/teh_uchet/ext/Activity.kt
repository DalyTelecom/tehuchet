package ru.tehuchet.teh_uchet.ext

import android.app.Activity

fun Activity.performLogin(login: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
    val correctPassword = when (login) {
        "daly" -> {
            "89207449792"
        }
        "leonov" -> {
            "89202707771"
        }

        "novikov" -> {
            "89029033765"
        }
        "sazonov" -> {
            "89509267026"
        }
        "larin" -> {
            "89914146972"
        }
        "zuev" -> {
            "89539563174"
        }
        "artamonov" -> {
            "89914146944"
        }
        "smirnov" -> {
            "89585705469"
        }
        else -> "1488"
    }

    if (password == correctPassword) {
        onSuccess()
    } else {
        onError("Некорректный логин или пароль")
    }
}