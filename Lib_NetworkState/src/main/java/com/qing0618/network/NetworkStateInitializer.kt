package com.qing0618.network

import android.content.Context
import androidx.startup.Initializer

/**
 * Create by Qing at 2024/12/9 16:24
 */
internal class NetworkStateInitializer : Initializer<Context> {
    override fun create(context: Context): Context {
        NetworkFlow.init(context)
        return context
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}