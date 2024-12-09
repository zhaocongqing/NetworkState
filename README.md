### 网络状态监听工具库
- 使用androidx.startup.Initializer初始化，默认在主进程自动初始化，如果要在其他进程使用，需要在其他进程手动初始化
```kotlin
NetworkFlow.init(context)
```

- 获取当前网络状态
```kotlin
val currentNetwork = NetworkFlow.currentNetwork
```

- 监听当前网络状态
```kotlin
// 使用Flow监听网络状态
lifecycleScope.launch {
    NetworkFlow.currentNetworkFlow.collect { networkState: NetworkState ->
        networkState.log()
    }
}
```

- 常规监听使用方式
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
```