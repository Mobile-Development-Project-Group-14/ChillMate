package com.example.chillmate.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chillmate.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChillMateScaffold(
    navController: NavController,
    isDay: Boolean,
    title: String,
    showBackButton: Boolean = true,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                // Constrain drawer width to content
                ModalDrawerSheet(
                    modifier = Modifier
                        .offset(x = (-1).dp)
                        .widthIn(max = 300.dp)
                        .wrapContentWidth(Alignment.End),
                    drawerShape = MaterialTheme.shapes.large.copy(
                        topEnd = CornerSize(0.dp),
                        bottomEnd = CornerSize(0.dp)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(16.dp)
                            .width(180.dp)
                    ) {
                        Text(
                            "Navigation",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // Navigation items with compact padding
                        NavigationDrawerItem(
                            label = { Text("Home Screen") },
                            selected = false,
                            onClick = {
                                navController.navigate("home")
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        NavigationDrawerItem(
                            label = { Text("Outfit Guide") },
                            selected = false,
                            onClick = {
                                navController.navigate("outfitGuide")
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        NavigationDrawerItem(
                            label = { Text("Today's Activity") },
                            selected = false,
                            onClick = {
                                navController.navigate("todayActivity")
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        NavigationDrawerItem(
                            label = { Text("Travel Guide") },
                            selected = false,
                            onClick = {
                                navController.navigate("travelGuide")
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        NavigationDrawerItem(
                            label = { Text("Shop Now") },
                            selected = false,
                            onClick = {
                                navController.navigate("shop")
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                    }
                }
            },
            gesturesEnabled = drawerState.isOpen,
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        title,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White
                                    )
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = AppTheme.getTopBarColor(isDay),
                                    titleContentColor = Color.White
                                ),
                                navigationIcon = {
                                    if (showBackButton) {
                                        IconButton(onClick = { navController.popBackStack() }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Back",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                },
                                actions = {
                                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                        Icon(
                                            Icons.Default.Menu,
                                            contentDescription = "Menu",
                                            tint = Color.White
                                        )
                                    }
                                }
                            )
                        },
                        content = content
                    )
                }
            }
        )
    }
}