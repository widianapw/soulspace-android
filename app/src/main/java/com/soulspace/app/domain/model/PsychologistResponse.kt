package com.soulspace.app.domain.model

import com.google.gson.annotations.SerializedName

data class PsychologistResponse(
    val data: List<PsychologistItem>
)

data class PsychologistItem(
    val address: String,
    val email: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("image_url")
    val imageUrl: String?
)