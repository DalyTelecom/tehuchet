package ru.tehuchet.teh_uchet.net

import retrofit2.Call
import retrofit2.http.*
import ru.tehuchet.teh_uchet.data.NewUser
import ru.tehuchet.teh_uchet.data.User

data class IdResponseData(val id: Int)

data class UsersResponseData(val users: List<User>)

data class SuccessResponse(val success: Boolean)

val BASE_URL = "http://daly-telecom.cf"

interface ServerApi {
    @GET("/api/users")
    fun getPeople(): Call<UsersResponseData>

    @POST("/api/users")
    fun createUser(@Body user: NewUser): Call<IdResponseData>

    @PUT("/api/users/{id}")
    fun updateUser(@Path("id") id: Long, @Body user: User): Call<SuccessResponse>

    @DELETE("/api/users/{id}")
    fun deleteUser(@Path("id") id: Long): Call<SuccessResponse>
}