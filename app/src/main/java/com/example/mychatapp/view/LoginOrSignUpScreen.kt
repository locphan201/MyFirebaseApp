package com.example.mychatapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mychatapp.obj.UserCredentials

@Composable
fun ClickableTextButton(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = TextStyle(
            color = Color.Blue, // Set your desired text color
            textDecoration = TextDecoration.Underline, // Underline the text
            fontSize = 16.sp // Set your desired font size
        ),
        modifier = Modifier.clickable { onClick() }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginOrSignUpScreen(
    onSignIn: (UserCredentials) -> Unit,
    onSignUp: (UserCredentials) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isLogInScreen by remember { mutableStateOf(true) }
    val text = if (isLogInScreen) {
        "Login"
    } else {
        "Sign Up"
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text (
            text = text,
            style = TextStyle(
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLogInScreen) {
            ClickableTextButton("You don't have an account?") {
                isLogInScreen = false
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                keyboardController?.hide()
                onSignIn(UserCredentials(username, password))
            }) {
                Text("Sign In")
            }
        } else {
            ClickableTextButton("You have an account?") {
                isLogInScreen = true
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                keyboardController?.hide()
                onSignUp(UserCredentials(username, password))
            }) {
                Text("Sign Up")
            }
        }
    }
}