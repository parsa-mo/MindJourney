package com.example.mindjourney

import RecordScreen
import ThoughtsScreen
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.*
import com.example.mindjourney.screens.*
import com.example.mindjourney.ui.theme.MindJourneyTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FirebaseAuth
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign-in options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set content with MainApp composable and pass ViewModelStoreOwner (in this case, `this`)
        setContent {
            MainApp(auth, googleSignInClient, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                // Signed in successfully, authenticate with Firebase
                firebaseAuthWithGoogle(account.idToken!!)
            }
        } catch (e: ApiException) {
            Log.e("Sign in error", "Google Sign-In failed with error: ${e.statusCode}, message: ${e.message}")
            Toast.makeText(this, "Google Sign In Failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show()
                    setContent {
                        MainApp(auth, googleSignInClient, this)
                    }
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }
}

@Composable
fun MainApp(auth: FirebaseAuth, googleSignInClient: GoogleSignInClient, viewModelStoreOwner: ViewModelStoreOwner) {
    val navController = rememberNavController()
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    val user = currentUser?.let {
        User(
            name = it.displayName ?: "No Name",
            email = it.email ?: "No Email",
            profilePictureUrl = it.photoUrl?.toString()
        )
    }

    // Safety check and logging for start destination
    val startDestination = if (auth.currentUser != null) "dashboard" else "home"
    Log.d("MainApp", "Start destination set to $startDestination")

    NavHost(navController = navController, startDestination = startDestination) {
        composable("home") { HomepageScreen(navController) }
        composable("login") { LoginScreen(navController, auth, googleSignInClient) }
        composable("signup") { SignupScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("record") { RecordScreen(navController) }
        composable("thoughts") { ThoughtsScreen(navController) }
        composable("account") {
            Account(navController) {
                auth.signOut()
                navController.navigate("home") {
                    popUpTo("dashboard") { inclusive = true }
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MindJourneyTheme {
//        MainApp(FirebaseAuth.getInstance(), GoogleSignIn.getClient(MainActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN))
//    }
//}
