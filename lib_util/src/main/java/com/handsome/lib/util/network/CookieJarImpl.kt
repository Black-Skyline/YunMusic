package com.handsome.lib.util.network

import com.handsome.lib.util.util.getSharePreference
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

object CookieJarImpl : CookieJar {
    private const val touristCookie = "MUSIC_A=bf8bfeabb1aa84f9c8c3906c04a04fb864322804c83f5d607e91a04eae463c9436bd1a17ec353cf7d4ce22d9c7155588429625b2f3034e9a993166e004087dd39c2fcc34fe98ae0db1aada678e071d8fc7650225309047105822cbe94f856496807e650dd04abd3fb8130b7ae43fcc5b; NMTID=00OPDkguXZjPfeMVEJKnBQKKu0kJScAAAGJY7_izA"

    //请求之前被调用，用于加载cookie
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return getCookies()
    }

    //请求之后调用，用于保存返回的新的cookie
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
    }

    private fun parseCookie(cookieString: String): List<Cookie> {
        val cookieList = mutableListOf<Cookie>()
        val cookies = cookieString.split(";").map { it.trim() }

        for (cookie in cookies) {
            val keyValue = cookie.split("=")
            val name = keyValue[0]
            val value = if (keyValue.size > 1) keyValue[1] else ""
            val cookieBuilder = Cookie.Builder()
                .domain("why.vin") // 设置你的域名
                .path("/")
                .name(name)
                .value(value)
                .httpOnly()
                .secure()
            cookieList.add(cookieBuilder.build())
        }
        return cookieList
    }

    private fun cookiesToString(cookies: List<Cookie>): String {
        val stringBuilder = StringBuilder()

        for (cookie in cookies) {
            stringBuilder.append(cookie.name)
            stringBuilder.append("=")
            stringBuilder.append(cookie.value)
            stringBuilder.append("; ")
        }

        return stringBuilder.toString().trimEnd(';')
    }

    private fun saveCookies(cookies: List<Cookie>){
        val sp = getSharePreference("cookie").edit()
        val cookieStr = cookiesToString(cookies)
        sp.putString("MyCookie",cookieStr)
        sp.apply()
    }

    private fun getCookies() : List<Cookie>{
        val sp = getSharePreference("cookie")
        val cookieStr = sp.getString("MyCookie", touristCookie)
        return parseCookie(cookieStr!!)
    }
}