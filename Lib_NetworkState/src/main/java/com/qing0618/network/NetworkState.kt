package com.qing0618.network

/**
 * Create by Qing at 2024/12/9 15:30
 */
interface NetworkState {

    /**
     * 网络Id
     */
    val id: String

    /**
     * 是否Wifi网络
     */
    val isWifi: Boolean

    /**
     * 是否手机网络
     */
    val isCellular: Boolean

    /**
     * 网络是否已连接，已连接不代表网络一定可用
     */
    val isConnected: Boolean

    /**
     * 网络是否已验证可用
     */
    val isValidated: Boolean
}