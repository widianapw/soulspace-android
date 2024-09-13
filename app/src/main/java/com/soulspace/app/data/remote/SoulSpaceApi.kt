package com.soulspace.app.data.remote

import com.soulspace.app.domain.model.AuthResponse
import com.soulspace.app.domain.model.ChatResponse
import com.soulspace.app.domain.model.PostChatResponse
import com.soulspace.app.domain.model.PsychologistResponse
import com.soulspace.app.domain.model.ResetChatResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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
    suspend fun getPsychologists(): PsychologistResponse

    @GET("chat")
    suspend fun getChat(): ChatResponse

    @FormUrlEncoded
    @POST("chat")
    suspend fun sendMessage(
        @Field("message") message: String
    ): PostChatResponse


    @POST("chat/reset")
    suspend fun resetChat(): ResetChatResponse
}