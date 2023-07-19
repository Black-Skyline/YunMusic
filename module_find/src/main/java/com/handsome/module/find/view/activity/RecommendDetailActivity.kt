package com.handsome.module.find.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.gsonSaveToSp
import com.handsome.lib.util.util.objectFromSp
import com.handsome.module.find.R
import com.handsome.module.find.databinding.ActivityRecommendDetailBinding
import com.handsome.module.find.network.exception.myCoroutineExceptionHandler
import com.handsome.module.find.network.model.RecommendDetailData
import com.handsome.module.find.view.adapter.RecommendDetailRvAdapter
import com.handsome.module.find.view.viewmodel.RecommendDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class RecommendDetailActivity : BaseActivity() {
    private val mBinding by lazy { ActivityRecommendDetailBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[RecommendDetailViewModel::class.java] }
    private val mRecommendDetailRvAdapter = RecommendDetailRvAdapter(::recommendDetailOnClick)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initBar()
        initMusic()
    }

    private fun initMusic() {
        initMusicAdapter()
        initMusicCollect()
        getRecommendDetailData()
    }

    private fun getRecommendDetailData() {
        mViewModel.getRecommendDetailData()
    }

    private fun recommendDetailOnClick(dailySong: RecommendDetailData.Data.DailySong) {
        dailySong.name.toast()
        //todo 播放逻辑！！！
    }

    private fun initMusicCollect() {
        fun doAfterGet(value : RecommendDetailData){
            mRecommendDetailRvAdapter.submitList(value.data.dailySongs)
            mBinding.apply {
                val time = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate.now().toString()
                } else {
                    "21/5"
                }
                recommendDetailTvTime.text = time
            }
        }
        lifecycleScope.launch(myCoroutineExceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.recommendDetailStateFlow.collectLatest {
                    if (it != null) {
                        if (it.code == 200){
                            doAfterGet(it)
                            gsonSaveToSp(it,"recommend_detail")
                        }else{
                            val value = objectFromSp<RecommendDetailData>("recommend_detail")
                            if (value!=null) doAfterGet(value)
                        }
                    }else{
                        val value = objectFromSp<RecommendDetailData>("recommend_detail")
                        if (value!=null) doAfterGet(value)
                    }
                }
            }
        }
    }

    private fun initMusicAdapter() {
        mBinding.recommendDetailRvMusic.apply {
            layoutManager =
                LinearLayoutManager(this@RecommendDetailActivity, RecyclerView.VERTICAL, false)
            adapter = mRecommendDetailRvAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_more, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.menu_item_music_more -> {
                "之后的道路，以后再来探索吧！".toast()
                //todo 等待点击事件
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initBar() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        fun startAction(context: Context) {
            context.startActivity(Intent(context, RecommendDetailActivity::class.java))
        }
    }
}