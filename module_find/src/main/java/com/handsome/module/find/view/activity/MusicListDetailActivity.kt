package com.handsome.module.find.view.activity

import android.content.Context
import android.content.Intent
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
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import com.handsome.module.find.R
import com.handsome.module.find.databinding.ActivityMusicListDetailBinding
import com.handsome.module.find.network.exception.myCoroutineExceptionHandler
import com.handsome.module.find.view.adapter.MusicListDetailAdapter
import com.handsome.module.find.view.viewmodel.MusicListDetailViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MusicListDetailActivity : BaseActivity() {
    private val mBinding by lazy { ActivityMusicListDetailBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[MusicListDetailViewModel::class.java] }
    private val mMusicListDetailAdapter = MusicListDetailAdapter()  //歌单详情也可以用

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        val id = intent.getLongExtra("id", 24381616)  //网上随便找的歌单id
        initBar()
        initRv(id)
        initTop(id)
    }

    private fun initTop(id : Long) {
        initTopCollect()
        getTopInfo(id)
    }

    private fun getTopInfo(id : Long) {
        mViewModel.getSingleMusicDetailData(id)
    }

    private fun initTopCollect() {
        lifecycleScope.launch(myCoroutineExceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.singleMusicListDetailStateFlow.collectLatest {
                    if (it != null && it.code == 200) {
                        val detail = it.playlist
                        mBinding.apply {
                            musicListCollapsingImg.setImageFromUrl(
                                detail.coverImgUrl, placeholder = R.drawable.icon_picture
                            )
                            musicListCollapsingTvColumnName.text = detail.name
                            musicListCollapsingTvColumnSinger.text = detail.creator.nickname
                            musicListCollapsingTvColumnDescribe.text = detail.description
                            musicListTvPlayAll.text = "播放全部"
                        }
                    }
                }
            }
        }
    }

    private fun initRv(id: Long) {
        initMusicAdapter()
        initMusicCollect(id)
    }

    private fun initMusicAdapter() {
        mBinding.musicListRvMusic.apply {
            layoutManager = LinearLayoutManager(
                this@MusicListDetailActivity,
                RecyclerView.VERTICAL, false
            )
            adapter = mMusicListDetailAdapter
        }
    }

    private fun initMusicCollect(id: Long) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.getMusicListData(id).catch {
                    it.printStackTrace()
                }.collect {
                    mMusicListDetailAdapter.submitData(it)
                }
            }
        }
    }

    private fun initBar() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    companion object {
        fun startAction(context: Context, id: Long) {
            val intent = Intent(context, MusicListDetailActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }
}