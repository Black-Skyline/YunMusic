package com.handsome.module.find.network.model

data class BannerBelowData(
    val code: Int,
    val `data`: List<Data>,
    val message: String
) {
    data class Data(
        val homepageMode: String,
        val iconUrl: String,
        val id: Int,
        val name: String,
        val resourceState: Any,
        val skinSupport: Boolean,
        val url: String
    )
}