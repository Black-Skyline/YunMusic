package com.handsome.lib.mv.network.model

data class MvRecommendData(
    val code: Int,
    val count: Int,
    val `data`: List<Data>,
    val hasMore: Boolean
) {
    data class Data(
        val alias: List<String>,
        val artistId: Int,
        val artistName: String,
        val artists: List<Artist>,
        val briefDesc: String,
        val cover: String?,
        val desc: Any,
        val duration: Int,
        val id: Long,
        val mark: Int,
        val name: String,
        val playCount: Int,
        val subed: Boolean,
        val transNames: List<String>
    ) {
        data class Artist(
            val alias: List<String>,
            val id: Int,
            val name: String,
            val transNames: List<String>
        )
    }
}