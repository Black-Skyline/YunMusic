package com.handsome.module.find.network.model

data class SingleMusicListDetailData(
    val code: Int,
    val fromUserCount: Int,
    val fromUsers: Any,
    val playlist: Playlist,
    val privileges: List<Privilege>,
    val relatedVideos: Any,
    val resEntrance: Any,
    val sharedPrivilege: Any,
    val songFromUsers: Any,
    val trialMode: Int,
    val urls: Any
) {
    data class Playlist(
        val adType: Int,
        val algTags: Any,
        val backgroundCoverId: Long,
        val backgroundCoverUrl: Any,
        val bannedTrackIds: Any,
        val cloudTrackCount: Int,
        val commentCount: Int,
        val commentThreadId: String,
        val copied: Boolean,
        val coverImgId: Long,
        val coverImgId_str: String,
        val coverImgUrl: String,
        val createTime: Long,
        val creator: Creator,
        val description: String,
        val englishTitle: Any,
        val gradeStatus: String,
        val highQuality: Boolean,
        val historySharedUsers: Any,
        val id: Long,
        val mvResourceInfos: Any,
        val name: String,
        val newImported: Boolean,
        val officialPlaylistType: Any,
        val opRecommend: Boolean,
        val ordered: Boolean,
        val playCount: Long,
        val privacy: Int,
        val relateResType: Any,
        val remixVideo: Any,
        val score: Any,
        val shareCount: Int,
        val sharedUsers: Any,
        val specialType: Int,
        val status: Int,
        val subscribed: Boolean,
        val subscribedCount: Int,
        val subscribers: List<Subscriber>,
        val tags: List<String>,
        val titleImage: Int,
        val titleImageUrl: Any,
        val trackCount: Int,
        val trackIds: List<TrackId>,
        val trackNumberUpdateTime: Long,
        val trackUpdateTime: Long,
        val tracks: List<Track>,
        val updateFrequency: Any,
        val updateTime: Long,
        val userId: Long,
        val videoIds: Any,
        val videos: Any
    ) {
        data class Creator(
            val accountStatus: Int,
            val anchor: Boolean,
            val authStatus: Int,
            val authenticationTypes: Int,
            val authority: Int,
            val avatarDetail: AvatarDetail,
            val avatarImgId: Long,
            val avatarImgIdStr: String,
            val avatarImgId_str: String,
            val avatarUrl: String,
            val backgroundImgId: Long,
            val backgroundImgIdStr: String,
            val backgroundUrl: String,
            val birthday: Int,
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
            val userId: Int,
            val userType: Int,
            val vipType: Int
        ) {
            data class AvatarDetail(
                val identityIconUrl: String,
                val identityLevel: Int,
                val userType: Int
            )
        }

        data class Subscriber(
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

        data class TrackId(
            val alg: Any,
            val at: Long,
            val f: Any,
            val id: Int,
            val rcmdReason: String,
            val sc: Any,
            val sr: Any,
            val t: Int,
            val uid: Long,
            val v: Int
        )

        data class Track(
            val a: Any,
            val al: Al,
            val alia: List<String>,
            val ar: List<Ar>,
            val awardTags: Any,
            val cd: String,
            val cf: String,
            val copyright: Int,
            val cp: Int,
            val crbt: Any,
            val djId: Int,
            val dt: Int,
            val entertainmentTags: Any,
            val fee: Int,
            val ftype: Int,
            val h: H,
            val hr: Hr,
            val id: Int,
            val l: L,
            val m: M,
            val mark: Long,
            val mst: Int,
            val mv: Int,
            val name: String,
            val no: Int,
            val noCopyrightRcmd: Any,
            val originCoverType: Int,
            val originSongSimpleData: Any,
            val pop: Int,
            val pst: Int,
            val publishTime: Long,
            val resourceState: Boolean,
            val rt: String,
            val rtUrl: Any,
            val rtUrls: List<Any>,
            val rtype: Int,
            val rurl: Any,
            val s_id: Int,
            val single: Int,
            val songJumpInfo: Any,
            val sq: Sq,
            val st: Int,
            val t: Int,
            val tagPicList: Any,
            val tns: List<String>,
            val v: Int,
            val version: Int
        ) {
            data class Al(
                val id: Int,
                val name: String,
                val pic: Long,
                val picUrl: String,
                val pic_str: String,
                val tns: List<Any>
            )

            data class Ar(
                val alias: List<Any>,
                val id: Int,
                val name: String,
                val tns: List<Any>
            )

            data class H(
                val br: Int,
                val fid: Int,
                val size: Int,
                val vd: Int
            )

            data class Hr(
                val br: Int,
                val fid: Int,
                val size: Int,
                val vd: Int
            )

            data class L(
                val br: Int,
                val fid: Int,
                val size: Int,
                val vd: Int
            )

            data class M(
                val br: Int,
                val fid: Int,
                val size: Int,
                val vd: Int
            )

            data class Sq(
                val br: Int,
                val fid: Int,
                val size: Int,
                val vd: Int
            )
        }
    }

    data class Privilege(
        val chargeInfoList: List<ChargeInfo>,
        val cp: Int,
        val cs: Boolean,
        val dl: Int,
        val dlLevel: String,
        val downloadMaxBrLevel: String,
        val downloadMaxbr: Int,
        val fee: Int,
        val fl: Int,
        val flLevel: String,
        val flag: Int,
        val freeTrialPrivilege: FreeTrialPrivilege,
        val id: Int,
        val maxBrLevel: String,
        val maxbr: Int,
        val paidBigBang: Boolean,
        val payed: Int,
        val pc: Any,
        val pl: Int,
        val plLevel: String,
        val playMaxBrLevel: String,
        val playMaxbr: Int,
        val preSell: Boolean,
        val realPayed: Int,
        val rightSource: Int,
        val rscl: Any,
        val sp: Int,
        val st: Int,
        val subp: Int,
        val toast: Boolean
    ) {
        data class ChargeInfo(
            val chargeMessage: Any,
            val chargeType: Int,
            val chargeUrl: Any,
            val rate: Int
        )

        data class FreeTrialPrivilege(
            val cannotListenReason: Int,
            val listenType: Any,
            val resConsumable: Boolean,
            val userConsumable: Boolean
        )
    }
}