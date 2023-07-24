package com.handsome.module.find.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.shareText
import com.handsome.module.find.databinding.ActivityTopListBinding
import com.handsome.module.find.network.exception.myCoroutineExceptionHandler
import com.handsome.module.find.network.model.TopListData
import com.handsome.module.find.view.adapter.TopListPictureAdapter
import com.handsome.module.find.view.adapter.TopListRvAdapter
import com.handsome.module.find.view.viewmodel.TopListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TopListActivity : BaseActivity() {
    private val mBinding by lazy { ActivityTopListBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[TopListViewModel::class.java] }
    private val mTopListRvAdapter by lazy { TopListRvAdapter(::onTopListClick) }
    private val mTopListPictureAdapter by lazy { TopListPictureAdapter(::onTopPictureListClick) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initTopList()
        initBarClick()
    }

    private fun initBarClick() {
        mBinding.apply {
            topListBarBack.setOnClickListener { finish() }
            topListBarShare.setOnClickListener {
                shareText("小帅哥快来玩啊: http://why.vin:2023/toplist/detail")
            }
        }
    }

    private fun initTopList() {
        initTopListRvAdapter()
        initCollect()
        getTopListData()
    }

    private fun getTopListData() {
        mViewModel.getTopList()
    }

    private fun initCollect() {
        lifecycleScope.launch(myCoroutineExceptionHandler){
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel.topListStateFlow.collectLatest {
                    if (it != null && it.code == 200){
                        //遍历，留下来有简单歌名人名的
                        val list = ArrayList<TopListData.Data>()
                        for (i in it.list){
                            if (i.tracks.isNotEmpty()){
                                list.add(i)
                            }else{
                                break
                            }
                        }
                        mTopListRvAdapter.submitList(list)
                        //排行榜下面图片
                        mTopListPictureAdapter.submitList(it.list)
                    }
                }
            }
        }
    }

    private fun initTopListRvAdapter() {
        mBinding.topListRv.apply {
            layoutManager = LinearLayoutManager(this@TopListActivity,RecyclerView.VERTICAL,false)
            adapter = mTopListRvAdapter

        }
        mBinding.topListPicRv.apply {
            layoutManager = GridLayoutManager(this@TopListActivity, 3, RecyclerView.VERTICAL, false)
            adapter = mTopListPictureAdapter
        }
    }


    private fun onTopListClick(data: TopListData.Data) {
        MusicListDetailActivity.startAction(this,data.id)
    }


    private fun onTopPictureListClick(data: TopListData.Data) {
        MusicListDetailActivity.startAction(this,data.id)
    }

    companion object{
        fun startAction(context : Context){
            val intent = Intent(context,TopListActivity::class.java)
            context.startActivity(intent)
        }
    }
}