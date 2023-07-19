package com.handsome.module.login.model

// 验证码的校验和获取都是回复这个
data class CaptchaResponseData(
    val code: Int,
    val `data`: Boolean
)