package com.handsome.lib.util.util

import android.content.Context
import android.content.Intent
import android.net.Uri


fun Context.shareText(shareText: String) {
    //设置为分享操作
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain" // 设置分享的内容类型为纯文本

    // 设置要分享的文本内容
    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)

    // 启动分享页面，createChooser用来弹出分享器
    startActivity(Intent.createChooser(shareIntent, "分享到")) // "分享到"是分享页面的标题，可以根据需要修改
}


fun Context.shareImage(imageStr: String) {
    val imageUri = Uri.parse(imageStr)
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "image/*" // 设置分享的内容类型为图片类型

    // 将图片的URI添加到Intent中
    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)

    // 启动分享页面
    startActivity(Intent.createChooser(shareIntent, "分享图片")) // "分享图片"是分享页面的标题，可以根据需要修改
}
