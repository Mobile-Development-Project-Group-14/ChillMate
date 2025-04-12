package com.example.chillmate.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.chillmate.ui.theme.ChillMateTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale



import com.example.chillmate.R


@Composable
fun ImageCard(
    imageRes: Int,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 0.5f,
    cornerRadius: Dp = 16.dp,
    backgroundColor: Color = Color.White.copy(alpha = 0.2f),
) {
    Box(
        modifier = modifier
            .aspectRatio(aspectRatio)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable(onClick=onClick)
            .background(backgroundColor)
    ) {
        Image(
            painter = painterResource(id=imageRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ImageCardPreview(){
    ChillMateTheme {
        ImageCard(
            imageRes = R.drawable.ic_launcher_foreground,
            onClick = {},
            contentDescription = "Preview",
            modifier = Modifier.size(150.dp)
        )
    }
}
