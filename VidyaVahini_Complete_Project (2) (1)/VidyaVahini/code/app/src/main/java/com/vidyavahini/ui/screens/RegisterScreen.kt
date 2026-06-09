package com.vidyavahini.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vidyavahini.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    isLoading: Boolean,
    error: String?,
    onRegister: (String, String, String) -> Unit,
    onBack: () -> Unit,
    onClearError: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Account") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Join Vidya-Vahini",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Register to get real-time bus updates",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(28.dp))

            (error ?: localError)?.let {
                ErrorBanner(message = it, onDismiss = {
                    localError = null
                    onClearError()
                })
                Spacer(modifier = Modifier.height(12.dp))
            }

            VVTextField(value = name, onValueChange = { name = it }, label = "Full Name")
            Spacer(modifier = Modifier.height(12.dp))
            VVTextField(value = email, onValueChange = { email = it }, label = "Email address")
            Spacer(modifier = Modifier.height(12.dp))
            VVTextField(value = password, onValueChange = { password = it }, label = "Password", isPassword = true)
            Spacer(modifier = Modifier.height(12.dp))
            VVTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirm Password", isPassword = true)

            Spacer(modifier = Modifier.height(28.dp))

            VVButton(
                text = "Create Account",
                onClick = {
                    when {
                        name.isBlank() -> localError = "Please enter your name"
                        email.isBlank() -> localError = "Please enter your email"
                        password.length < 6 -> localError = "Password must be at least 6 characters"
                        password != confirmPassword -> localError = "Passwords do not match"
                        else -> onRegister(name.trim(), email.trim(), password)
                    }
                },
                isLoading = isLoading,
                enabled = name.isNotBlank() && email.isNotBlank() && password.isNotBlank()
            )
        }
    }
}
