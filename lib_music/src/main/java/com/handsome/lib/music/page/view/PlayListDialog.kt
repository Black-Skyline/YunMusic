package com.handsome.lib.music.page.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.handsome.lib.music.R
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.music.page.adapter.PlaylistAdapter
import com.handsome.lib.music.utils.ServiceHelper

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/25
 * @Description:
 *
 */
class PlayListDialog(private val parent: Activity, private val list: MutableList<WrapPlayInfo>, theme: Int) :
    BottomSheetDialog(parent, theme) {

    private val listAdapter by lazy { PlaylistAdapter(list, ::onItemClick) }

    private lateinit var playlistContent: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_play_list)
        initView()
        initConfig()
        initBehavior()
    }

    override fun onStart() {
        super.onStart()
        val view: FrameLayout =
            delegate.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
        val layoutParams: CoordinatorLayout.LayoutParams =
            view.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.height = getPhoneHeightOrWidth(0) * 3 / 5
        layoutParams.width = getPhoneHeightOrWidth(1) * 9 / 10
        view.layoutParams = layoutParams
        val behavior = BottomSheetBehavior.from(view)
        behavior.peekHeight = getPhoneHeightOrWidth(0) * 3 / 5
        behavior.state = STATE_EXPANDED
    }

    private fun initBehavior() {
        behavior.apply {
//            peekHeight = getPhoneHeightOrWidth(0) * 3 / 5
//            maxHeight = getPhoneHeightOrWidth(0) * 3 / 5
//            maxWidth = getPhoneHeightOrWidth(1) * 9 / 10

            addBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == STATE_HIDDEN)
                        dismiss()
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (slideOffset < -0.5)
                        state = STATE_HIDDEN
                }
            })

        }
    }

    private fun initConfig() {
        setCancelable(true)   // 支持拖拽关闭
        setCanceledOnTouchOutside(true)// 支持点击视图外部关闭

    }

    private fun initView() {
        playlistContent = findViewById(R.id.music_dialog_rv_playlist_content)!!
        playlistContent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
    }

    /**
     * 每一个列表项点击后回调的事件
     * @param item
     * @param index
     */
    private fun onItemClick(item: View, index: Int) {
        parent as MusicPlayActivity
        parent.serviceOperator.apply {
            updateCurSong(index)
            MusicPlayActivity.sentAudioNextOrPrevious(ServiceHelper.SENT_AUDIO_CHANGE_NEXT)
        }
    }

    /**
     * Get phone height or width
     *
     * @param type 0为获取高度，1为获取厚度
     * @return
     */
    private fun getPhoneHeightOrWidth(type: Int): Int {
        var realHeight = 0
        var realWidth = 0
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            // 实际显示区域指定包含系统装饰的内容的显示部分
            window?.windowManager!!.apply {
                val height = currentWindowMetrics.bounds.height()
                val width = currentWindowMetrics.bounds.width()
                val insets =
                    currentWindowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                realHeight = height - insets.bottom - insets.top
                realWidth = width - insets.right - insets.left
            }
        } else {
            // 获取减去系统栏的屏幕的高度和宽度
            val dm = DisplayMetrics()
            window?.windowManager!!.defaultDisplay.getRealMetrics(dm)
            realHeight = dm.heightPixels
            realWidth = dm.widthPixels
        }
        return if (type == 0) realHeight else realWidth
    }
}