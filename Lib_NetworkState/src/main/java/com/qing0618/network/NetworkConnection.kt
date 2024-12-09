package com.qing0618.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

/**
 * Create by Qing at 2024/12/9 16:03
 */
internal class NetworkConnection(
    private val manager: ConnectivityManager,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) {
    private val _networks = mutableMapOf<Network, NetworkState>()
    private val _networksFlow = MutableStateFlow<List<NetworkState>?>(null)

    /**
     * 当前网络状态
     */
    val currentNetwork: NetworkState
        get() = manager.currentNetworkState() ?: NetStateNone

    /**
     * 监听所有网络
     */
    val allNetworksFlow: Flow<List<NetworkState>> = _networksFlow.filterNotNull()

    /**
     * 注册网络监听
     */
    private suspend fun registerNetworkListener() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        while (true) {
            val register = try {
                manager.registerNetworkCallback(request, _networkCallback)
                true
            } catch (e: RuntimeException) {
                e.printStackTrace()
                false
            }

            val list = manager.currentNetworkState().let { networkState ->
                if (networkState == null) {
                    emptyList()
                } else {
                    listOf(networkState)
                }
            }

            if (register) {
                // 这里使用compareAndSet因为registerNetworkListener的时候可能已经回调了网络状态
                _networksFlow.compareAndSet(null, list)
                break
            } else {
                _networksFlow.value = list
                delay(1_000)
                continue
            }
        }
    }

    /**
     * 监听当前网络
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentNetworkFlow: Flow<NetworkState> = allNetworksFlow
        .mapLatest(::filterCurrentNetwork)
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)

    /**
     * 网络回调
     */
    private val _networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            _networks.remove(network)
            syncNetworksFlow()
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            _networks[network] = createNetworkState(network, networkCapabilities)
            syncNetworksFlow()
        }

        private fun syncNetworksFlow() {
            _networksFlow.value = _networks.values.toList()
        }
    }

    /**
     * 过滤当前网络
     */
    private suspend fun filterCurrentNetwork(list: List<NetworkState>): NetworkState {
        if (list.isEmpty()) return NetStateNone
        while (true) {
            val target = list.find { it.id == manager.activeNetwork?.netId() }
            if (target != null) {
                return target
            } else {
                delay(1_000)
                continue
            }
        }
    }

    init {
        scope.launch {
            registerNetworkListener()
        }
    }
}

private fun ConnectivityManager.currentNetworkState(): NetworkState? {
    val network = this.activeNetwork ?: return null
    val capabilities = this.getNetworkCapabilities(network) ?: return null
    return createNetworkState(network, capabilities)
}

/**
 * 创建网络状态
 */
private fun createNetworkState(
    network: Network,
    networkCapabilities: NetworkCapabilities,
): NetworkState {
    return NetStateModel(
        netId = network.netId(),
        transportWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI),
        transportCellular = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR),
        netCapabilityInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET),
        netCapabilityValidated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED),
    )
}

private fun Network.netId(): String = this.toString()