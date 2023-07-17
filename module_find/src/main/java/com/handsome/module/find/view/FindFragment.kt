package com.handsome.module.find.view

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
import com.handsome.module.find.R
import com.handsome.module.find.databinding.FragmentFindBinding
import com.handsome.module.find.view.adapter.FindBannerBelowRvAdapter
import com.handsome.module.find.view.adapter.FindBannerVpAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FindFragment : Fragment() {
   private val mBinding by lazy { FragmentFindBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[FindFragmentViewModel::class.java] }
    private val findBannerVpAdapter = FindBannerVpAdapter()
    private val findBannerBelowRvAdapter = FindBannerBelowRvAdapter()
    private lateinit var autoScrollHandler : Handler
    private lateinit var autoScrollRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)  //打开开关，让fragment也可以修改activity中的toolbar，同时会先监听activity中的menu
        initBanner()
        initBannerBelow()  //banner下面的图标,想不到起什么名字，就叫做bannerBelow了，下面同理
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
        mBinding.findRvBannerBelow.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d("lx", "onScrolled:dx = $dx ; dy = $dy")
                val haveScrolled = recyclerView.computeHorizontalScrollOffset().toDouble()
                val rvMaxWidth = recyclerView.computeHorizontalScrollRange().toDouble()
                val percent : Double = haveScrolled / rvMaxWidth
                Log.d("TAG", " percent:${percent}")
                val processWidth = mBinding.findRvBannerBelowSb.measuredWidth
                Log.d("TAG", " processWidth:${processWidth}")
                val scrollDistance = processWidth * percent
                Log.d("TAG", " scrollDistance:${scrollDistance}")
                mBinding.findRvBannerBelowSb.progress = scrollDistance.toInt()
            }
        })

        mBinding.findRvBannerBelowSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    val haveScrolled = progress.toDouble()
                    val sbMaxWidth = mBinding.findRvBannerBelowSb.measuredWidth.toDouble()
                    val percent : Double = haveScrolled / sbMaxWidth
                    val rvScrollWidth = mBinding.findRvBannerBelow.computeHorizontalScrollRange().toDouble()
                    val scrollDistance = rvScrollWidth * percent
                    Log.d("TAG", "scrollDistance:${scrollDistance} ")
                    mBinding.findRvBannerBelow.smoothScrollToPosition(scrollDistance.toInt())
                }
                Log.d("lx", "onProgressChanged:${progress} ")
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
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel.bannerBelowStateFlow.collectLatest {
                    if (it != null){
                        findBannerBelowRvAdapter.submitList(it.data)
                    }
                }
            }
        }
    }

    private fun initBannerBelowAdapter() {
        mBinding.findRvBannerBelow.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter = findBannerBelowRvAdapter
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
            val nextItem = currentItem+1
            mBinding.findVpBanner.setCurrentItem(nextItem,true)
            autoScrollHandler.postDelayed(autoScrollRunnable,5000)
        }
        autoScrollHandler.postDelayed(autoScrollRunnable,5000)
    }

    private fun stopAutoScroll(){
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
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel.bannerStateFlow.collectLatest {
                    if (it != null){
                        findBannerVpAdapter.submitList(it.banners)
                        mBinding.findVpBanner.apply {
                            setCurrentItem(Int.MAX_VALUE/2,false)
                            offscreenPageLimit = 3
                        }
                        startAutoScroll()  //banner自动滑动
                    }
                }
            }
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
            R.id.item_tb_search-> {
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