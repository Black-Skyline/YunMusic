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
import com.handsome.lib.music.MusicPlayActivity
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.setImageFromLocalUri
import com.handsome.lib.util.util.getSharePreference
import com.handsome.module.mine.databinding.FragmentMineBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MineFragment : BaseFragment() {
    private val mBinding by lazy { FragmentMineBinding.inflate(layoutInflater) }
    private val mLatestMusicAdapter by lazy { LatestMusicAdapter(::onClickMusic) }
    private val mViewModel by lazy { ViewModelProvider(this)[MineFragmentViewModel::class.java] }
    private val isStartActivity = false

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