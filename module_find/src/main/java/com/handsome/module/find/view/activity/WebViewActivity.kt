package com.handsome.module.find.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.toast
import com.handsome.module.find.R
import com.handsome.module.find.databinding.ActivityWebViewBinding
import com.handsome.module.find.view.fragment.MyBSDialogFragment

class WebViewActivity : BaseActivity() {
    private val mBinding by lazy { ActivityWebViewBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        val title = intent.getStringExtra("title")
        val url = intent.getStringExtra("url")
        initWebView(url)
        initTitle(title)
        initBack()
        initShare()
    }

    private fun initShare() {
        //从底部弹窗
        mBinding.webBarShare.setOnClickListener {
            MyBSDialogFragment().show(supportFragmentManager,"MyBSDialogFragment")
        }
    }

    private fun initBack() {
        mBinding.webBarBack.setOnClickListener {
            finish()
        }
    }

    private fun initTitle(title: String?) {
        title?.let {
            mBinding.webBarTitle.text = it
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(url: String?) {
        try {
            mBinding.webWeb.apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                if (url != null) {
                    loadUrl(url)
                } else {
                    "加载失败".toast()
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
            "加载失败".toast()
        }

    }

    companion object {
        fun startAction(context: Context, url: String, title: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            context.startActivity(intent)
        }
    }
}