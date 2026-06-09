package com.vidyavahini.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.vidyavahini.data.model.User
import com.vidyavahini.utils.Constants
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user!!
            // Save user profile to database
            val userModel = User(
                userId = user.uid,
                name = name,
                email = email,
                loginType = "email"
            )
            db.child(Constants.USERS_NODE).child(user.uid).setValue(userModel).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginAnonymously(): Result<FirebaseUser> {
        return try {
            val result = auth.signInAnonymously().await()
            val user = result.user!!
            val userModel = User(
                userId = user.uid,
                name = "Guest",
                loginType = "anonymous"
            )
            db.child(Constants.USERS_NODE).child(user.uid).setValue(userModel).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun isAdmin(uid: String): Boolean {
        return try {
            val snapshot = db.child(Constants.ADMINS_NODE).child(uid).get().await()
            snapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserProfile(uid: String): User? {
        return try {
            val snapshot = db.child(Constants.USERS_NODE).child(uid).get().await()
            snapshot.getValue(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUserRoute(uid: String, routeId: String) {
        try {
            db.child(Constants.USERS_NODE).child(uid).child("routeNumber").setValue(routeId).await()
        } catch (e: Exception) {
            // handle silently
        }
    }

    suspend fun markReachedSafely(uid: String) {
        try {
            db.child(Constants.USERS_NODE).child(uid).child("reachedSafely").setValue(true).await()
        } catch (e: Exception) {
            // handle silently
        }
    }
}
