package com.handsome.module.podcast.model

data class ProgramAudioData(
    val code: Int,
    val `data`: List<Data>
) {
    data class Data(
        val br: Int,
        val canExtend: Boolean,
        val code: Int,
        val effectTypes: Any,
        val encodeType: Any,
        val expi: Int,
        val fee: Int,
        val flag: Int,
        val freeTimeTrialPrivilege: FreeTimeTrialPrivilege,
        val freeTrialInfo: Any,
        val freeTrialPrivilege: Any,
        val gain: Double,
        val id: Int,
        val level: Any,
        val md5: String,
        val payed: Int,
        val peak: Int,
        val podcastCtrp: String,
        val rightSource: Int,
        val size: Int,
        val time: Int,
        val type: String,
        val uf: Any,
        val url: String?,         // 音频文件的url，如果为空，则查不到
        val urlSource: Int
    ) {
        data class FreeTimeTrialPrivilege(
            val remainTime: Int,
            val resConsumable: Boolean,
            val type: Int,
            val userConsumable: Boolean
        )
    }
}