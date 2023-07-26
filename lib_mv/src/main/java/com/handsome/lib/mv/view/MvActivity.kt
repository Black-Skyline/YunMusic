package com.handsome.lib.mv.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.mv.databinding.ActivityMvBinding
import com.handsome.lib.mv.network.model.MvRecommendData
import com.handsome.lib.mv.view.adapter.RecommendMvRvAdapter
import com.handsome.lib.mv.view.viewmodel.MvViewModel
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import xyz.doikki.videocontroller.StandardVideoController

class MvActivity : BaseActivity() {
    private val mBinding by lazy { ActivityMvBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[MvViewModel::class.java] }
    private val mRecommendMvRvAdapter by lazy { RecommendMvRvAdapter(::onClickRecommend) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        val id = intent.getLongExtra("id", 10896407)
        val picUrl = intent.getStringExtra("picUrl")
        mBinding.mvTvSongName.text = intent.getStringExtra("songName") ?: "Mv"
        mBinding.mvTvSingerName.text = intent.getStringExtra("singerName") ?: "未知"
        if (picUrl != null && picUrl != "") {
            mBinding.mvImgUserIcon.setImageFromUrl(picUrl)
        }
        initMv(id)
        initRecommend()
    }

    private fun initRecommend() {
        initRecommendAdapter()
        initRecommendCollect()
        getRecommendData()
    }


    private fun onClickRecommend(data: MvRecommendData.Data) {
        MvActivity.startAction(
            this@MvActivity,
            data.id,
            data.name,
            data.artistName,
            data.cover
        )
    }

    //由于mv相似接口太烂了，所以这里请求的是上升最快mv
    private fun getRecommendData() {
        mViewModel.getRecommendMvData()
    }

    private fun initRecommendCollect() {
        mViewModel.mvRecommendLiveData.observe(this) {
            if (it.code != 200) {
                toast("网络异常，请重试")
            }
            if (it.data.isEmpty()) {
                "暂无相关推荐".toast()
            }
            mRecommendMvRvAdapter.submitList(it.data)
        }
    }

    private fun initRecommendAdapter() {
        mBinding.mvRvRecommend.apply {
            layoutManager = LinearLayoutManager(this@MvActivity, RecyclerView.VERTICAL, false)
            adapter = mRecommendMvRvAdapter
        }
    }

    private fun initMv(id: Long) {
        initObserve()
        mViewModel.getMvData(id)
    }

    private fun initObserve() {
        mViewModel.mvLiveData.observe(this) {
            if (it.code == 200) {
                val url = it.data.url
                setCurrentVideo(url)
            }
        }
    }

    private fun setCurrentVideo(url: String) {
        mBinding.mvVideo.apply {
            val controller = StandardVideoController(this@MvActivity)
            controller.addDefaultControlComponent("Mv", false)
            setVideoController(controller) //设置控制器
            setUrl(url) //设置视频地址
            start() //开始播放，不调用则不自动播放
        }

    }

    override fun onPause() {
        super.onPause()
        mBinding.mvVideo.pause()
    }

    override fun onResume() {
        super.onResume()
        mBinding.mvVideo.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.mvVideo.release()
    }


    override fun onBackPressed() {
        if (!mBinding.mvVideo.onBackPressed()) {
            super.onBackPressed()
        }
    }


    companion object {
        fun startAction(
            context: Context,
            id: Long,
            songName: String,
            singerName: String,
            picUrl: String
        ) {
            val intent = Intent(context, MvActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("songName", songName)
            intent.putExtra("singerName", singerName)
            intent.putExtra("picUrl", picUrl)
            context.startActivity(intent)
        }
    }
}