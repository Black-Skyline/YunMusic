package com.handsome.module.find.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.handsome.module.find.R
import com.handsome.module.find.databinding.FragmentFindBinding
import com.handsome.module.find.network.model.BannerBelowData
import com.handsome.module.find.network.model.BannerData
import com.handsome.module.find.network.model.RecommendMusicListData
import com.handsome.module.find.view.viewmodel.FindFragmentViewModel
import com.handsome.module.find.view.activity.WebViewActivity
import com.handsome.module.find.view.adapter.FindBannerBelowRvAdapter
import com.handsome.module.find.view.adapter.FindBannerVpAdapter
import com.handsome.module.find.view.adapter.FindRecommendListVpAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FindFragment : Fragment() {
    private val mBinding by lazy { FragmentFindBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[FindFragmentViewModel::class.java] }
    private val findBannerVpAdapter = FindBannerVpAdapter(::onBannerClick)
    private val findBannerBelowRvAdapter = FindBannerBelowRvAdapter(::onBannerBelowClick)
    private val findRecommendListVpAdapter = FindRecommendListVpAdapter(::onRecommendListClick)
    private lateinit var autoScrollHandler: Handler
    private lateinit var autoScrollRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)  //打开开关，让fragment也可以修改activity中的toolbar，同时会先监听activity中的menu，增加一个搜索图标
        initBanner()
        initBannerBelow()  //banner下面的图标,想不到起什么名字，就叫做bannerBelow了，下面同理
        initRecommendList()
    }

    private fun initRecommendList() {
        initRecommendListRvAdapter()
        initRecommendListCollect()
        getRecommendListData(6)
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
        lifecycleScope.launch() {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.recommendListStateFlow.collectLatest {
                    if (it != null) {
                        findRecommendListVpAdapter.submitList(it.result)
                    }
                }
            }
        }
    }

    private fun onRecommendListClick(result: RecommendMusicListData.Result) {
        result.name.toast()
        when (result.name) {
            //todo 点击事件
        }
    }

    private fun initBannerBelow() {
        initBannerBelowAdapter()
        initBannerBelowCollect()
        getBannerBelowData()
        //下面是配套的滑条
        initBannerBelowSb()
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.bannerBelowStateFlow.collectLatest {
                    if (it != null) {
                        findBannerBelowRvAdapter.submitList(it.data)
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
            "每日推荐" -> {}
            "私人FM" -> {}
            "歌单" -> {}
            "排行榜" -> {}
            else -> {}
        }
    }

    /**
     * 初始化轮播图的方法
     */
    private fun initBanner() {
        initBannerAdapter()
        initBannerCollect()
        getBannerData()
    }

    private fun startAutoScroll() {
        autoScrollHandler = Handler(Looper.getMainLooper())  //主线程上的handler
        autoScrollRunnable = Runnable {
            val currentItem = mBinding.findVpBanner.currentItem
            val nextItem = currentItem + 1
            mBinding.findVpBanner.setCurrentItem(nextItem, true)
            autoScrollHandler.postDelayed(autoScrollRunnable, 5000)
        }
        autoScrollHandler.postDelayed(autoScrollRunnable, 5000)
    }

    private fun stopAutoScroll() {
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.bannerStateFlow.collectLatest {
                    if (it != null) {
                        findBannerVpAdapter.submitList(it.banners)
                        mBinding.findVpBanner.apply {
                            setCurrentItem(Int.MAX_VALUE / 2, false)
                            offscreenPageLimit = 3
                        }
                        startAutoScroll()  //banner自动滑动
                    }
                }
            }
        }
    }

    /**
     * 用于传入banner的点击事件
     */
    private fun onBannerClick(bannerData: BannerData.Banner) {
        if (bannerData.targetId == 0) {
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

            }
            else -> {bannerData.typeTitle.toast()}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    /**
     *重写这个方法让fragment修改所在activity中的toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_tb, menu)
    }

    /**
     * 重写这个方法等到activity中没有消费这个事件之后然后进入这个fragment消费，如果匹配就在这里消费。
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //要想轮到这里，必须在activity中设置点击事件调用父类的
        return when (item.itemId) {
            R.id.item_tb_search -> {
                //todo 等待点击搜索之后就会跳转
                Log.d("lx", "menu点击事件出来了: ")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FindFragment()
    }
}