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
import com.handsome.module.find.databinding.ActivitySpecialEditionBinding
import com.handsome.module.find.view.adapter.AlbumSongsAdapter
import com.handsome.module.find.view.viewmodel.SpecialEditionViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

//也是album，网上搜的时候有点不靠谱。。
class SpecialEditionActivity : BaseActivity() {
    private val mBinding by lazy { ActivitySpecialEditionBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[SpecialEditionViewModel::class.java] }
    private val mAlbumSongsAdapter = AlbumSongsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        val id = intent.getLongExtra("id",32311)  //网上随便找的专辑id
        initBar()
        initRv(id)
    }

    private fun initRv(id : Long) {
        initMusicAdapter()
        initMusicCollect()
        getMusicData(id)
    }

    private fun initMusicAdapter() {
        mBinding.specialEditionRvMusic.apply {
            layoutManager = LinearLayoutManager(this@SpecialEditionActivity,RecyclerView.VERTICAL,false)
            adapter = mAlbumSongsAdapter
        }
    }

    private fun getMusicData(id: Long) {
        mViewModel.getAlbumData(id)
    }

    private fun initMusicCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel.stateFlow.collectLatest {
                    if (it != null){
                        mAlbumSongsAdapter.submitList(it.songs)
                        mBinding.apply {
                            val album = it.album
                            val singer = "歌手:${album.artists[0].name} >"
                            val publishTime = "发行时间 : ${SimpleDateFormat("yyyy.MM.dd").format(Date(album.publishTime))}"
                            val playAll =  "播放全部(${it.songs.size})"
                            specialEditionCollapsingImg.setImageFromUrl(album.blurPicUrl, placeholder = R.drawable.icon_picture)
                            specialEditionCollapsingTvColumnName.text = album.name
                            specialEditionCollapsingTvColumnSinger.text = singer
                            specialEditionCollapsingTvColumnTime.text = publishTime
                            specialEditionCollapsingTvColumnDescribe.text = album.description.trim()
                            specialEditionTvPlayAll.text = playAll
                        }
                    }
                }
            }
        }
    }

    private fun initBar() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_more,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { finish()
                return true
            }
            R.id.menu_item_music_more -> {
                "之后的道路，以后再来探索吧！".toast()
                //todo 等待点击事件
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        fun startAction(context : Context, id : Long){
            val intent = Intent(context,SpecialEditionActivity::class.java)
            intent.putExtra("id",id)
            context.startActivity(intent)
        }
    }
}