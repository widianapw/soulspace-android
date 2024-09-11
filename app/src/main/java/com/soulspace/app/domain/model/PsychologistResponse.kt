package com.soulspace.app.domain.model

import com.google.gson.annotations.SerializedName

data class PsychologistResponse(
    val data: List<PsychologistItem>
)

data class PsychologistItem(
    val address: String,
    val email: String,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String
)