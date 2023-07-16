package com.handsome.lib.util.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 定义的数据类包装类
 */
data class ApiWrapper<T>(
    @SerializedName("data")
    override val data: T,
    @SerializedName("errorCode")
    override val errorCode: Int,
    @SerializedName("errorMsg")
    override val errorMsg: String
) : IApiWrapper<T>

data class ApiStatus(
    @SerializedName("errorCode")
    override val errorCode: Int,
    @SerializedName("errorMsg")
    override val errorMsg: String
) : IApiStatus

interface IApiWrapper<T> : IApiStatus {
    val data: T
}

interface IApiStatus : Serializable {
    val errorCode: Int
    val errorMsg: String
}