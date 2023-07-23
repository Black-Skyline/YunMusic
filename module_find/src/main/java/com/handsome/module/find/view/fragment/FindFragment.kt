package com.handsome.module.find.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.gsonSaveToSp
import com.handsome.lib.util.util.objectFromSp
import com.handsome.lib.util.util.shareText
import com.handsome.module.find.databinding.FragmentFindBinding
import com.handsome.module.find.network.exception.myCoroutineExceptionHandler
import com.handsome.module.find.network.model.BannerBelowData
import com.handsome.module.find.network.model.BannerData
import com.handsome.module.find.network.model.RecommendMusicListData
import com.handsome.module.find.network.model.TopListData
import com.handsome.module.find.view.activity.MusicListDetailActivity
import com.handsome.module.find.view.activity.RecommendDetailActivity
import com.handsome.module.find.view.activity.SpecialEditionActivity
import com.handsome.module.find.view.activity.TopListActivity
import com.handsome.module.find.view.viewmodel.FindFragmentViewModel
import com.handsome.module.find.view.activity.WebViewActivity
import com.handsome.module.find.view.adapter.FindBannerBelowRvAdapter
import com.handsome.module.find.view.adapter.FindBannerVpAdapter
import com.handsome.module.find.view.adapter.FindRecommendListVpAdapter
import com.handsome.module.find.view.adapter.TopListVpAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FindFragment : Fragment() {
    private val mBinding by lazy { FragmentFindBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[FindFragmentViewModel::class.java] }
    private val findBannerVpAdapter by lazy{ FindBannerVpAdapter(::onBannerClick) }
    private val findBannerBelowRvAdapter by lazy {FindBannerBelowRvAdapter(::onBannerBelowClick)}
    private val findRecommendListVpAdapter by lazy{FindRecommendListVpAdapter(::onRecommendListClick)}
    private val findTopListVpAdapter by lazy { TopListVpAdapter(::onClickTopList) }
    private var autoScrollHandler: Handler? = null
    private var autoScrollRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBanner()
        initBannerBelow()  //banner下面的图标,想不到起什么名字，就叫做bannerBelow了，下面同理
        initRecommendList()
        initTopList()
    }

    /**
     * 初始化轮播图的方法
     */
    private fun initBanner() {
        initBannerAdapter()
        initBannerCollect()
        getBannerData()
    }

    private fun initBannerBelow() {
        initBannerBelowAdapter()
        initBannerBelowCollect()
        getBannerBelowData()
        //下面是配套的滑条
        initBannerBelowSb()
    }

    private fun initRecommendList() {
        initRecommendListRvAdapter()
        initRecommendListCollect()
        getRecommendListData(10)
        initRecommendListShare()
    }

    private fun initRecommendListShare() {
        mBinding.findImgMore.setOnClickListener {
            requireContext().shareText("小帅哥快来玩啊: http://why.vin:2023/personalized")
        }
    }

    private fun initTopList() {
        initTopListAdapter()
        initTopListCollect()
        getTopListData()
        initTopListShare()
    }

    private fun initTopListShare() {
        mBinding.findImgTopListMore.setOnClickListener {
            requireContext().shareText("小帅哥快来玩啊: http://why.vin:2023/toplist/detail")
        }
    }


    private fun initRecommendListRvAdapter() {
        mBinding.findRvRecommend.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = findRecommendListVpAdapter
        }
    }

    private fun getRecommendListData(size: Int) {
        mViewModel.getRecommendListData(size)
    }

    private fun initRecommendListCollect() {
        fun doAfterGet(value : RecommendMusicListData){
            findRecommendListVpAdapter.submitList(value.result)
        }
        lifecycleScope.launch(myCoroutineExceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.recommendListStateFlow.collectLatest {
                    if (it != null) {
                        if (it.code == 200){
                            doAfterGet(it)
                            gsonSaveToSp(it,"recommend_music_list")
                        }else{
                            val value = objectFromSp<RecommendMusicListData>("recommend_music_list")
                            if (value != null) doAfterGet(value)
                        }
                    }else{
                        val value = objectFromSp<RecommendMusicListData>("recommend_music_list")
                        if (value != null) doAfterGet(value)
                    }
                }
            }
        }
    }

    private fun onRecommendListClick(result: RecommendMusicListData.Result) {
        MusicListDetailActivity.startAction(requireContext(),result.id)
    }

    private fun initBannerBelowSb() {
        initScroll()
    }

    /**
     * 这个方法用来rv和seekbar协作滑动，滑动事件监听
     */
    private fun initScroll() {
        mBinding.findRvBannerBelow.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val haveScrolled = recyclerView.computeHorizontalScrollOffset().toDouble()
                val rvMaxWidth = recyclerView.computeHorizontalScrollRange().toDouble()
                val percent: Double = haveScrolled / rvMaxWidth
                val processWidth = mBinding.findRvBannerBelowSb.measuredWidth
                val scrollDistance = processWidth * percent
                mBinding.findRvBannerBelowSb.progress = scrollDistance.toInt()
            }
        })

        mBinding.findRvBannerBelowSb.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val haveScrolled = progress.toDouble()
                    val sbMaxWidth = mBinding.findRvBannerBelowSb.measuredWidth.toDouble()
                    val percent: Double = haveScrolled / sbMaxWidth
                    val rvScrollWidth =
                        mBinding.findRvBannerBelow.computeHorizontalScrollRange().toDouble()
                    val scrollDistance = rvScrollWidth * percent
                    mBinding.findRvBannerBelow.smoothScrollToPosition(scrollDistance.toInt())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun getBannerBelowData() {
        mViewModel.getBannerBelowData()
    }

    private fun initBannerBelowCollect() {
        fun doAfterGet(value : BannerBelowData){
            findBannerBelowRvAdapter.submitList(value.data)
        }
        lifecycleScope.launch(myCoroutineExceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.bannerBelowStateFlow.collectLatest {
                    if (it != null) {
                        if (it.code == 200){
                            doAfterGet(it)
                            gsonSaveToSp(it,"banner_below")
                        }else{
                            val value = objectFromSp<BannerBelowData>("banner_below")
                            if (value != null) doAfterGet(value)
                        }
                    }else{
                        val value = objectFromSp<BannerBelowData>("banner_below")
                        if (value != null) doAfterGet(value)
                    }
                }
            }
        }
    }

    private fun initBannerBelowAdapter() {
        mBinding.findRvBannerBelow.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = findBannerBelowRvAdapter
        }
    }

    private fun onBannerBelowClick(bannerBelowData: BannerBelowData.Data) {
        bannerBelowData.name.toast()
        when (bannerBelowData.name) {
            "每日推荐" -> {
                RecommendDetailActivity.startAction(requireContext())
            }
            "私人FM" -> {}
            "歌单" -> {}
            "排行榜" -> {
                TopListActivity.startAction(requireContext())
            }
            else -> {}
        }
    }



    private fun startAutoScroll() {
        autoScrollHandler = Handler(Looper.getMainLooper())  //主线程上的handler
        autoScrollRunnable = Runnable {
            val currentItem = mBinding.findVpBanner.currentItem
            val nextItem = currentItem + 1
            mBinding.findVpBanner.setCurrentItem(nextItem, true)
            autoScrollHandler?.postDelayed(autoScrollRunnable!!, 5000)
        }
        autoScrollHandler?.postDelayed(autoScrollRunnable!!, 5000)
    }

    private fun stopAutoScroll() {
        autoScrollHandler?.removeCallbacks(autoScrollRunnable!!)
    }

    override fun onStop() {
        super.onStop()
        stopAutoScroll()
    }

    private fun initBannerAdapter() {
        mBinding.findVpBanner.adapter = findBannerVpAdapter
    }

    private fun getBannerData() {
        mViewModel.getBannerData()
    }

    private fun initBannerCollect() {
        fun doAfterGet(value : BannerData){
            stopAutoScroll()
            findBannerVpAdapter.submitList(value.banners)
            mBinding.findVpBanner.apply {
                setCurrentItem(Int.MAX_VALUE / 2, false)
                offscreenPageLimit = 3
            }
            startAutoScroll()  //banner自动滑动
        }
        lifecycleScope.launch(myCoroutineExceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.bannerStateFlow.collectLatest {
                    if (it != null) {
                        if (it.code == 200){
                            doAfterGet(it)
                            gsonSaveToSp(it,"banner")
                        }else{
                            val value = objectFromSp<BannerData>("banner")
                            if (value != null) doAfterGet(value)
                        }
                    }else{
                        val value = objectFromSp<BannerData>("banner")
                        if (value != null) doAfterGet(value)
                    }
                }
            }
        }
    }

    /**
     * 用于传入banner的点击事件
     */
    private fun onBannerClick(bannerData: BannerData.Banner) {
        if (bannerData.targetId.toInt() == 0) {
            //那就不是歌曲，是链接。
            val url =  bannerData.url
            val title = bannerData.typeTitle
            WebViewActivity.startAction(requireContext(),url,title)
            return
        }
        //可能是歌曲，也可能是专辑
        when(bannerData.typeTitle){
            //todo 等待播放做好
            "新歌首发" -> {
                "新歌首发".toast()
            }
            "新碟首发" -> {
                SpecialEditionActivity.startAction(requireContext(),bannerData.targetId)
            }
            else -> {bannerData.typeTitle.toast()}
        }
    }

    private fun getTopListData() {
        mViewModel.getTopList()
    }

    private fun initTopListCollect() {
        fun doAfterGet(it : TopListData){
            //遍历，留下来有简单歌名人名的
            val list = ArrayList<TopListData.Data>()
            for (i in it.list){
                if (i.tracks.isNotEmpty()){
                    list.add(i)
                }else{
                    break
                }
            }
            findTopListVpAdapter.submitList(list)
        }
        lifecycleScope.launch(myCoroutineExceptionHandler){
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel.topListStateFlow.collectLatest {
                    if (it != null) {
                        if (it.code == 200){
                            doAfterGet(it)
                            gsonSaveToSp(it,"top_list")
                        }else{
                            val value = objectFromSp<TopListData>("top_list")
                            if (value != null) doAfterGet(value)
                        }
                    }else{
                        val value = objectFromSp<TopListData>("top_list")
                        if (value != null) doAfterGet(value)
                    }
                }
            }
        }
    }

    private fun initTopListAdapter() {
        mBinding.findVpTopList.adapter = findTopListVpAdapter
        //取消边部阴影
        val childView = mBinding.findVpTopList.getChildAt(0)
        if (childView is RecyclerView){
            childView.overScrollMode = View.OVER_SCROLL_NEVER
        }
    }

    private fun onClickTopList(data: TopListData.Data) {
        MusicListDetailActivity.startAction(requireContext(),data.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FindFragment()
    }
}