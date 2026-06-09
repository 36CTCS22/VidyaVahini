// ========== User.kt ==========
package com.vidyavahini.data.model

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val routeNumber: String = "",
    val isAdmin: Boolean = false,
    val reachedSafely: Boolean = false,
    val loginType: String = "email" // "email" or "anonymous"
)

// ========== Route.kt ==========
// (Place this in a separate Route.kt file with the same package)
/*
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
*/

// ========== BusStatus.kt ==========
// (Place this in a separate BusStatus.kt file with the same package)
/*
package com.vidyavahini.data.model

enum class BusStatus {
    ON_TIME,
    DELAYED,
    CANCELLED,
    BREAKDOWN,
    ARRIVED
}
*/
