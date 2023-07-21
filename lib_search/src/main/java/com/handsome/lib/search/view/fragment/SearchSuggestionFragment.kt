package com.handsome.lib.search.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.search.databinding.FragmentSearchSuggestionBinding
import com.handsome.lib.search.network.model.SearchSuggestionData
import com.handsome.lib.search.network.myCoroutineExceptionHandler
import com.handsome.lib.search.view.adapter.SearchSuggestionRvAdapter
import com.handsome.lib.search.view.viewmodel.SearchSuggestionFragmentViewModel
import com.handsome.lib.util.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SearchSuggestionFragment(private val keywords : String, onSearchSuggestionClick: (SearchSuggestionData.Result.AllMatch) -> Unit) : BaseFragment() {
    private val mBinding by lazy { FragmentSearchSuggestionBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[SearchSuggestionFragmentViewModel::class.java] }
    private val mSearchSuggestionRvAdapter = SearchSuggestionRvAdapter(onSearchSuggestionClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSearchSuggestion()
    }

    private fun initSearchSuggestion() {
        initAdapter()
        initCollect()
        getSearchSuggestion()
    }

    private fun initCollect() {
        lifecycleScope.launch(myCoroutineExceptionHandler){
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel.searchSuggestionStateFlow.collectLatest {
                    if (it != null && it.code == 200){
                        mSearchSuggestionRvAdapter.submitList(it.result.allMatch)
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        mBinding.searchSuggestionRv.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter = mSearchSuggestionRvAdapter
        }
    }

    private fun getSearchSuggestion() {
        mViewModel.getSearchSuggestion(keywords)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    fun changeData(key : String){
        Log.d("lx", "key: ")
        mViewModel.getSearchSuggestion(key)
    }

}