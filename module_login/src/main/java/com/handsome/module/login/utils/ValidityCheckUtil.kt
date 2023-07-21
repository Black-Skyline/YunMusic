package com.handsome.module.login.utils

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/19
 * @Description:
 *
 */
object ValidityCheckUtil {
    fun isValidPhoneNumber(input: String?): Boolean {
        if (input.isNullOrBlank())
            return false
        val pattern = Regex("^1[3456789]\\d{9}$")
        return pattern.matches(input)
    }
    fun isValidCaptcha(input: String?): Boolean {
        if (input.isNullOrBlank())
            return false
        val pattern = Regex("^\\d{4}$")
        return pattern.matches(input)
    }
    fun isValidEmail(input: String?): Boolean {
        if (input.isNullOrBlank())
            return false
        val pattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return pattern.matches(input)
    }
}