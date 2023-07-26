package com.handsome.module.podcast.page.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.handsome.module.podcast.databinding.FragmentPodcastBinding
import com.handsome.module.podcast.model.PersonalizeRecommendationData
import com.handsome.module.podcast.page.adapter.InterestRadioRecommendAdapter
import com.handsome.module.podcast.page.viewmodel.PodcastFragmentViewModel

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/21
 * @Description:
 *
 */
class PodcastFragment : Fragment() {
    private var _binding: FragmentPodcastBinding? = null
    private val binding: FragmentPodcastBinding get() = _binding!!

    private val model by lazy { ViewModelProvider(this)[PodcastFragmentViewModel::class.java] }


    private val podcastInterestRecommendAdapter by lazy { InterestRadioRecommendAdapter(::contentClickEvent) }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        _binding = FragmentPodcastBinding.inflate(layoutInflater)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null)
            _binding = FragmentPodcastBinding.inflate(inflater, container, false)
        initView()
        initClick()
        initObserve()
        initEvent()
        initSubscribe()
        return binding.root
    }

    private fun initSubscribe() {

    }

    private fun initEvent() {

    }

    private fun initObserve() {

    }

    private fun initClick() {

    }

    private fun initView() {
//        initBanner()
        initTopFunction()   // 顶部的功能滑块
        initPartFM()        // 顶部的功能滑块下方的“随心听FM”
        initRadioRecommendList()
    }


    /**
     * 把多个adapter初始化以后concat起来
     */
    private fun initRadioRecommendList() {
        binding.podcastRvRadioRecommend.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = podcastInterestRecommendAdapter
        }

    }

    private fun initPartFM() {

    }

    private fun initTopFunction() {

    }

    /**
     * 兴趣推荐的具体item电台 的点击事件交由展示兴趣推荐Fragment实现
     * @param response
     */
    private fun contentClickEvent(response: PersonalizeRecommendationData.Data) {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PodcastFragment()
    }
}