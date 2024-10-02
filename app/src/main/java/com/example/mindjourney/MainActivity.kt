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
import com.example.mindjourney.login.GoogleAuthClient
import com.example.mindjourney.login.SignInScreen
import com.example.mindjourney.login.SignInViewModel
import com.example.mindjourney.screens.* // Import your existing screens
import com.example.mindjourney.ui.theme.MindJourneyTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    val googleAuthClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                MainApp(googleAuthClient)
            }
        }
}

@Composable
fun MainApp(googleAuthClient: GoogleAuthClient) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "sign_in") {
        composable("sign_in") {
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val lifecycleOwner = LocalLifecycleOwner.current
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK){
                        lifecycleOwner.lifecycleScope.launch {
                            val signInResult = googleAuthClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            val context = LocalContext.current
            val applicationContext = context.applicationContext
            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Sign in Successful!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            
            SignInScreen(
                state = state,
                onSignInClick = {
                    lifecycleOwner.lifecycleScope.launch {
                        val signInIntentSender = googleAuthClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }
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

    }
}
