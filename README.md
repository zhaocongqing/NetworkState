### 网络状态监听工具库
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

