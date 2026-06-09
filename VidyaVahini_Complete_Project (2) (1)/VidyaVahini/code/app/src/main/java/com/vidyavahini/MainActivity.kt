package com.vidyavahini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vidyavahini.ui.screens.*
import com.vidyavahini.ui.theme.VidyaVahiniTheme
import com.vidyavahini.utils.Constants
import com.vidyavahini.viewmodel.AdminViewModel
import com.vidyavahini.viewmodel.AuthViewModel
import com.vidyavahini.viewmodel.RouteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VidyaVahiniTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VidyaVahiniApp()
                }
            }
        }
    }
}

@Composable
fun VidyaVahiniApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val routeViewModel: RouteViewModel = viewModel()
    val adminViewModel: AdminViewModel = viewModel()

    val authState by authViewModel.uiState.collectAsState()
    val routes by routeViewModel.routes.collectAsState()
    val currentRoute by routeViewModel.currentRoute.collectAsState()
    val routeIsLoading by routeViewModel.isLoading.collectAsState()
    val adminState by adminViewModel.uiState.collectAsState()
    val adminRoutes by adminViewModel.routes.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Constants.SPLASH
    ) {
        // Splash
        composable(Constants.SPLASH) {
            SplashScreen(
                isLoggedIn = authState.isLoggedIn,
                isAdmin = authState.isAdmin,
                onNavigate = { dest ->
                    navController.navigate(dest) {
                        popUpTo(Constants.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // Login
        composable(Constants.LOGIN) {
            LoginScreen(
                isLoading = authState.isLoading,
                error = authState.error,
                onLogin = { email, pass -> authViewModel.login(email, pass) },
                onNavigateToRegister = { navController.navigate(Constants.REGISTER) },
                onAnonymousLogin = { authViewModel.loginAnonymously() },
                onClearError = { authViewModel.clearError() }
            )
            // Navigate when login succeeds
            LaunchedEffect(authState.isLoggedIn) {
                if (authState.isLoggedIn) {
                    val dest = if (authState.isAdmin) Constants.ADMIN else Constants.ROUTE_SELECTION
                    navController.navigate(dest) {
                        popUpTo(Constants.LOGIN) { inclusive = true }
                    }
                }
            }
        }

        // Register
        composable(Constants.REGISTER) {
            RegisterScreen(
                isLoading = authState.isLoading,
                error = authState.error,
                onRegister = { name, email, pass -> authViewModel.register(name, email, pass) },
                onBack = { navController.popBackStack() },
                onClearError = { authViewModel.clearError() }
            )
            LaunchedEffect(authState.isLoggedIn) {
                if (authState.isLoggedIn) {
                    navController.navigate(Constants.ROUTE_SELECTION) {
                        popUpTo(Constants.LOGIN) { inclusive = true }
                    }
                }
            }
        }

        // Route Selection
        composable(Constants.ROUTE_SELECTION) {
            LaunchedEffect(Unit) { routeViewModel.loadAllRoutes() }
            RouteSelectionScreen(
                userName = authState.user?.name ?: "Student",
                routes = routes,
                isLoading = routeIsLoading,
                onSelectRoute = { route ->
                    authViewModel.updateUserRoute(route.routeId)
                    navController.navigate(Constants.liveStatusRoute(route.routeId))
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Constants.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Live Status
        composable(
            route = Constants.LIVE_STATUS,
            arguments = listOf(navArgument("routeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val routeId = backStackEntry.arguments?.getString("routeId") ?: return@composable
            LaunchedEffect(routeId) { routeViewModel.observeRoute(routeId) }
            LiveStatusScreen(
                route = currentRoute,
                isLoading = currentRoute == null,
                onBack = { navController.popBackStack() },
                onSafeReach = {
                    navController.navigate(Constants.safeReachRoute(routeId))
                }
            )
        }

        // Safe Reach
        composable(
            route = Constants.SAFE_REACH,
            arguments = listOf(navArgument("routeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val routeId = backStackEntry.arguments?.getString("routeId") ?: return@composable
            SafeReachScreen(
                routeName = currentRoute?.routeName ?: routeId,
                alreadyReached = authState.user?.reachedSafely ?: false,
                onConfirm = { authViewModel.markReachedSafely() },
                onBack = { navController.popBackStack() }
            )
        }

        // Admin
        composable(Constants.ADMIN) {
            AdminScreen(
                routes = adminRoutes,
                isLoading = adminState.isLoading,
                updateSuccess = adminState.updateSuccess,
                error = adminState.error,
                onUpdateRoute = { id, loc, eta, delayed, cancelled, reason ->
                    adminViewModel.updateRouteStatus(id, loc, eta, delayed, cancelled, reason)
                },
                onAddRoute = { id, name -> adminViewModel.addNewRoute(id, name) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Constants.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onClearState = { adminViewModel.clearState() }
            )
        }
    }
}
