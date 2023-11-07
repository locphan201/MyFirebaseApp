package com.example.mychatapp.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.mychatapp.R
import com.example.mychatapp.obj.ImageStorage
import com.example.mychatapp.obj.User
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LoadImage(uri: String) {
    val context = LocalContext.current
    // Check if the image exists in local storage
    val file = File(context.cacheDir, uri.hashCode().toString())
    if (file.exists()) {
        // Image exists locally, load it using Coil or Picasso
        // Example using Coil:
        Image(
            painter = rememberImagePainter(data = file),
            contentDescription = null,
            modifier = Modifier.size(250.dp),
            contentScale = ContentScale.Crop
        )
    } else {
        val coroutineScope = rememberCoroutineScope()
        val imageLoader = ImageLoader.Builder(context)
            .diskCachePolicy(CachePolicy.DISABLED) // Disable disk caching for this example
            .build()

        coroutineScope.launch(Dispatchers.IO) {
            val request = ImageRequest.Builder(context)
                .data(uri)
                .build()

            when (val result = imageLoader.execute(request)) {
                is SuccessResult -> {
                    val bitmap = result.drawable.toBitmap()
                    saveBitmapToFile(bitmap, file)
                }
                else -> {
                    // Handle error loading image
                }
            }
        }

    }
}

// Function to save a bitmap to a file
private fun saveBitmapToFile(bitmap: Bitmap, file: File) {
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.close()
}

@SuppressLint("IntentReset")
@OptIn(coil.annotation.ExperimentalCoilApi::class)
@Composable
fun LoadImageFromUrlOrDefault(
    userID: String,
    onToast: (String) -> Unit
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher: ActivityResultLauncher<Intent> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val selectedImageUri: Uri? = result.data?.data

            ImageStorage().uploadImage(Uri.parse(selectedImageUri.toString()), userID) { success, message ->
                if (success) {
                    onToast(message)
                    imageUri = Uri.parse(selectedImageUri.toString())
                } else {
                    onToast(message)
                }
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .padding(8.dp)
            .clickable {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryIntent.type = "image/*"
                galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                galleryIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                coroutineScope.launch {
                    launcher.launch(galleryIntent)
                }
            }
    ) {
        ImageStorage().getImage(userID) { uri ->
            imageUri = uri
        }

        imageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
        } ?: run {
            Icon (
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingScreen(
    user: User?,
    onToast: (String) -> Unit,
    onUpdate: (String) -> Unit
) {
    var isEditMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(user?.getName().toString()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadImageFromUrlOrDefault(
            userID = user?.getUID().toString(),
            onToast = { message ->
                onToast(message)
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isEditMode) {
            TextField (
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cancel_icon),
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            name = user?.getName().toString()
                            isEditMode = false
                        }
                )


                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    painter = painterResource(id = R.drawable.done_icon),
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            onUpdate(name)
                            isEditMode = false
                        }
                )
            }

        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    painter = painterResource(id = R.drawable.edit_icon),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            isEditMode = true
                        }
                )
            }
        }
    }
}