package com.handsome.module.find.view.fragment

import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.handsome.lib.util.extention.toast
import com.handsome.module.find.databinding.BottomSheetShareBinding


class MyBSDialogFragment : BottomSheetDialogFragment() {

    private val mBinding by lazy { BottomSheetShareBinding.inflate(layoutInflater) }
    private lateinit var dialog : Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(mBinding.root)
        initView()
        return dialog
    }

    private fun initView() {
        mBinding.bottomSheetShareQq.setOnClickListener {
            "假如分享到qq了".toast()
            val comp = ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity")
            val shareIntent = Intent()
            shareIntent.component = comp
//            shareIntent.action = Intent.ACTION_SEND_MULTIPLE
////            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, )
//            shareIntent.type = "image/*"
            startActivity(Intent.createChooser(shareIntent, "分享多张图片"))
            dialog.cancel()
        }
    }
}
