package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.ui.theme.SideBack

@Composable
fun Product(
    navController: NavHostController,
    product: ProductModel,
    productId: Int
) {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SideBack)
            .padding(8.dp)
    ) {
        if (product.url != null) {
            Image(
                painter = rememberAsyncImagePainter(product.url),
                contentDescription = null,
                modifier = Modifier
                    .height((screenWidth - 32.dp) / 2)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable(onClick = { navController.navigate("products/$productId") }),
                contentScale = ContentScale.Crop
            )
        } else {
            if (product.uri != null) {
                AsyncImage(
                    modifier = Modifier
                        .height((screenWidth - 32.dp) / 2)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(onClick = { navController.navigate("products/$productId") }),
                    model = product.uri,
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
            Text(text = product.name, modifier = Modifier.padding(start = 8.dp), fontSize = 16.sp)
        }
    }
}