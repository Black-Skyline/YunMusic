package com.handsome.module.podcast.model

data class PersonalizeRecommendationData(
    val code: Int,
    val `data`: List<Data>
) {
    data class Data(
        val alg: String,
        val buyed: Boolean,
        val category: String,
        val categoryId: Int,
        val composeVideo: Boolean,
        val createTime: Long,
        val desc: String,
        val descPicList: Any,
        val discountPrice: Any,
        val dj: Dj,
        val `dynamic`: Boolean,
        val feeScope: Int,
        val finished: Boolean,
        val hightQuality: Boolean,
        val icon: Any,
        val id: Int,    // 电台rid
        val intervenePicId: Long,
        val intervenePicUrl: String,
        val lastProgramCreateTime: Long,
        val lastProgramId: Long,
        val lastProgramName: String,
        val lastUpdateProgramName: String,
        val liveInfo: Any,
        val manualTagsDTO: Any,
        val name: String,
        val original: String,
        val originalPrice: Int,
        val picId: Long,
        val picUrl: String,
        val playCount: Int,
        val price: Int,
        val privacy: Boolean,
        val programCount: Int,
        val purchaseCount: Int,
        val radioFeeType: Int,
        val rcmdText: String,       // 电台介绍
        val rcmdtext: String,       // 电台介绍同上
        val replaceResource: Any,
        val scoreInfoDTO: Any,
        val secondCategory: String, // 电台标签
        val secondCategoryId: Int,
        val shortName: Any,
        val subCount: Int,
        val subed: Boolean,
        val taskId: Int,
        val underShelf: Boolean,
        val videos: Any,
        val whiteList: Boolean
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
            val birthday: Long,
            val city: Int,
            val defaultAvatar: Boolean,
            val description: String,
            val detailDescription: String,
            val djStatus: Int,
            val expertTags: Any,
            val experts: Experts,
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
        ) {
            data class Experts(
                val `1`: String
            )
        }

    }

}

data class NormalRecommendationData(
    val code: Int,
    val djRadios: List<DjRadio>,
    val name: String
) {
    data class DjRadio(
        val buyed: Boolean,
        val category: String,
        val categoryId: Int,
        val copywriter: String,
        val createTime: Long,
        val dj: Dj,
        val feeScope: Int,
        val id: Int,          // 电台rid
        val name: String,
        val picUrl: String,
        val playCount: Int,
        val programCount: Int,
        val radioFeeType: Int,
        val rcmdtext: String,
        val subCount: Int,
        val subed: Boolean
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
            val birthday: Long,
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
    }
}