package com.qing0618.demo

import android.util.Log
import com.qing0618.network.NetworkState

/**
 * Create by Qing at 2024/12/9 17:41
 */
fun NetworkState.log() {
    val wifiOrCellular = when {
        isWifi -> "Wifi"
        isCellular -> "Cellular"
        else -> "None"
    }

    logMsg {
        """
         $wifiOrCellular
         id:${id}
         isConnected:${isConnected}
         isValidated:${isValidated}
         ${toString()}
        """.trimIndent()
    }
}

fun logMsg(function: () -> String) {
    Log.e("NetworkState", function())
}
