package com.vidyavahini.utils

object Constants {
    const val USERS_NODE = "users"
    const val ROUTES_NODE = "routes"
    const val ADMINS_NODE = "admins"

    // Navigation routes
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val ROUTE_SELECTION = "route_selection"
    const val LIVE_STATUS = "live_status/{routeId}"
    const val ADMIN = "admin"
    const val SAFE_REACH = "safe_reach/{routeId}"

    fun liveStatusRoute(routeId: String) = "live_status/$routeId"
    fun safeReachRoute(routeId: String) = "safe_reach/$routeId"
}
