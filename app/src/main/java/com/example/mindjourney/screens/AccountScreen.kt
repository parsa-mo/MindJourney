import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter // Make sure to include Coil for image loading

import com.example.mindjourney.R
import com.example.mindjourney.components.BottomNavBar
import com.example.mindjourney.screens.User
import com.example.mindjourney.ui.theme.Purple40
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AccountScreen(navController: NavHostController, function: () -> Unit) {
    // Default user icon drawable resource (you can replace this with your own icon)
    val defaultIcon = painterResource(id = R.drawable.icon)
    val currentUser = FirebaseAuth.getInstance().currentUser

    val user = currentUser?.let {
        User(
            name = it.displayName ?: "No Name",
            email = it.email ?: "No Email",
            profilePictureUrl = it.photoUrl?.toString()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Circle image for user profile
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)), // Updated for Material3
            contentAlignment = Alignment.Center,

            ) {
            if (user?.profilePictureUrl != null) {
                // Load user's profile picture (using Coil, Glide, etc.)
                Image(
                    painter = rememberImagePainter(user.profilePictureUrl),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Show default icon
                Image(
                    painter = defaultIcon,
                    contentDescription = "Default Profile Icon",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User name and email
        user?.let {
            Text(
                text = it.name,
                style = MaterialTheme.typography.titleLarge, // Updated for Material3
                textAlign = TextAlign.Center
            )
            Text(
                text = it.email,
                style = MaterialTheme.typography.bodyMedium, // Updated for Material3
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.fillMaxHeight(0.8F))


        // Sign-out button
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()  // Perform the sign-out operation
                navController.navigate("home")  // Navigate to home after sign-out
            },
            colors = ButtonDefaults.buttonColors(
                Purple40
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign Out")
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomNavBar(navController)
    }
}