package com.baharudin.mailingapp.domain.letter.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class LetterEntity(
    val imagesLetterUrl: String? = null,
    val letterDate: String? = null,
    val letterDestination: String? = null,
    val letterDiscription: String? = null,
    val letterKinds: String? = null,
    val letterNumber: Int? = null,
    val senderIdentity: String? = null
) : Parcelable
