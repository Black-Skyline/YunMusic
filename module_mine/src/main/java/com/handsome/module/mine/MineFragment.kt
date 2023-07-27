package com.handsome.module.mine

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.handsome.lib.music.page.view.MusicPlayActivity
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.setImageFromLocalUri
import com.handsome.lib.util.util.getSharePreference
import com.handsome.lib.util.util.shareText
import com.handsome.module.mine.databinding.FragmentMineBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MineFragment : BaseFragment() {
    private val mBinding by lazy { FragmentMineBinding.inflate(layoutInflater) }
    private val mLatestMusicAdapter by lazy { LatestMusicAdapter(::onClickMore,::onClickMusic) }
    private val mViewModel by lazy { ViewModelProvider(this)[MineFragmentViewModel::class.java] }

    //从相册中拿结果
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            mBinding.musicListCollapsingImg.setImageFromLocalUri(uri)
            val edit = getSharePreference("user_img").edit()
            edit.putString("user_img",uri.toString())
            edit.apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //从本地读取照片
        fromLocalReadImg()
        initTopClick()
        initAdapter()
        getLatestData()
        initClickPlayAll()
        initClickLogin()
    }

    //登录操作
    private fun initClickLogin() {
        mBinding.musicListCollapsingTvUserName.setOnClickListener {
            //模块之间通信
            ARouter.getInstance().build("/login/gate").navigation()
        }
    }

    private fun onClickMore(wrapPlayInfo: WrapPlayInfo) {
        val shareText = "${wrapPlayInfo.artistName.trim()}的${wrapPlayInfo.audioName.trim()}特别好听，你也来听听吧！"
        requireContext().shareText(shareText)
    }

    private fun initClickPlayAll() {
        mBinding.musicListTvPlayAll.setOnClickListener {
            val firstView = mBinding.musicListRvMusic.layoutManager?.findViewByPosition(0)
            firstView?.performClick()
        }
    }

    private fun getLatestData() {
        lifecycleScope.launch(myCoroutineExceptionHandler){
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel.loadMusic().collectLatest {
                    mLatestMusicAdapter.submitData(it)
                }
            }
        }
    }

    private fun initAdapter() {
        mBinding.musicListRvMusic.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter = mLatestMusicAdapter
        }
    }

    //从本地读取用户头像
    private fun fromLocalReadImg() {
        val sp = getSharePreference("user_img")
        val img = sp.getString("user_img", null)
        if (img != null){
            val uri = Uri.parse(img)
            mBinding.musicListCollapsingImg.setImageFromLocalUri(uri)
        }
    }

    private fun initTopClick() {
        mBinding.musicListCollapsingTvChangeImg.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    //adapter点击事件
    private fun onClickMusic(list: MutableList<WrapPlayInfo>, index: Int) {
        MusicPlayActivity.startWithPlayList(requireContext(), list, index)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()
    }
}