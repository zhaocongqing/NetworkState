package com.qing0618.demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.qing0618.network.NetworkObserver
import com.qing0618.network.NetworkState

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 注册
        _networkObserver.registerNetworkListener()
    }

    /**
     * 常规监听网络状态
     */
    private val _networkObserver = object : NetworkObserver() {
        override fun onChange(networkState: NetworkState) {
            networkState.log()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消注册
        _networkObserver.unregisterNetworkListener()
    }
}