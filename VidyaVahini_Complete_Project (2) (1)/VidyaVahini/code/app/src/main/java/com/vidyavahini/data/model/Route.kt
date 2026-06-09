package com.vidyavahini.data.model

data class Route(
    val routeId: String = "",
    val routeName: String = "",
    val currentLocation: String = "Not started",
    val eta: String = "Unknown",
    val isDelayed: Boolean = false,
    val isCancelled: Boolean = false,
    val delayReason: String = "",
    val lastUpdated: Long = 0L
)
