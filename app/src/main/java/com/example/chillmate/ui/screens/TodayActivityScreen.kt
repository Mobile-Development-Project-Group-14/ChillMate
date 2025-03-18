package com.example.chillmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import java.util.Locale


//Hardcoded data for weather data
val todayWeather = WeatherCondition(
    location = "Oulu", //City name
    type = "snow",  //Weather condition
    icon = "❄️", //Weather icon
    temperature = -8, //Temperature in Celsius
    isDay = true //Daytime
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



                Spacer(modifier = Modifier.height(24.dp))

                //Display list of activities based on weather
                val activities = getActivitySuggestions(todayWeather)
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
        //Display Location
        Text(
            text = weather.location,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))
        //Display Temperature
        Text(
            text = "${weather.temperature}°C",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold

        )

        Spacer(modifier = Modifier.height(8.dp))
        //Display Weather Condition
        Text(
            text = weather.type,
            fontSize = 24.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))
        //Display Day/Night
        Text(
            text = if(weather.isDay) "Day" else "Night",
            fontSize = 24.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun getActivitySuggestions(weather: WeatherCondition) : List<Activity> {
    return when(weather.type.lowercase(Locale.ROOT)) {
        "sunny" -> listOf(
            Activity("Swimming", "Swimming is a great way to cool off on a hot day.", "🏊"),
            Activity("Picnic", "Enjoy a picnic in the park with friends and family.", "🧺"),
            Activity("Cycling", "Go for a bike ride and enjoy the scenery.", "🚴")
        )
        "rain" -> listOf(
            Activity("Indoor Yoga", "Practice yoga and relax your mind and body.", "🧘"),
            Activity("Reading", "Relax with a good book and enjoy the rain.", "📚"),
            Activity("Cooking", "Try out a new recipe and cook a delicious meal.", "🍳")
        )
        "snow" -> listOf(
            Activity("Skiing", "Hit the slopes and enjoy the snow.", "⛷️"),
            Activity("Snowball Fight", "Have fun with friends in the snow.", "❄️"),
            Activity("Ice Skating", "Go ice skating and enjoy the winter weather.", "⛸️"),
            Activity("Sauna Bath", "Relax in a warm sauna and unwind.", "🧖"),
            Activity("Hot Chocolate", "Warm up with a cup of hot chocolate.", "☕")
        )

        else -> listOf(
            Activity("Hiking", "Explore the great outdoors and enjoy the fresh air.", "🥾"),
            Activity("Running", "Go for a run and get some exercise.", "🏃"),
            Activity("Gardening", "Spend time in the garden and tend to your plants.", "🌻")
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
        modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
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
