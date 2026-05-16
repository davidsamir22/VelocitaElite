package com.velocita.elite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velocita.elite.ui.theme.VelocitaColors
import com.velocita.elite.viewmodel.MainViewModel

@Composable
fun EnrollmentScreen(
    viewModel: MainViewModel,
    onEnrolled: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isEmailValid = email.contains("@")
    val isPasswordValid = password.length >= 8

    val nameError = remember(name) { name.isNotEmpty() && name.length < 2 }
    val emailError = remember(email) { email.isNotEmpty() && !isEmailValid }
    val passwordError = remember(password) { password.isNotEmpty() && !isPasswordValid }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VelocitaColors.Obsidian)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "VELOCITÀ",
                style = MaterialTheme.typography.displayMedium.copy(
                    color = VelocitaColors.GoldPrimary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 8.sp
                )
            )
            Text(
                text = "ENROLLMENT",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = VelocitaColors.SilverMid,
                    letterSpacing = 4.sp
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("FULL NAME") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = nameError,
                singleLine = true,
                colors = textFieldColors(nameError)
            )
            if (nameError) ErrorText("Name is too short")

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("EMAIL") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                isError = emailError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = textFieldColors(emailError)
            )
            if (emailError) ErrorText("Invalid email format (must contain @)")

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("PASSWORD") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                isError = passwordError,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                colors = textFieldColors(passwordError)
            )
            if (passwordError) ErrorText("Password must be at least 8 characters")

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { if (isEmailValid && isPasswordValid && name.length >= 2) onEnrolled() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VelocitaColors.GoldPrimary,
                    contentColor = VelocitaColors.Obsidian
                ),
                enabled = isEmailValid && isPasswordValid && name.length >= 2
            ) {
                Text("REQUEST ACCESS", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text(
                    "ALREADY AN OWNER? SIGN IN",
                    color = VelocitaColors.SilverMid,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun ErrorText(text: String) {
    Text(
        text = text,
        color = VelocitaColors.ErrorRed,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 4.dp)
    )
}

@Composable
private fun textFieldColors(isError: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = if (isError) VelocitaColors.ErrorRed else VelocitaColors.GoldPrimary,
    unfocusedBorderColor = if (isError) VelocitaColors.ErrorRed else VelocitaColors.CharcoalLight,
    focusedLabelColor = if (isError) VelocitaColors.ErrorRed else VelocitaColors.GoldPrimary,
    cursorColor = VelocitaColors.GoldPrimary
)
