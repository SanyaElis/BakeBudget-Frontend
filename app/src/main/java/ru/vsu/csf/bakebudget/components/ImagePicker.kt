package ru.vsu.csf.bakebudget.components

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack

@Composable
fun ImagePicker(selectedImageUri: MutableState<Uri?>, uri: Uri?, url: String?) {
    val mContext = LocalContext.current


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let { uri ->
                val sizeInBytes = getFileSize(mContext, uri)
                if (sizeInBytes > 4 * 1024 * 1024) { // 4 MB в байтах
                    Toast.makeText(mContext, "Размер изображения превышает 4 МБ", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    selectedImageUri.value = uri
                }
            }
        }
    )

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Добавьте изображение",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.size(20.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBack,
                contentColor = Color.White
            ),
            onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = android.R.drawable.ic_input_add),
                    contentDescription = "Add Image"
                )
                Text(
                    text = "Выбрать изображение",
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.size(20.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (url != null) {
                AsyncImage(
                    modifier = Modifier
                        .height((screenWidth-32.dp)/2)
                        .width((screenWidth-32.dp)/2)
                        .clip(RoundedCornerShape(14.dp)),
                    model = if (selectedImageUri.value == null) url else selectedImageUri.value,
                    error = painterResource(R.drawable.error),
                    placeholder = painterResource(id = R.drawable.loading2),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    modifier = Modifier
                        .height((screenWidth - 32.dp) / 2)
                        .width((screenWidth - 32.dp) / 2)
                        .clip(RoundedCornerShape(14.dp)),
                    model = if (uri != null && selectedImageUri.value == null) uri else selectedImageUri.value,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }

    }
}

fun getFileSize(context: Context, uri: Uri): Long {
    val cursor = context.contentResolver.query(uri, null, null, null, null) ?: return 0
    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
    cursor.moveToFirst()
    val size = cursor.getLong(sizeIndex)
    cursor.close()
    return size
}