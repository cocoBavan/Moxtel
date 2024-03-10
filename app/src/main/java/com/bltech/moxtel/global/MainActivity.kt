package com.bltech.moxtel.global

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bltech.moxtel.global.navigation.MoxNavGraph
import com.bltech.moxtel.global.navigation.MoxRoutes
import com.bltech.moxtel.global.theme.MoxtelTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MoxtelTheme {
                MainScreen()
            }
        }
    }
}


val LocalTitleSetter = compositionLocalOf { ({ _: String -> }) }

@Composable
fun MainScreen() {
    val topBarTitle = remember { mutableStateOf("Your Moxtel") }
    val titleSetter: (String) -> Unit = {
        topBarTitle.value = it
    }
    CompositionLocalProvider(LocalTitleSetter provides titleSetter) {
        Scaffold(topBarTitle.value)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Scaffold(title: String) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val shouldShowBackIcon = remember {
        derivedStateOf { currentBackStackEntry?.destination?.route != MoxRoutes.HOME }
    }
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            title = {
                Text(
                    text = title,
                    color = Color.White
                )
            },
            navigationIcon = {
                if (shouldShowBackIcon.value) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            }
        )
    }) { innerPadding ->
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            MoxNavGraph(navController)
        }
    }
}
