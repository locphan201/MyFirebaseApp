package com.example.mychatapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mychatapp.R
import com.example.mychatapp.obj.FireStoreDB
import com.example.mychatapp.obj.RealtimeDB
import com.example.mychatapp.obj.User
import com.example.mychatapp.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageScreen(
    user: User?,
    onToast: (String) -> Unit,
    onLogout: () -> Unit,
    onUpdate: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isDialogOpen by remember { mutableStateOf(false) }
        var announcementInput by remember { mutableStateOf("") }
        var isProfileOpen by remember { mutableStateOf(false) }

        if (isDialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when clicking outside the dialog or pressing the back button
                    isDialogOpen = false
                },
                title = { Text("Enter your room's name") },
                text = {
                    // TextField for input
                    TextField(
                        value = announcementInput,
                        onValueChange = { announcementInput = it },
                        label = { Text("Name") }
                    )
                },
                confirmButton = {
                    // Button to confirm the action
                    TextButton(
                        onClick = {
                            isDialogOpen = false
                            val userID = user?.getUID().toString()

                            RealtimeDB().createNewRoom(announcementInput) { roomID ->
                                if (roomID != null) {
                                    FireStoreDB().addNewRoomID(userID, roomID.toString()) { message ->
                                        user?.addNewRoom(roomID)
                                        onToast(message)
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    // Button to cancel the action and close the dialog
                    TextButton(
                        onClick = {
                            // Close the dialog without performing any action
                            isDialogOpen = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        when {
            isProfileOpen -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Purple40)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { isProfileOpen = false }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Profile",
                        style = TextStyle(
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.logout_icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { onLogout() }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                ProfileSettingScreen(user = user, onToast = onToast, onUpdate = onUpdate)
            }
            else -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Purple40)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.person_icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { isProfileOpen = true }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Chats",
                        style = TextStyle(
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.add_icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { isDialogOpen = true }
                    )
                }
                ChatRoomListScreen(user = user)
            }
        }


    }
}
