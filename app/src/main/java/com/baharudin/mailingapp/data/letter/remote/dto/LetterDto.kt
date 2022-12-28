package com.baharudin.mailingapp.data.letter.remote.dto

import com.google.gson.annotations.SerializedName

data class LetterDto(
    @SerializedName("__v")val code: Int,
    @SerializedName("_id")val id: String,
    @SerializedName("createdAt")val createdAt: String,
    @SerializedName("images_letter_url")val imagesLetterUrl: String,
    @SerializedName("letter_date")val letterDate: String,
    @SerializedName("letter_destination")val letterDestination: String,
    @SerializedName("letter_discription")val letterDiscription: String,
    @SerializedName("letter_kinds")val letterKinds: String,
    @SerializedName("letter_number")val letterNumber: Int,
    @SerializedName("sender_identity")val senderIdentity: String
)
