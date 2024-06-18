package com.qure.data.utils

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isConnectNetwork: Flow<Boolean>
}