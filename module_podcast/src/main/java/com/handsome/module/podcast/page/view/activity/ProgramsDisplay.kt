package com.handsome.module.podcast.page.view.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.music.page.view.MusicPlayActivity
import com.handsome.lib.music.sevice.MusicService
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.MyRotationAnimate
import com.handsome.module.podcast.databinding.ActivityProgramsDisplayForStationBinding
import com.handsome.module.podcast.model.RadioProgramsData
import com.handsome.module.podcast.page.adapter.ProgramsDisplayAdapter
import com.handsome.module.podcast.page.viewmodel.ProgramsListDetailViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/27
 * @Description:
 *
 */
class ProgramsDisplay : BaseActivity() {
    private val binding by lazy { ActivityProgramsDisplayForStationBinding.inflate(layoutInflater) }
    private val model by lazy { ViewModelProvider(this)[ProgramsListDetailViewModel::class.java] }

    private val programsDisplayAdapter by lazy {
        ProgramsDisplayAdapter(
            ::onClickMore,
            ::onItemClick
        )
    }

    // 底部播放栏的view
    private lateinit var bottomPlay: ImageView
    private lateinit var bottomPic: ImageView   //下面的唱片view
    private lateinit var bottomAnimator: MyRotationAnimate  //下面的唱片的旋转动画


    // Service实例
    private lateinit var playService: MusicService  // 可播放具体节目

    // Service是否已绑定
    private var isServiceBinding: Boolean = false

    // Service的连接器
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playService = (service as MusicService.MusicPlayBinder).service
            isServiceBinding = true
            updateBottomInfo()   //得到底部播放栏信息得
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBinding = false
        }
    }

    // 通过其伴生对象的启动方法传入的参数电台rid
    private var rid: Long? = null

    // 仅当rid为null或0时才使用这个default rid
    private val defaultRid: Long = 336355127

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (intent != null)
            rid = intent.getLongExtra("rid", 0L)
        initSubscribe()
        initView()
        initClick()
        bindPlayService()
    }

    /**
     * 画面停摆后再恢复也要刷新当前的底部播放栏UI信息
     */
    override fun onStart() {
        super.onStart()
        if (isServiceBinding) {
            updateBottomInfo()
        }
    }

    /**
     *  页面不可见就必须停止该动画
     */
    override fun onStop() {
        super.onStop()
        bottomAnimator.stopAnimate()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        bottomAnimator.stopAnimate()
    }

    private fun initView() {
        // 底部viw
        bottomPlay = findViewById(com.handsome.lib.util.R.id.main_bottom_music_image_play)
        bottomPic = findViewById(com.handsome.lib.util.R.id.main_bottom_music_image_image)
        bottomAnimator = MyRotationAnimate(bottomPic)
        // toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // top Part
        initTopView()
        // rv
        initProgramsList()
    }

    private fun initSubscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.radioDetailResponseFlow.collectLatest {
                    if (it != null && it.code == 200) {
                        val detail = it.data
                        binding.apply {
                            programsRadioStationImgBackground.setImageFromUrl(detail.picUrl)
                            programsRadioStationTvName.text = detail.name
                            programsRadioStationTvSubCount.text = "关注量为：${detail.subCount}"
                            programsRadioStationTvDescribe.text = detail.dj.signature
                        }
                    }
                }
            }
        }
    }

    private fun initClick() {
        // 底部播放键的点击事件
        bottomPlay.setOnClickListener {
            if (!isServiceBinding || !playService.isPrepared)
                return@setOnClickListener
            if (playService.isPlaying()) { // 正在播放点击暂停
                bottomPlay.setImageResource(com.handsome.lib.util.R.drawable.icon_bottom_music_play)
                playService.pausePlay()
                bottomAnimator.stopAnimate()
            } else {                       // 正在暂停点击播放
                bottomPlay.setImageResource(com.handsome.lib.util.R.drawable.icon_bottom_music_stop)
                playService.startPlay()
                bottomAnimator.startAnimate()
            }
        }
        // 底部播放栏的点击事件
        (bottomPic.parent as ViewGroup).setOnClickListener {
            startActivity(Intent(this, MusicPlayActivity::class.java))
        }
        // 从头播放的点击事件
        binding.programsImgPlayAll.setOnClickListener {
            // 模拟点击第一条数据
            val firstView = binding.programsRvList.layoutManager?.findViewByPosition(0)
            firstView?.performClick()
        }
        binding.programsTvPlayAll.setOnClickListener {
            // 模拟点击第一条数据
            val firstView = binding.programsRvList.layoutManager?.findViewByPosition(0)
            firstView?.performClick()
        }

    }

    private fun bindPlayService() {
        Intent(this, MusicService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    private fun initTopView() {
        model.getRadioDetail(getAvailableRid())
    }

    /**
     * 初始化底部的节目列表
     */
    private fun initProgramsList() {
        // 初始化adapter
        binding.programsRvList.apply {
            layoutManager = LinearLayoutManager(
                this@ProgramsDisplay,
                RecyclerView.VERTICAL, false
            )
            adapter = programsDisplayAdapter
        }
        // 获取数据
        // 设置好adapter后让paging与数据和该adapter关联
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.getProgramsList(getAvailableRid()).catch {
                    it.printStackTrace()
                    it.message?.toast()
                }.collect {
                    programsDisplayAdapter.submitData(it)
                }
            }
        }
    }

    /**
     * 更新底部UI状态信息
     */
    private fun updateBottomInfo() {
        //获取当前歌名字歌手名字
        val programName = playService.getCurAudioName()
        val artistName = playService.getCurArtistName()
        val picUrl = playService.getCurAudioPicUrl()    //可能找不到
        findViewById<TextView>(com.handsome.lib.util.R.id.main_bottom_music_tv_name).text =
            programName
        findViewById<TextView>(com.handsome.lib.util.R.id.main_bottom_music_tv_singer).text =
            artistName
        if (picUrl != "找不到") {
            findViewById<ImageView>(com.handsome.lib.util.R.id.main_bottom_music_image_image).setImageFromUrl(
                picUrl
            )
        }
        if (playService.isPlaying()) {
            bottomPlay.setImageResource(com.handsome.lib.util.R.drawable.icon_bottom_music_stop)
            bottomAnimator.startAnimate()
        } else {
            bottomPlay.setImageResource(com.handsome.lib.util.R.drawable.icon_bottom_music_play)
            bottomAnimator.stopAnimate()
        }
    }


    private fun onClickMore(program: RadioProgramsData.Program) {
        toast("功能未实现")
    }

    /**
     * 节目列表的每一项点击后都会跳转到MusicPlayActivity
     * @param list
     * @param index
     */
    private fun onItemClick(list: MutableList<WrapPlayInfo>, index: Int) {
        MusicPlayActivity.startWithPlayList(this, list, index)
    }

    /**
     * 获取可用的rid
     */
    private fun getAvailableRid() = if (rid == null || rid == 0L) defaultRid else rid!!

    companion object {
        fun startWithSharedView(context: Activity, id: Long, sharedView: View) {
            val intent = Intent(context, ProgramsDisplay::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                context,
                sharedView,
                "RadioStationPicture"
            ).toBundle()
            intent.putExtra("rid", id)
            context.startActivity(intent, options)
        }
    }
}