package com.handsome.module.find.network.model

data class TopListData(
    val artistToplist: ArtistToplist,
    val code: Int,
    val list: List<Data>,
    val rewardToplist: RewardToplist
) {
    data class ArtistToplist(
        val artists: List<Artist>,
        val coverUrl: String,
        val name: String,
        val position: Int,
        val upateFrequency: String,
        val updateFrequency: String
    ) {
        data class Artist(
            val first: String,
            val second: String,
            val third: Int
        )
    }

    data class Data(
        val ToplistType: String,
        val adType: Int,
        val anonimous: Boolean,
        val artists: Any,
        val backgroundCoverId: Int,
        val backgroundCoverUrl: Any,
        val cloudTrackCount: Int,
        val commentThreadId: String,
        val coverImageUrl: Any,
        val coverImgId: Long,
        val coverImgId_str: String,
        val coverImgUrl: String,
        val coverText: Any,
        val createTime: Long,
        val creator: Any,
        val description: String,
        val englishTitle: Any,
        val highQuality: Boolean,
        val iconImageUrl: Any,
        val id: Long,
        val name: String,
        val newImported: Boolean,
        val opRecommend: Boolean,
        val ordered: Boolean,
        val playCount: Long,
        val privacy: Int,
        val recommendInfo: Any,
        val socialPlaylistCover: Any,
        val specialType: Int,
        val status: Int,
        val subscribed: Any,
        val subscribedCount: Int,
        val subscribers: List<Any>,
        val tags: List<String>,
        val titleImage: Int,
        val titleImageUrl: Any,
        val totalDuration: Int,
        val trackCount: Int,
        val trackNumberUpdateTime: Long,
        val trackUpdateTime: Long,
        val tracks: List<Track>,
        val updateFrequency: String,
        val updateTime: Long,
        val userId: Long
    ) {
        data class Track(
            val first: String,
            val second: String
        )
    }

    data class RewardToplist(
        val coverUrl: String,
        val name: String,
        val position: Int,
        val songs: List<Song>
    ) {
        data class Song(
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
            val hrMusic: Any,
            val id: Int,
            val lMusic: LMusic,
            val mMusic: MMusic,
            val mark: Int,
            val mp3Url: Any,
            val mvid: Int,
            val name: String,
            val no: Int,
            val noCopyrightRcmd: Any,
            val originCoverType: Int,
            val originSongSimpleData: Any,
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
            val single: Int,
            val sqMusic: SqMusic,
            val starred: Boolean,
            val starredNum: Int,
            val status: Int,
            val transName: Any
        ) {
            data class Album(
                val alias: List<Any>,
                val artist: Artist,
                val artists: List<Artist>,
                val blurPicUrl: String,
                val briefDesc: String,
                val commentThreadId: String,
                val company: Any,
                val companyId: Int,
                val copyrightId: Int,
                val description: String,
                val dolbyMark: Int,
                val gapless: Int,
                val id: Int,
                val mark: Int,
                val name: String,
                val onSale: Boolean,
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
                val transName: Any,
                val type: String
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

            data class MMusic(
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

            data class SqMusic(
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
    }
}