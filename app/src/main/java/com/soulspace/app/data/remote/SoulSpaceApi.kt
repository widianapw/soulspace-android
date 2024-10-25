package com.soulspace.app.data.remote

import com.soulspace.app.domain.model.AuthResponse
import com.soulspace.app.domain.model.ChatResponse
import com.soulspace.app.domain.model.NavigationItem
import com.soulspace.app.domain.model.PostChatResponse
import com.soulspace.app.domain.model.PsychologistResponse
import com.soulspace.app.domain.model.ResetChatResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SoulSpaceApi {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @GET("psychologist")
    suspend fun getPsychologists(
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null
    ): PsychologistResponse

    @GET("chat")
    suspend fun getChat(): ChatResponse

    @FormUrlEncoded
    @POST("chat")
    suspend fun sendMessage(
        @Field("message") message: String
    ): PostChatResponse


    @POST("chat/reset")
    suspend fun resetChat(): ResetChatResponse

    @GET("psychologist/route")
    suspend fun getPsychologistRoute(
        @Query("fromLatitude") fromLatitude: Double? = null,
        @Query("fromLongitude") fromLongitude: Double? = null,
        @Query("toLatitude") toLatitude: Double? = null,
        @Query("toLongitude") toLongitude: Double? = null
    ): List<NavigationItem>
}