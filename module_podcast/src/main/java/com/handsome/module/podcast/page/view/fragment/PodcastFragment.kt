package com.handsome.module.podcast.page.view.fragment

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.music.sevice.MusicService
import com.handsome.lib.util.extention.toast
import com.handsome.module.podcast.databinding.FragmentPodcastBinding
import com.handsome.module.podcast.model.FMProgramsData
import com.handsome.module.podcast.model.NormalRecommendationData
import com.handsome.module.podcast.model.PersonalizeRecommendationData
import com.handsome.module.podcast.page.adapter.FMVpAdapter
import com.handsome.module.podcast.page.adapter.NormalRecommendAdapter
import com.handsome.module.podcast.page.adapter.PersonalizeRadioRecommendAdapter
import com.handsome.module.podcast.page.adapter.RecommendTitleAdapter
import com.handsome.module.podcast.page.view.activity.ProgramsDisplay
import com.handsome.module.podcast.page.viewmodel.PodcastFragmentViewModel
import com.handsome.module.podcast.utils.DataConstructionUtil
import com.handsome.module.podcast.utils.MultiplePagesTransformer
import com.handsome.module.podcast.utils.exceptionPrinter
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

    private val FMVpadapter by lazy { FMVpAdapter(::onClickFM) }

    // Service实例
    private lateinit var playService: MusicService  // 可播放具体节目

    // Service是否已绑定
    private var isServiceBinding: Boolean = false

    // Service的连接器
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playService = (service as MusicService.MusicPlayBinder).service
            isServiceBinding = true

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBinding = false
        }
    }


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
        bindPlayService()
        return binding.root
    }


    private fun initSubscribe() {
        // 个性化（兴趣）推荐数据
        lifecycleScope.launch(exceptionPrinter) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.personalizeRecommendResponseFlow.collectLatest {
                    if (it != null && it.code == 200) { // 有网络且请求成功
                        // 处理并向adapter提交数据
                        dealPersonalizeRecommendData(it.data)
                    } else {  // 无网络或请求失败
                        if (it != null) {
                            Log.d("ProgressTest", "code is ${it.code}")
                            toast("没有请求到personalizeRecommend数据")
                        }
                    }
                }
            }
        }
        // 通用推荐数据
        lifecycleScope.launch(exceptionPrinter) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.normalRecommendResponseFlow.collectLatest {
                    if (it != null && it.code == 200) { // 有网络且请求成功
                        // 处理并向adapter提交数据
                        podcastNormalRecommendAdapter.submitList(it.djRadios.take(9))
                    } else {  // 无网络或请求失败
                        if (it != null) {
                            toast("没有请求到normalRecommend数据")
                        }
                    }
                }
            }
        }
        // 获得FM的节目列表数据，提交给adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.fmProgramsResponseFlow.collectLatest {
                    Log.d("ProgressTest", "回调了fmProgramsResponseFlow collectLatest")
                    if (it != null && it.code == 200) { // 有网络且请求成功
                        // 处理并向adapter提交数据 )
                        FMVpadapter.submitList(it.programs)
                    } else {  // 无网络或请求失败
                        if (it != null) {
                            toast("没有请求到fmProgramsResponse数据")
                        }
                    }
                }
            }
        }
        // 获得音频的url
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.programAudioResponseFlow.collectLatest {
                    if (it != null && it.code == 200) { // 有网络且请求成功
                        // 处理获得音频的url,可用MusicService播放

                    } else {  // 无网络或请求失败
                        if (it != null) {
                            toast("没有请求到ProgramsResponse数据")
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
        binding.podcastVpFm.apply {
            adapter = FMVpadapter
            offscreenPageLimit = 3
            setPageTransformer(MultiplePagesTransformer())
        }
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
    private fun enterRadioStation1(response: PersonalizeRecommendationData.Data, sharedView: View) {
        // 进入电台具体页面……
        ProgramsDisplay.startWithSharedView(requireActivity(), response.id, sharedView)
    }

    private fun enterRadioStation2(response: NormalRecommendationData.DjRadio, sharedView: View) {
        // 进入电台具体页面……
        ProgramsDisplay.startWithSharedView(requireActivity(), response.id, sharedView)
    }

    private fun onClickFM(programs: List<FMProgramsData.Program>, index: Int) {
        // 播放FM的节目
        programs as ArrayList

        val list = ArrayList<WrapPlayInfo>()
//        /**
//         * 下方是电台VP的FM节目播放代码
//         */
//        for (i in programs) {
//            val audioName = i.description
//            val artist = i.name
//            val audioId = i.mainSong.id
//            val picUrl = i.coverUrl
//            val playInfo = WrapPlayInfo(audioName, artist, audioId, picUrl)
//            list.add(playInfo)
//        }
//        playService.setPlayInfoList(list, index)
//        model.getProgramAudio(programs[index].mainSong.id)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        activity?.unbindService(connection)
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = PodcastFragment()
    }

    private fun dealPersonalizeRecommendData(data: List<PersonalizeRecommendationData.Data>) {
        PersonalizeRadioRecommendAdapter.Data.TitleBean().let {
            dataBuilder.createPersonalizeRecommendAdapterData(it, data).apply {
                podcastPersonalizeRecommendAdapter.submitList(this)
            }
        }
    }

    private fun bindPlayService() {
        activity?.apply {
            Intent(this, MusicService::class.java).also { intent ->
                bindService(intent, connection, AppCompatActivity.BIND_AUTO_CREATE)
            }
        }

    }
}