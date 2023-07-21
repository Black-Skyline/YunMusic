package com.handsome.lib.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.handsome.lib.search.databinding.ActivitySearchBinding
import com.handsome.lib.search.network.model.SearchResultData
import com.handsome.lib.search.network.model.SearchSuggestionData
import com.handsome.lib.search.network.myCoroutineExceptionHandler
import com.handsome.lib.search.view.fragment.SearchResultFragment
import com.handsome.lib.search.view.fragment.SearchSuggestionFragment
import com.handsome.lib.search.view.viewmodel.SearchActivityViewModel
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.GONE
import com.handsome.lib.util.extention.VISIBLE
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.gsonSaveToSp
import com.handsome.lib.util.util.objectFromSp
import kotlinx.coroutines.launch

class SearchActivity : BaseActivity() {
    private val mBinding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[SearchActivityViewModel::class.java] }
    private var mTv: TextView? = null
    private var list: ArrayList<String> = ArrayList()
    private var mSearchSuggestionFragment: SearchSuggestionFragment? = null
    private var mSearchResultFragment: SearchResultFragment? = null
    private var mHandler: Handler = Handler(Looper.getMainLooper())
    private var mRunnable: Runnable? = null
    private val delayTime: Long = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
//        gsonSaveToSp(null,"search_history")
        initSearch()
        initSearchHistory()
    }

    //初始化搜索历史
    private fun initSearchHistory() {
        val list1 = objectFromSp<List<String>>("search_history") ?: return
        list.addAll(list1)
        if (list.isNotEmpty()) {
            //非空就遍历增加view然后设置点击事件
            for (i in list) {
                addSearchHistoryView(" $i ")
            }
            initSearchHistoryClick()
        }
    }

    //一个个添加搜索历史
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addSearchHistoryView(searchHistory: String) {
        mTv = TextView(this)
        mTv?.let {
            it.background = resources.getDrawable(R.drawable.shape_backgroud, theme)
            it.text = searchHistory
            mBinding.searchHistoryFlow.addView(mTv)
        }
    }

    //遍历子view，然后添加点击事件
    private fun initSearchHistoryClick() {
        val childCount = mBinding.searchHistoryFlow.childCount
        for (i in 0 until childCount) {
            val child = mBinding.searchHistoryFlow.getChildAt(i)
            child.setOnClickListener {
                if (child is TextView) {
                    onSearchHistoryClick(child.text.toString())
                }
            }
        }
    }

    //初始化搜索的逻辑
    private fun initSearch() {
        mBinding.searchSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //提交搜索的时候
            override fun onQueryTextSubmit(query: String?): Boolean {
                mRunnable?.let { mHandler.removeCallbacks(it) }  //提交的时候将之前延时任务删除掉
                doAfterSubmit(query)
                //表示已经处理，系统无需再次处理
                return true
            }

            //搜索改变的时候
            override fun onQueryTextChange(newText: String?): Boolean {
                mRunnable?.let { mHandler.removeCallbacks(it) } //改变的时候将之前延时任务删除掉
                mSearchResultFragment?.let { removeFragment(it) } //当改变的时候如果搜索结果还在就移除
                mRunnable = Runnable {
                    doAfterChange(newText)
                }
                mHandler.postDelayed(mRunnable!!, delayTime)
                return false
            }
        })
    }

    //当用户提交之后的操作
    private fun doAfterSubmit(query: String?) {
        //开启一个新的fragment并且在新的fragment中网络请求
        //每次保存下来搜索历史,将搜索历史在创建时传递给fragment
        if (query != null && query != "") {
            mSearchResultFragment = SearchResultFragment(query, ::onClickSearchResult) //创建新的
            replaceFragment(mSearchResultFragment!!)  //将这个fragment放进去
            list.add(query)
            gsonSaveToSp(list ,"search_history")
        }
    }

    private fun doAfterChange(newText: String?) {
        if (newText == null) return
        //搜索改变的时候的搜索提示
        if (newText != "") {
            mBinding.searchFragmentContainer.VISIBLE()  //设置可见
            mSearchSuggestionFragment?.let { removeFragment(it) }  //移除老的
            mSearchSuggestionFragment = SearchSuggestionFragment(newText, ::onSearchSuggestionClick)
            replaceFragment(mSearchSuggestionFragment!!)   //展示新的
        } else {
            mBinding.searchFragmentContainer.GONE()  //当啥也没有的时候显示本来的面目
        }
    }

    //点击搜索结果之后
    private fun onClickSearchResult(song: SearchResultData.Result.Song) {
        //todo 测试
        song.name.toast()
    }

    private fun onSearchHistoryClick(searchHistory: String) {
        // 点击到搜索历史之后的事情
        setSearchViewQuery(searchHistory)
    }

    private fun onSearchSuggestionClick(match: SearchSuggestionData.Result.AllMatch) {
        setSearchViewQuery(match.keyword)
    }

    private fun setSearchViewQuery(key: String) {
        mBinding.searchSearch.setQuery(key, true)
    }

    //更新fragment的操作
    private fun replaceFragment(fragment: Fragment) {
        if (fragment.isAdded) {
            return
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.apply {
            add(R.id.search_fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun removeFragment(fragment: Fragment) {
        if (fragment in supportFragmentManager.fragments) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.apply {
                remove(fragment)
                commit()
            }
        }
    }

    private fun removeAllFragment(){
        for (fragment in supportFragmentManager.fragments){
            fragment.onDestroy()
        }
    }

    companion object {
        fun startAction(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }
}