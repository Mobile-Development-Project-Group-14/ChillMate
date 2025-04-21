package com.example.chillmate.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.chillmate.R
import com.example.chillmate.ui.theme.ChillMateTheme
import com.example.chillmate.ui.theme.Typography

@Composable
fun ImageCard(
    imageRes: Int,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    backgroundColor: Color = Color.Transparent,
    title: String? = null,
    description: String? = null,
    showSlideIndicators: Boolean = false,
    totalSlides: Int = 0,
    currentSlide: Int = 0,
    fixedHeight: Dp? = null
) {
    val aspectRatio = 2f/3f // Aspect ratio for the card
    Box(
        modifier = modifier
            .then(if (fixedHeight != null) Modifier.height(fixedHeight) else Modifier)
            .aspectRatio(aspectRatio)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable(onClick = onClick)
            .background(backgroundColor)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 0.4f
                    )
                )
        )

        // Content area (title and description)
        if (title != null || description != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                verticalArrangement = Arrangement.Bottom
            ) {

                description?.let {
                    Text(
                        text = it,
                        style = Typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Slide indicators if enabled
        if (showSlideIndicators && totalSlides > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(totalSlides) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (currentSlide == index)
                                    Color.White
                                else
                                    Color.White.copy(alpha = 0.3f)
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageCardPreview() {
    ChillMateTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Basic preview
            ImageCard(
                imageRes = R.drawable.ic_launcher_foreground,
                onClick = {},
                contentDescription = "Basic Preview",
                modifier = Modifier.size(150.dp)
            )

            // Preview with text content
            ImageCard(
                imageRes = R.drawable.ic_launcher_foreground,
                onClick = {},
                contentDescription = "With Text Preview",
                modifier = Modifier.size(150.dp),
                title = "Activity Name",
                description = "This is a sample activity description that might be a bit longer"
            )

            // Preview with indicators
            ImageCard(
                imageRes = R.drawable.ic_launcher_foreground,
                onClick = {},
                contentDescription = "With Indicators Preview",
                modifier = Modifier.size(150.dp),
                title = "Activity Name",
                description = "With slide indicators",
                showSlideIndicators = true,
                totalSlides = 4,
                currentSlide = 1
            )
        }
    }
}