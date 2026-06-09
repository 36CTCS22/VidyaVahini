package com.vidyavahini.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vidyavahini.data.model.Route
import com.vidyavahini.utils.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RouteRepository {

    private val db = FirebaseDatabase.getInstance().reference

    // Observe a single route in real-time (for live status screen)
    fun observeRoute(routeId: String): Flow<Route?> = callbackFlow {
        val ref = db.child(Constants.ROUTES_NODE).child(routeId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val route = snapshot.getValue(Route::class.java)
                trySend(route)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    // Observe all routes in real-time (for route selection)
    fun observeAllRoutes(): Flow<List<Route>> = callbackFlow {
        val ref = db.child(Constants.ROUTES_NODE)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val routes = mutableListOf<Route>()
                for (child in snapshot.children) {
                    val route = child.getValue(Route::class.java)
                    if (route != null) routes.add(route)
                }
                trySend(routes)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    // Admin: update route status
    suspend fun updateRouteStatus(
        routeId: String,
        currentLocation: String,
        eta: String,
        isDelayed: Boolean,
        isCancelled: Boolean,
        delayReason: String
    ) {
        val updates = mapOf(
            "currentLocation" to currentLocation,
            "eta" to eta,
            "isDelayed" to isDelayed,
            "isCancelled" to isCancelled,
            "delayReason" to delayReason,
            "lastUpdated" to System.currentTimeMillis()
        )
        db.child(Constants.ROUTES_NODE).child(routeId).updateChildren(updates).await()
    }

    // Admin: add a new route
    suspend fun addRoute(route: Route) {
        db.child(Constants.ROUTES_NODE).child(route.routeId).setValue(route).await()
    }

    // Get single route once (no real-time)
    suspend fun getRoute(routeId: String): Route? {
        return try {
            val snapshot = db.child(Constants.ROUTES_NODE).child(routeId).get().await()
            snapshot.getValue(Route::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
