package at.aau.serg.kingdombuilder

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.example.myapplication.R

class SettingsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SettingsScreen(onSave = {finish()})
        }
    }

    @Composable
    fun SettingsScreen(onSave: () -> Unit){

        val context = LocalContext.current
        val prefs = context.getSharedPreferences("app_settings", MODE_PRIVATE)
        val userName = prefs.getString("user_name", "") ?: ""

        var userNameState by remember {mutableStateOf(userName)}

        val scrollState = rememberScrollState()


        Scaffold(
            topBar = {
                SettingsBar(
                    onCancel = {
                        val intent = Intent(context, StartMenuActivity::class.java)
                        context.startActivity(intent)
                    },
                    title = "Settings",
                    onSave = {
                        prefs.edit {
                            putString("user_name", userNameState)
                        }
                        onSave()
                    }

                )
            },
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.beige_lobby_background))
            )
            Column(modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState)){
                Text(
                    text = "Your Username:",
                    color = colorResource(id = R.color.light_blue_900),
                    fontSize = 20.sp
                )
                TextField(
                    value = userNameState,
                    onValueChange = { userNameState = it },
                    placeholder = { Text("Enter your name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true
                )
            }

        }
    }

    @Composable
    fun SettingsBar(
        title: String,
        onSave: () -> Unit,
        onCancel: () -> Unit
    ) {
        val backgroundImage = painterResource(id = R.drawable.start_menu_background)
        Box(
            modifier = Modifier.fillMaxWidth().height(72.dp)
        ) {
            Image(
               painter = backgroundImage,
               contentDescription = null,
               contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
               modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onCancel) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.light_blue_900).copy(alpha = 0.5f),
                                shape = RoundedCornerShape(2.dp) // leicht abgerundet, aber eckig
                            )
                            .padding(6.dp)
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Cancel",
                            tint = colorResource(id = R.color.beige_lobby_background)
                        )
                    }
                }

                Text(
                    text = title,
                    color = colorResource(id = R.color.light_blue_900),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onSave) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.light_blue_900).copy(alpha = 0.5f),
                                shape = RoundedCornerShape(4.dp) // optional: für leicht abgerundete Ecken
                            )
                            .padding(6.dp) // etwas Innenabstand, damit Icon nicht direkt am Rand klebt
                            .size(40.dp), // quadratisch
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
                            tint = colorResource(id = R.color.beige_lobby_background)
                        )
                    }
                }

            }
        }
    }
    @Preview (showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
    @Composable
    fun SettingsScreenPreview(){
        SettingsScreen(onSave = {})
    }
}