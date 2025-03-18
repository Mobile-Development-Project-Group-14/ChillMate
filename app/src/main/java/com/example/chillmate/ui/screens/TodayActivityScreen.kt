package com.example.chillmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chillmate.model.Activity
import com.example.chillmate.model.WeatherCondition
import com.example.chillmate.ui.theme.ChillMateTheme


//Hardcoded data for weather data
val todayWeather = WeatherCondition(
    type = "Sunny",
    icon = "☀️",
    temperature = 28
)

//Hard coded list of activities based on weather
val activities = listOf(
    Activity(
        name = "Swimming",
        description = "Swimming is a great way to cool off on a hot day.",
        icon = "🏊"
    ),
    Activity(
        name = "Picnic",
        description = "Enjoy a picnic in the park with friends and family.",
        icon = "🧺"
    ),
    Activity(
        name = "Hiking",
        description = "Explore the great outdoors and enjoy the fresh air.",
        icon = "🥾"
    ),
    Activity(
        name = "Cycling",
        description = "Go for a bike ride and enjoy the scenery.",
        icon = "🚴"
    ),
    Activity(
        name = "Running",
        description = "Go for a run and get some exercise.",
        icon = "🏃"
    ),
    Activity(
        name = "Reading",
        description = "Relax with a good book and enjoy the sunshine.",
        icon = "📚"
    ),
    Activity(
        name = "Gardening",
        description = "Spend time in the garden and tend to your plants.",
        icon = "🌻"
    ),
    Activity(
        name = "Cooking",
        description = "Try out a new recipe and cook a delicious meal.",
        icon = "🍳"
    ),
    Activity(
        name = "Yoga",
        description = "Practice yoga and relax your mind and body.",
        icon = "🧘"
    ),
    Activity(
        name = "Painting",
        description = "Get creative and paint a masterpiece.",
        icon = "🎨"
    )
)


@Composable
fun TodayActivityScreen() {
    ChillMateTheme {
        Surface (modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Display weather information
                WeatherInfo(weather = todayWeather)

                Spacer(modifier = Modifier.height(24.dp))

                //Display list of activities
                ActivityList(activities = activities)
            }
        }
    }

}

@Composable
fun WeatherInfo(weather: WeatherCondition) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = weather.icon,
            fontSize = 48.sp
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${weather.temperature}°C",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = weather.type,
            fontSize = 24.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ActivityList(activities: List<Activity>) {
    LazyColumn {
        items(activities) {activity ->
            ActivityCard(activity = activity)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ActivityCard(activity: Activity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity.icon,
                fontSize = 32.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column {
                Text(
                    text = activity.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = activity.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
