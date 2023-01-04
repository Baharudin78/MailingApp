package com.baharudin.mailingapp.domain.letter.entity

data class LetterKindEntity(
    val name : String,
    val nameAditional : String,
    var selected : Boolean = false
)
