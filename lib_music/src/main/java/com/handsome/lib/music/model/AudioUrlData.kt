package com.handsome.lib.music.model

data class AudioUrlData(
    val code: Int,
    val `data`: List<Data>
) {
    data class Data(
        val br: Int,
        val canExtend: Boolean,
        val code: Int,
        val effectTypes: Any,
        val encodeType: String,
        val expi: Int,
        val fee: Int,
        val flag: Int,
        val freeTimeTrialPrivilege: FreeTimeTrialPrivilege,
        val freeTrialInfo: Any,
        val freeTrialPrivilege: FreeTrialPrivilege,
        val gain: Double,
        val id: Long,          // 音乐id
        val level: String,     // 音质等级
        val md5: String,
        val payed: Int,
        val peak: Double,
        val podcastCtrp: Any,
        val rightSource: Int,
        val size: Int,
        val time: Int,
        val type: String,
        val uf: Any,
        val url: String,     // 播放地址
        val urlSource: Int
    ) {
        data class FreeTimeTrialPrivilege(
            val remainTime: Int,
            val resConsumable: Boolean,
            val type: Int,
            val userConsumable: Boolean
        )

        data class FreeTrialPrivilege(
            val cannotListenReason: Any,
            val listenType: Any,
            val resConsumable: Boolean,
            val userConsumable: Boolean
        )
    }
}