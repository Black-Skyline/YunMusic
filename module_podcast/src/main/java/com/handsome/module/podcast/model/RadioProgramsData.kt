package com.handsome.module.podcast.model

data class RadioProgramsData(
    val code: Int,
    val count: Int,
    val more: Boolean,
    val programs: List<Program>
) {
    data class Program(
        val adIconInfo: Any,
        val additionIconList: Any,
        val alg: Any,
        val auditDisPlayStatus: Int,
        val auditStatus: Int,
        val authDTO: Any,
        val bdAuditStatus: Int,
        val blurCoverUrl: String,
        val buyed: Boolean,
        val canReward: Boolean,
        val categoryId: Int,
        val categoryName: Any,
        val channels: List<Any>,
        val commentCount: Int,
        val commentThreadId: String,
        val coverId: Long,
        val coverUrl: String,
        val createEventId: Int,
        val createTime: Long,
        val description: String,
        val disPlayStatus: Any,
        val dj: Dj,
        val djPlayRecordVo: Any,
        val duration: Int,
        val existLyric: Boolean,
        val feeScope: Int,
        val h5Links: Any,
        val icon: Any,
        val id: Long,
        val isPublish: Boolean,
        val likedCount: Int,
        val listenerCount: Int,
        val liveInfo: Any,
        val mainSong: MainSong,
        val mainTrackId: Int,
        val name: String,
        val privacy: Boolean,
        val programDesc: Any,
        val programFeeType: Int,
        val pubStatus: Int,
        val radio: Radio,
        val recommended: Boolean,
        val replaceResource: Any,
        val replaceVoiceId: Int,
        val reward: Boolean,
        val scheduledPublishTime: Long,
        val score: Int,
        val secondCategoryId: Int,
        val secondCategoryName: Any,
        val serialNum: Int,
        val shareCount: Int,
        val smallLanguageAuditStatus: Int,
        val songs: Any,
        val subscribed: Boolean,
        val subscribedCount: Int,
        val titbitImages: Any,
        val titbits: Any,
        val trackCount: Int,
        val videoInfo: Any
    ) {
        data class Dj(
            val accountStatus: Int,
            val anchor: Boolean,
            val authStatus: Int,
            val authenticationTypes: Int,
            val authority: Int,
            val avatarDetail: Any,
            val avatarImgId: Long,
            val avatarImgIdStr: String,
            val avatarImgId_str: String,
            val avatarUrl: String,
            val backgroundImgId: Long,
            val backgroundImgIdStr: String,
            val backgroundUrl: String,
            val birthday: Int,
            val brand: String,
            val city: Int,
            val defaultAvatar: Boolean,
            val description: String,
            val detailDescription: String,
            val djStatus: Int,
            val expertTags: Any,
            val experts: Any,
            val followed: Boolean,
            val gender: Int,
            val mutual: Boolean,
            val nickname: String,
            val province: Int,
            val remarkName: Any,
            val signature: String,
            val userId: Long,
            val userType: Int,
            val vipType: Int
        )

        data class MainSong(
            val album: Album,
            val alias: List<Any>,
            val artists: List<Artist>,
            val audition: Any,
            val bMusic: BMusic,
            val commentThreadId: String,
            val copyFrom: String,
            val copyright: Int,
            val copyrightId: Int,
            val crbt: Any,
            val dayPlays: Int,
            val disc: String,
            val duration: Int,
            val fee: Int,
            val ftype: Int,
            val hMusic: HMusic,
            val hearTime: Int,
            val id: Int,         // audio id
            val lMusic: LMusic,
            val mMusic: Any,
            val mark: Int,
            val mp3Url: Any,
            val mvid: Int,
            val name: String,
            val no: Int,
            val noCopyrightRcmd: Any,
            val playedNum: Int,
            val popularity: Int,
            val position: Int,
            val ringtone: String,
            val rtUrl: Any,
            val rtUrls: List<Any>,
            val rtype: Int,
            val rurl: Any,
            val score: Int,
            val sign: Any,
            val starred: Boolean,
            val starredNum: Int,
            val status: Int,
            val transName: Any
        ) {
            data class Album(
                val alias: List<Any>,
                val artist: Artist,
                val artists: List<Artist>,
                val blurPicUrl: Any,
                val briefDesc: String,
                val commentThreadId: String,
                val company: Any,
                val companyId: Int,
                val copyrightId: Int,
                val description: String,
                val id: Int,
                val mark: Int,
                val name: String,
                val pic: Int,
                val picId: Int,
                val picUrl: String,
                val publishTime: Int,
                val size: Int,
                val songs: List<Any>,
                val status: Int,
                val subType: Any,
                val tags: String,
                val transName: Any,
                val type: Any
            ) {
                data class Artist(
                    val albumSize: Int,
                    val alias: List<Any>,
                    val briefDesc: String,
                    val id: Int,
                    val img1v1Id: Int,
                    val img1v1Url: String,
                    val musicSize: Int,
                    val name: String,
                    val picId: Int,
                    val picUrl: String,
                    val topicPerson: Int,
                    val trans: String
                )
            }

            data class Artist(
                val albumSize: Int,
                val alias: List<Any>,
                val briefDesc: String,
                val id: Int,
                val img1v1Id: Int,
                val img1v1Url: String,
                val musicSize: Int,
                val name: String,
                val picId: Int,
                val picUrl: String,
                val topicPerson: Int,
                val trans: String
            )

            data class BMusic(
                val bitrate: Int,
                val dfsId: Int,
                val extension: String,
                val id: Long,
                val name: Any,
                val playTime: Int,
                val size: Int,
                val sr: Int,
                val volumeDelta: Int
            )

            data class HMusic(
                val bitrate: Int,
                val dfsId: Int,
                val extension: String,
                val id: Long,
                val name: Any,
                val playTime: Int,
                val size: Int,
                val sr: Int,
                val volumeDelta: Int
            )

            data class LMusic(
                val bitrate: Int,
                val dfsId: Int,
                val extension: String,
                val id: Long,
                val name: Any,
                val playTime: Int,
                val size: Int,
                val sr: Int,
                val volumeDelta: Int
            )
        }

        data class Radio(
            val buyed: Boolean,
            val category: String,
            val categoryId: Int,
            val createTime: Long,
            val desc: String,
            val descPicList: List<Any>,
            val discountPrice: Any,
            val dj: Any,
            val `dynamic`: Boolean,
            val feeScope: Int,
            val finished: Boolean,
            val icon: Any,
            val id: Int,
            val intervenePicId: Long,
            val intervenePicUrl: String,
            val lastProgramCreateTime: Long,
            val lastProgramId: Long,
            val lastProgramName: Any,
            val liveInfo: Any,
            val manualTagsDTO: Any,
            val name: String,
            val originalPrice: Int,
            val picId: Long,
            val picUrl: String,
            val playCount: Int,
            val price: Int,
            val privacy: Boolean,
            val programCount: Int,
            val purchaseCount: Int,
            val radioFeeType: Int,
            val replaceRadio: Any,
            val replaceRadioId: Int,
            val secondCategory: String,
            val shortName: Any,
            val subCount: Int,
            val subed: Boolean,
            val taskId: Int,
            val underShelf: Boolean,
            val videos: Any
        )
    }
}