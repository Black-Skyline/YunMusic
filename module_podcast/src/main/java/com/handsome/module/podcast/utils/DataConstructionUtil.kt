package com.handsome.module.podcast.utils

import com.handsome.module.podcast.model.PersonalizeRecommendationData
import com.handsome.module.podcast.page.adapter.PersonalizeRadioRecommendAdapter.Data

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/27
 * @Description:
 *
 */
class DataConstructionUtil {

    fun createPersonalizeRecommendAdapterData(
        titleBean: Data.TitleBean,
        contentBeanList: List<PersonalizeRecommendationData.Data>
    ): MutableList<Data> {
        val result = mutableListOf<Data>()
        result.add(0,titleBean)
        for (i in contentBeanList) {
            result.add(Data.ContentBean(i))
        }
        return result
    }
}