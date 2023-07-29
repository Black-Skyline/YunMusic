package com.handsome.module.find.view.fragment

import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.shareText
import com.handsome.module.find.databinding.BottomSheetShareBinding


class MyBSDialogFragment(val text : String) : BottomSheetDialogFragment() {

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
            requireContext().shareText(text)
            dialog.cancel()
        }
    }
}
