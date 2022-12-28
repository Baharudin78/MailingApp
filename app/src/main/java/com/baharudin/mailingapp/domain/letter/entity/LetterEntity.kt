package com.baharudin.mailingapp.domain.letter.entity

data class LetterEntity(
    val v: Int,
    val id: String,
    val createdAt: String,
    val imagesLetterUrl: String,
    val letterDate: String,
    val letterDestination: String,
    val letterDiscription: String,
    val letterKinds: String,
    val letterNumber: Int,
    val senderIdentity: String
)
