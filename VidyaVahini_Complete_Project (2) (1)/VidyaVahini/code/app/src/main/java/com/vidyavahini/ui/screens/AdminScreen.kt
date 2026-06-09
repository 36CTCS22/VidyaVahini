package com.vidyavahini.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vidyavahini.data.model.Route
import com.vidyavahini.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    routes: List<Route>,
    isLoading: Boolean,
    updateSuccess: Boolean,
    error: String?,
    onUpdateRoute: (String, String, String, Boolean, Boolean, String) -> Unit,
    onAddRoute: (String, String) -> Unit,
    onLogout: () -> Unit,
    onClearState: () -> Unit
) {
    var selectedRoute by remember { mutableStateOf<Route?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            selectedRoute = null
            onClearState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Route")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            error?.let {
                ErrorBanner(message = it, onDismiss = onClearState)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (updateSuccess) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Route updated successfully!",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                text = "Tap a route to update its status",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(routes) { route ->
                    AdminRouteCard(route = route, onClick = { selectedRoute = route })
                }
            }
        }
    }

    // Edit Route Dialog
    selectedRoute?.let { route ->
        EditRouteDialog(
            route = route,
            isLoading = isLoading,
            onUpdate = { loc, eta, delayed, cancelled, reason ->
                onUpdateRoute(route.routeId, loc, eta, delayed, cancelled, reason)
            },
            onDismiss = { selectedRoute = null }
        )
    }

    // Add Route Dialog
    if (showAddDialog) {
        AddRouteDialog(
            onAdd = { id, name -> onAddRoute(id, name); showAddDialog = false },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
fun AdminRouteCard(route: Route, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.DirectionsBus,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(route.routeName, fontWeight = FontWeight.SemiBold)
                Text(
                    "At: ${route.currentLocation} | ETA: ${route.eta}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            StatusChip(isDelayed = route.isDelayed, isCancelled = route.isCancelled)
        }
    }
}

@Composable
fun EditRouteDialog(
    route: Route,
    isLoading: Boolean,
    onUpdate: (String, String, Boolean, Boolean, String) -> Unit,
    onDismiss: () -> Unit
) {
    var location by remember { mutableStateOf(route.currentLocation) }
    var eta by remember { mutableStateOf(route.eta) }
    var isDelayed by remember { mutableStateOf(route.isDelayed) }
    var isCancelled by remember { mutableStateOf(route.isCancelled) }
    var delayReason by remember { mutableStateOf(route.delayReason) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update: ${route.routeName}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                VVTextField(value = location, onValueChange = { location = it }, label = "Current Location")
                VVTextField(value = eta, onValueChange = { eta = it }, label = "ETA (e.g. 10 mins)")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isDelayed, onCheckedChange = { isDelayed = it })
                    Text("Mark as Delayed")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isCancelled, onCheckedChange = { isCancelled = it })
                    Text("Mark as Cancelled")
                }
                if (isDelayed || isCancelled) {
                    VVTextField(value = delayReason, onValueChange = { delayReason = it }, label = "Reason")
                }
            }
        },
        confirmButton = {
            VVButton(
                text = "Update",
                onClick = { onUpdate(location, eta, isDelayed, isCancelled, delayReason) },
                isLoading = isLoading,
                modifier = Modifier.width(120.dp)
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddRouteDialog(
    onAdd: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var routeId by remember { mutableStateOf("") }
    var routeName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Route") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                VVTextField(value = routeId, onValueChange = { routeId = it }, label = "Route ID (e.g. route3)")
                VVTextField(value = routeName, onValueChange = { routeName = it }, label = "Route Name")
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(routeId.trim(), routeName.trim()) },
                enabled = routeId.isNotBlank() && routeName.isNotBlank()
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
