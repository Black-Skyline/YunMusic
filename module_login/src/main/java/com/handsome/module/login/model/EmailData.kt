package com.handsome.module.login.model

data class EmailData(
    val code: Int,
    val message: String,
    val redirectUrl: String
)