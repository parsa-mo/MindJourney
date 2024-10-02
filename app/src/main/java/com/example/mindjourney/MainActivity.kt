package com.example.mindjourney

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.mindjourney.screens.* // Import your existing screens
import com.example.mindjourney.ui.theme.MindJourneyTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                MainApp()
            }
        }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomepageScreen(navController) }  // Homepage screen
        composable("login") { LoginScreen(navController) }    // Login screen
        composable("signup") { SignupScreen(navController) }  // Signup screen
        composable("dashboard") { DashboardScreen(navController) }  // Dashboard with bottom navigation
        composable("record"){ RecordScreen(navController)}
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MindJourneyTheme {
        MainApp()
    }
}
