package com.handsome.lib.search.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.search.databinding.FragmentSearchResultBinding
import com.handsome.lib.search.network.model.SearchResultData
import com.handsome.lib.search.view.adapter.SearchResultRvAdapter
import com.handsome.lib.search.view.viewmodel.SearchResultFragmentViewModel
import com.handsome.lib.util.base.BaseFragment
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchResultFragment(private val key : String,private val onClick : (SearchResultData.Result.Song) -> Unit) : BaseFragment() {
    private val mBinding by lazy { FragmentSearchResultBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[SearchResultFragmentViewModel::class.java] }
    private val mSearchResultPagingDataAdapter by lazy { SearchResultRvAdapter(onClick) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSearchResult()
    }

    private fun initSearchResult() {
        initSearchResultAdapter()
        getSearchData()
    }

    private fun getSearchData() {
        lifecycleScope.launch{
            mViewModel.searchData(key).catch {
                it.printStackTrace()
            }.collectLatest {
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
        return mBinding.root
    }
}