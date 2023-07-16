package com.handsome.lib.util.extention

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import com.handsome.lib.util.R
import com.handsome.lib.util.network.getBaseUrl

/**
//1 ，2为了提升用户体验，1在具体图片显示出来之前，我们会先使用placeholder作为替代，2是如果请求出错，那么我们应该放置什么图片
//3 传入一段函数，用来配置glide函数
//4 对url进行一些操作，保证符合我们的需求
//5 使用3来配置glide
 */

fun ImageView.setImageFromUrl(
    url: String,  //必须传入一个url
    @DrawableRes placeholder: Int = R.drawable.config_ic_place_holder, //1
    @DrawableRes error: Int = R.drawable.config_ic_place_holder, //2
    func: (RequestBuilder<Drawable>.() -> Unit)? = null  //3
) {
    val realUrl = if (url.startsWith("http")) url else getBaseUrl() + url //4
    Glide.with(this)
        .load(realUrl)
        .apply(RequestOptions().placeholder(placeholder).error(error))
        .apply { func?.invoke(this) }  //5
        .into(this)
}

fun ImageView.setImageFromId(
    @DrawableRes id: Int,  //必须传入一个id
    @DrawableRes placeholder: Int = R.drawable.config_ic_place_holder,
    @DrawableRes error: Int = R.drawable.config_ic_place_holder,
    func: (RequestBuilder<Drawable>.() -> Unit)? = null
) {
    Glide.with(this)
        .load(id)
        .apply(RequestOptions().placeholder(placeholder).error(error))
        .apply { func?.invoke(this) }
        .into(this)
}