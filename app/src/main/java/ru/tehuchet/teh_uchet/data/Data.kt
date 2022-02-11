package ru.tehuchet.teh_uchet.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    val id: Long = 0L,
    @SerializedName("name") val fio: String = "",
    val address: String = "",
    val phone: String = "",
    val mobile: String = "",
    val kross: Int = 1,
    val magistral: Int = 1,
    val raspred: Int = 1,
    val adsl: Int = 1,
    val boxes: List<String> = listOf(""),
    @SerializedName("latitude") val lat: Double = 37.586452,
    @SerializedName("longitude") val long: Double = 54.166907
) : Serializable

data class NewUser(
    @SerializedName("name") val fio: String = "",
    val address: String = "",
    val phone: String = "",
    val mobile: String = "",
    val kross: Int = 1,
    val magistral: Int = 1,
    val raspred: Int = 1,
    val adsl: Int = 1,
    val boxes: List<String> = listOf(""),
    @SerializedName("latitude") val lat: Double = 37.586452,
    @SerializedName("longitude") val long: Double = 54.166907
) : Serializable

fun User.toNewUser(): NewUser =
    NewUser(fio, address, phone, mobile, kross, magistral, raspred, adsl, boxes, lat, long)

fun User.withId(newId: Long): User =
    User(newId, fio, address, phone, mobile, kross, magistral, raspred, adsl, boxes, lat, long)
