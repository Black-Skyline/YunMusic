package com.handsome.module.login.model

data class AnonymousData(
    val code: Int,       // 200
    val cookie: String,
    val createTime: Long,
    val userId: Long
)