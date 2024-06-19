package ru.vsu.csf.bakebudget.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.ui.theme.SideBack

@Composable
fun Product(
    navController: NavHostController,
    product: ProductModel,
    productId: Int,
    retryHash: MutableState<Long>
) {
    val configuration = LocalConfiguration.current
    val mContext = LocalContext.current


    val screenWidth = configuration.screenWidthDp.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SideBack)
            .padding(8.dp)
    ) {
        if (product.url != null) {
            AsyncImage(
                modifier = Modifier
                    .height((screenWidth - 32.dp) / 2)
                    .width((screenWidth - 32.dp) / 2)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable(onClick = { navController.navigate("products/$productId") }),
                model = product.url,
                error = painterResource(R.drawable.error),
                placeholder = painterResource(id = R.drawable.loading2),
                onSuccess = {
                    retryHash.value++
                    Log.d("helpme", "Update $productId")
                },
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } else {
            if (product.uri != null) {
                AsyncImage(
                    modifier = Modifier
                        .height((screenWidth - 32.dp) / 2)
                        .width((screenWidth - 32.dp) / 2)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(onClick = { navController.navigate("products/$productId") }),
                    model = product.uri,
//                        placeholder = painterResource(id = R.drawable.loading),
                    onSuccess = { retryHash.value++ },
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(onClick = { navController.navigate("products/$productId") }),
                    contentScale = ContentScale.Fit,
                    painter = painterResource(id = product.iconId),
                    contentDescription = product.name
                )
            }
        }
        Text(text = product.name, modifier = Modifier.padding(start = 8.dp), fontSize = 16.sp)
    }
}