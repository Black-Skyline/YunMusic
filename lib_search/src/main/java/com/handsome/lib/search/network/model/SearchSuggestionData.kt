package com.handsome.lib.search.network.model

data class SearchSuggestionData(
    val code: Int,
    val result: Result
) {
    data class Result(
        val allMatch: List<AllMatch>
    ) {
        data class AllMatch(
            val alg: String,
            val feature: String,
            val keyword: String,
            val lastKeyword: String,
            val type: Int
        )
    }
}