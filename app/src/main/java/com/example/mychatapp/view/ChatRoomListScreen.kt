package com.example.mychatapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mychatapp.obj.User

@Composable
fun ChatRoomCard(
    roomID: String,
    roomName: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = 5.dp)
            .clickable { onClick() }
    ) {
        Spacer(modifier = Modifier.width(5.dp))
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .padding(8.dp)
        ) {
            Icon (
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = roomName,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Composable
fun ChatRoomListScreen(
    user: User?
) {
    var roomIDs by remember { mutableStateOf<ArrayList<String>?>(null) }

    roomIDs = user?.getRoomIDs()

    var selectedRoomID by remember { mutableStateOf("") }


    LazyColumn(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(selectedRoomID)
        }

        item {
            Spacer(modifier = Modifier.height(5.dp))
        }

        if (roomIDs != null) {
            items(roomIDs!!) { roomID ->
                ChatRoomCard(roomID = roomID, roomName = "Room $roomID", onClick = { selectedRoomID = roomID })
            }
        } else {
            item {
                Text("No Chat Yet")
            }
        }
    }



}
