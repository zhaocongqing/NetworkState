package com.qing0618.network

/**
 * Create by Qing at 2024/12/9 15:30
 * @param netId 网络Id
 * @param transportWifi 是否是wifi网络 [NetworkCapabilities.TRANSPORT_WIFI]
 * @param transportCellular 是否是手机网络 [NetworkCapabilities.TRANSPORT_CELLULAR]
 * @param netCapabilityInternet 是否有网络连接 [NetworkCapabilities.NET_CAPABILITY_INTERNET]
 * @param netCapabilityValidated 网络是否已验证可用 [NetworkCapabilities.NET_CAPABILITY_VALIDATED]
 */
internal data class NetStateModel(
    val netId: String,
    val transportWifi: Boolean,
    val transportCellular: Boolean,
    val netCapabilityInternet: Boolean,
    val netCapabilityValidated: Boolean,
): NetworkState {

    override val id: String
        get() = netId

    override val isWifi: Boolean
        get() = transportWifi

    override val isCellular: Boolean
        get() = transportCellular

    override val isConnected: Boolean
        get() = netCapabilityInternet

    override val isValidated: Boolean
        get() = netCapabilityValidated
}

internal val NetStateNone: NetworkState = NetStateModel(
    netId = "",
    transportWifi = false,
    transportCellular = false,
    netCapabilityInternet = false,
    netCapabilityValidated = false,
)