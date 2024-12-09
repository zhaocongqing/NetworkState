package com.qing0618.network

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * 监听当前网络状态
 * Create by Qing at 2024/12/9 15:46
 */
abstract class NetworkObserver {

    private val _scope = MainScope()
    private var _job: Job? = null

    /**
     * 注册网络监听
     */
    @Synchronized
    fun registerNetworkListener() {
        if (_job == null) {
            _job = _scope.launch {
                NetworkFlow.currentNetworkFlow.collect{
                    onChange(it)
                }
            }
        }
    }

    /**
     * 取消网络监听
     */
    @Synchronized
    fun unregisterNetworkListener() {
        _job?.cancel()
        _job = null
    }

    /**
     * 当前网络状态变化(MainThread)
     */
    protected abstract fun onChange(networkState: NetworkState)
}