package at.aau.serg.websocketbrokerdemo

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

class SettingsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SettingsScreen(onSave = {finish()}, onCancel = {finish()})
        }
    }

    @Composable
    fun SettingsScreen(onSave: () -> Unit, onCancel: () -> Unit){

        val context = LocalContext.current
        val prefs = context.getSharedPreferences("app_settings", MODE_PRIVATE)
        val userName = prefs.getString("user_name", "") ?: ""
        val musicVolume = prefs.getInt("music_volume", 100)
        val soundVolume = prefs.getInt("sound_volume", 100)
        val darkMode = prefs.getBoolean("dark_mode", false)

        var userNameState by remember {mutableStateOf(userName)}
        var musicVolumeState by remember { mutableStateOf(musicVolume.toFloat()) }
        var soundVolumeState by remember { mutableStateOf(soundVolume.toFloat()) }
        var darkModeEnabled by remember { mutableStateOf(darkMode) }

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
                        val editor = prefs.edit()
                        editor.putString("user_name", userNameState)
                        editor.putInt("music_volume", musicVolumeState.toInt())
                        editor.putInt("sound_volume", soundVolumeState.toInt())
                        editor.putBoolean("dark_mode", darkModeEnabled)
                        editor.apply()
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
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Preferences:", color = colorResource(id = R.color.light_blue_900), fontSize = 25.sp)
                Spacer(modifier = Modifier.height(12.dp))

                VolumeSlider("Music Volume", musicVolumeState){musicVolumeState = it}
                VolumeSlider("Sound Volume", soundVolumeState){soundVolumeState = it}
                SettingToggle("Dark Mode (Test Button)", darkModeEnabled){darkModeEnabled = it}

                Spacer(modifier = Modifier.height(8.dp))
            }

        }
    }

    @Composable
    fun VolumeSlider(label: String, value: Float, onValueChange: (Float) -> Unit){
        Column(modifier = Modifier.padding(vertical = 8.dp)){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = label, color = colorResource(id = R.color.light_blue_900), fontSize = 20.sp)
            }
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..100f,
                steps = 99
            )
        }
    }
    @Composable
    fun SettingToggle(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, color = colorResource(id = R.color.light_blue_900), fontSize = 15.sp)
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
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
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Cancel",
                        tint = colorResource(id = R.color.light_blue_900)
                    )
                }
                Text(
                    text = title,
                    color = colorResource(id = R.color.light_blue_900),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onSave) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save",
                        tint = colorResource(id = R.color.light_blue_900)
                    )
                }
            }
        }
    }
}