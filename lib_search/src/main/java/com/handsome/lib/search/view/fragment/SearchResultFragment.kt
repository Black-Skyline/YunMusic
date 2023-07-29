package com.handsome.lib.search.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.search.databinding.FragmentSearchResultBinding
import com.handsome.lib.search.network.model.SearchResultData
import com.handsome.lib.search.view.adapter.SearchResultRvAdapter
import com.handsome.lib.search.view.viewmodel.SearchResultFragmentViewModel
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.INVISIBLE
import com.handsome.lib.util.extention.VISIBLE
import com.handsome.lib.util.extention.toast
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchResultFragment(private val key : String,onClick : (SearchResultData.Result.Song) -> Unit) : BaseFragment() {
    private val mBinding by lazy { FragmentSearchResultBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[SearchResultFragmentViewModel::class.java] }
    private val mSearchResultPagingDataAdapter by lazy { SearchResultRvAdapter(onClick) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProgressBar()
    }

    //加载状态的设置
    private fun initProgressBar() {
        mSearchResultPagingDataAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    mBinding.searchProgressBar.INVISIBLE()
                    mBinding.searchResultRvMusic.VISIBLE()
                }
                is LoadState.Loading -> {
                    mBinding.searchProgressBar.VISIBLE()
                    mBinding.searchResultRvMusic.INVISIBLE()
                }
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    mBinding.searchProgressBar.INVISIBLE()
                    toast(state.error.message)
                    Log.d("lx", "initProgressBar:error=${state.error.message} ")
                }
            }
        }
    }

    private fun initSearchResult() {
        initSearchResultAdapter()
        getSearchData()
    }

    private fun getSearchData() {
        viewLifecycleOwner.lifecycleScope.launch{
            mViewModel.searchData(key).catch {
                it.printStackTrace()
            }.collectLatest {
                Log.d("lx", "getSearchData:我有拿到结果的(傲娇)${it} ")
                mSearchResultPagingDataAdapter.submitData(it)
            }
        }
    }

    private fun initSearchResultAdapter() {
        mBinding.searchResultRvMusic.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter = mSearchResultPagingDataAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initSearchResult()
        return mBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("lx", "搜索结果的fragment被摧毁了: ")
    }
}