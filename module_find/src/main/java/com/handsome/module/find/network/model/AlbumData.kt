package com.handsome.module.find.network.model

data class AlbumData(
    val album: Album,
    val code: Int,
    val resourceState: Boolean,
    val songs: List<Song>
) {
    data class Album(
        val alias: List<Any>,
        val artist: Artist,
        val artists: List<Artist>,
        val awardTags: Any,
        val blurPicUrl: String,
        val briefDesc: String,
        val commentThreadId: String,
        val company: String,
        val companyId: Int,
        val copyrightId: Int,
        val description: String,
        val id: Long,
        val info: Info,
        val mark: Int,
        val name: String,
        val onSale: Boolean,
        val paid: Boolean,
        val pic: Long,
        val picId: Long,
        val picId_str: String,
        val picUrl: String,
        val publishTime: Long,
        val size: Int,
        val songs: List<Any>,
        val status: Int,
        val subType: String,
        val tags: String,
        val type: String
    ) {
        data class Artist(
            val albumSize: Int,
            val alias: List<String>,
            val briefDesc: String,
            val followed: Boolean,
            val id: Int,
            val img1v1Id: Long,
            val img1v1Id_str: String,
            val img1v1Url: String,
            val musicSize: Int,
            val name: String,
            val picId: Long,
            val picId_str: String,
            val picUrl: String,
            val topicPerson: Int,
            val trans: String
        )

        data class Info(
            val commentCount: Int,
            val commentThread: CommentThread,
            val comments: Any,
            val latestLikedUsers: Any,
            val liked: Boolean,
            val likedCount: Int,
            val resourceId: Int,
            val resourceType: Int,
            val shareCount: Int,
            val threadId: String
        ) {
            data class CommentThread(
                val commentCount: Int,
                val hotCount: Int,
                val id: String,
                val latestLikedUsers: Any,
                val likedCount: Int,
                val resourceId: Int,
                val resourceInfo: ResourceInfo,
                val resourceOwnerId: Int,
                val resourceTitle: String,
                val resourceType: Int,
                val shareCount: Int
            ) {
                data class ResourceInfo(
                    val creator: Any,
                    val encodedId: Any,
                    val id: Int,
                    val imgUrl: String,
                    val name: String,
                    val subTitle: Any,
                    val userId: Int,
                    val webUrl: Any
                )
            }
        }
    }

    data class Song(
        val a: Any,
        val al: Al,
        val alia: List<String>,
        val ar: List<Ar>,
        val cd: String,
        val cf: String,
        val cp: Int,
        val crbt: Any,
        val djId: Int,
        val dt: Int,
        val fee: Int,
        val ftype: Int,
        val h: H,
        val hr: Any,
        val id: Long,
        val l: L,
        val m: M,
        val mst: Int,
        val mv: Int,
        val name: String,
        val no: Int,
        val noCopyrightRcmd: Any,
        val pop: Int,
        val privilege: Privilege,
        val pst: Int,
        val rt: String,
        val rtUrl: Any,
        val rtUrls: List<Any>,
        val rtype: Int,
        val rurl: Any,
        val songJumpInfo: Any,
        val sq: Sq,
        val st: Int,
        val t: Int,
        val tns: List<String>,
        val v: Int
    ) {
        data class Al(
            val id: Int,
            val name: String,
            val pic: Long,
            val picUrl: String,
            val pic_str: String
        )

        data class Ar(
            val alia: List<String>,
            val id: Int,
            val name: String
        )

        data class H(
            val br: Int,
            val fid: Int,
            val size: Int,
            val sr: Int,
            val vd: Int
        )

        data class L(
            val br: Int,
            val fid: Int,
            val size: Int,
            val sr: Int,
            val vd: Int
        )

        data class M(
            val br: Int,
            val fid: Int,
            val size: Int,
            val sr: Int,
            val vd: Int
        )

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
            val payed: Int,
            val pl: Int,
            val plLevel: String,
            val playMaxBrLevel: String,
            val playMaxbr: Int,
            val preSell: Boolean,
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
                val listenType: Any,
                val resConsumable: Boolean,
                val userConsumable: Boolean
            )
        }

        data class Sq(
            val br: Int,
            val fid: Int,
            val size: Int,
            val sr: Int,
            val vd: Int
        )
    }
}