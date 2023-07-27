package com.handsome.module.podcast.page.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.toast
import com.handsome.module.podcast.databinding.FragmentPodcastBinding
import com.handsome.module.podcast.model.FMProgramsData
import com.handsome.module.podcast.model.NormalRecommendationData
import com.handsome.module.podcast.model.PersonalizeRecommendationData
import com.handsome.module.podcast.network.api.RadioStationRecommendationApiService
import com.handsome.module.podcast.page.adapter.FMVpAdapter
import com.handsome.module.podcast.page.adapter.NormalRecommendAdapter
import com.handsome.module.podcast.page.adapter.PersonalizeRadioRecommendAdapter
import com.handsome.module.podcast.page.adapter.RecommendTitleAdapter
import com.handsome.module.podcast.page.view.activity.ProgramsDisplay
import com.handsome.module.podcast.page.viewmodel.PodcastFragmentViewModel
import com.handsome.module.podcast.utils.DataConstructionUtil
import com.handsome.module.podcast.utils.exceptionPrinter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
    private val dataBuilder = DataConstructionUtil()

    private val podcastPersonalizeRecommendAdapter by lazy { PersonalizeRadioRecommendAdapter(::enterRadioStation1) }
    private val podcastNormalRecommendAdapter by lazy { NormalRecommendAdapter(::enterRadioStation2) }

    private val FMVpadapter by lazy { FMVpAdapter(::onclick) }


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

        initSubscribe()
        initObserve()
        initView()
        initClick()
        initEvent()
        return binding.root
    }

    private fun initSubscribe() {
        // 个性化（兴趣）推荐数据
        lifecycleScope.launch(exceptionPrinter) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.personalizeRecommendResponseFlow.collectLatest {
                    if (it != null && it.code == 200) { // 有网络且请求成功
                        // 处理并向adapter提交数据
                        Log.d("ProgressTest", "得到了数据 code is ${it.code}")
                        for (i in it.data) {
                            Log.d("datatest", "datalook: ${i.playCount}")
                        }
                        dealPersonalizeRecommendData(it.data)
                    } else {  // 无网络或请求失败
                        if (it != null) {
                            Log.d("ProgressTest", "code is ${it?.code}")
                            toast("没有请求到personalizeRecommend数据")
                        }
                    }
                }
            }
        }
        lifecycleScope.launch(exceptionPrinter) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.normalRecommendResponseFlow.collectLatest {
                    if (it != null && it.code == 200) { // 有网络且请求成功
                        // 处理并向adapter提交数据
                        Log.d("ProgressTest", "得到了数据 code is ${it.code}")
                        podcastNormalRecommendAdapter.submitList(it.djRadios)
                    } else {  // 无网络或请求失败
                        if (it != null) {
                            Log.d("ProgressTest", "code is ${it?.code}")
                            toast("没有请求到normalRecommend数据")
                        }

                    }
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    model.fmProgramsResponseFlow.collectLatest {
                        if (it != null && it.code == 200) { // 有网络且请求成功
                            // 处理并向adapter提交数据
//                            Log.d("ProgressTest", "得到了数据 code is ${it.code}")
//                            FMVpadapter.submitList(it.programs)
//                            podcastNormalRecommendAdapter.submitList(it.djRadios)
                        } else {  // 无网络或请求失败
                            if (it != null) {
//                                Log.d("ProgressTest", "code is ${it?.code}")
                                toast("没有请求到fmProgramsResponse数据")
                            }

                        }
                    }
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    model.ProgramsResponseFlow.collectLatest {
                        if (it != null && it.code == 200) { // 有网络且请求成功
                            // 处理并向adapter提交数据
//                            Log.d("ProgressTest", "得到了数据 code is ${it.code}")
                        } else {  // 无网络或请求失败
                            if (it != null) {
                                toast("没有请求到ProgramsResponse数据")
                            }

                        }
                    }
                }
            }
        }
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


    private fun initPartFM() {
        getFMData()
        initFMVpAdapter()
    }

    private fun initFMVpAdapter() {
//        binding.viewPager2.apply {
//            adapter = FMVpadapter
//        }
    }

    private fun getFMData() {
        model.getFMPrograms()
    }

    private fun initTopFunction() {

    }


    /**
     * 把多个adapter初始化以后concat起来
     */
    private fun initRadioRecommendList() {
        getRadioRecommendData()
        initRadioRecommendAdapter()
    }

    private fun initRadioRecommendAdapter() {
        binding.podcastRvRadioRecommend.apply {
            val manager =
                GridLayoutManager(requireActivity(), 3, RecyclerView.VERTICAL, false)
            manager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position == 0 || position == 7) return 3
                    return 1
                }
            }
            layoutManager = manager
            adapter = ConcatAdapter(
                podcastPersonalizeRecommendAdapter,
                RecommendTitleAdapter("通用推荐"),
                podcastNormalRecommendAdapter
            )
        }
    }

    private fun getRadioRecommendData() {
        // 从网络获取对应数据向订阅者发送
        model.getPersonalizeRecommend()
        model.getNormalRecommend()
    }


    /**
     * 兴趣推荐的具体item电台 的点击事件交由展示兴趣推荐Fragment实现
     * @param response
     */
    private fun enterRadioStation1(response: PersonalizeRecommendationData.Data) {
        // 进入电台具体页面……
        ProgramsDisplay.startAction(requireActivity(), response.id)
    }

    private fun enterRadioStation2(response: NormalRecommendationData.DjRadio) {
        // 进入电台具体页面……
        ProgramsDisplay.startAction(requireActivity(), response.id)
    }

    private fun onclick(programs: List<FMProgramsData.Program>, i: Int) {
        // 播放节目

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PodcastFragment()
    }

    fun dealPersonalizeRecommendData(data: List<PersonalizeRecommendationData.Data>) {
        PersonalizeRadioRecommendAdapter.Data.TitleBean().let {
            dataBuilder.createPersonalizeRecommendAdapterData(it, data).apply {
                for (i in this) {
                    if (i.type == 1) {
                        i as PersonalizeRadioRecommendAdapter.Data.ContentBean
                        Log.d("datatest", "datadeal: ${i.need.playCount}")
                    }
                }
                podcastPersonalizeRecommendAdapter.submitList(this)
            }
        }
    }

}