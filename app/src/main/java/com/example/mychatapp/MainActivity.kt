package com.example.mychatapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mychatapp.ui.theme.MyChatAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.mychatapp.obj.Authentication
import com.example.mychatapp.obj.FireStoreDB
import com.example.mychatapp.obj.User
import com.example.mychatapp.view.HomePageScreen
import com.example.mychatapp.view.LoginOrSignUpScreen
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private var app = FirebaseApp.initializeApp(this)
    private var isUserAuthenticated by mutableStateOf(false)
    private var authentication = Authentication()
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyChatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    when {
                        isUserAuthenticated -> {
                            HomePageScreen(
                                user = currentUser,
                                onToast = { message ->
                                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                                },
                                onLogout = {
                                    // Logout action
                                    authentication.signOut {success ->
                                        if (success) {
                                            currentUser = null
                                            isUserAuthenticated = false
                                            Toast.makeText(this@MainActivity, "Logout Successfully!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                onUpdate = { newName ->
                                    FireStoreDB().updateUsername(currentUser!!.getUID(), newName) { success, message ->
                                        if (success) {
                                            currentUser!!.setName(newName)
                                        }
                                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                        else -> {
                            LoginOrSignUpScreen(
                                onSignIn = { credentials ->
                                    val username = credentials.username
                                    val password = credentials.password
                                    authentication.signIn(username, password) { user, error  ->
                                        if (user != null) {
                                            isUserAuthenticated = true
                                            currentUser = user
                                        } else {
                                            Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
                                        }
                                    }
                                },
                                onSignUp = { credentials ->
                                    val username = credentials.username
                                    val password = credentials.password
                                    authentication.signUp(username, password) { success, error->
                                        if (success) {
                                            Toast.makeText(this@MainActivity, "Sign Up Successfully!", Toast.LENGTH_LONG).show()
                                        } else {
                                            Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
                                        }
                                    }

                                }
                            )
                        }
                    }
                }
            }
        }
    }
}




