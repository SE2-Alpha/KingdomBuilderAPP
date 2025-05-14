package at.aau.serg.websocketbrokerdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
        //To Do: Music and Sound Settings, Change Username, Change UserPicture, Darkmode/lightmode

        val context = LocalContext.current
        val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val MusicVolume = prefs.getInt("music_volume", 100)
        val SoundVolume = prefs.getInt("sound_volume", 100)
        val DarkMode = prefs.getBoolean("dark_mode", false)

        var musicVolume by remember { mutableStateOf(MusicVolume.toFloat()) }
        var soundVolume by remember { mutableStateOf(SoundVolume.toFloat()) }
        var darkModeEnabled by remember { mutableStateOf(DarkMode) }

        val scrollState = rememberScrollState()


        Scaffold(
            topBar = {
                SettingsBar(
                    title = "Settings",
                    onSave = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent) },
                    onCancel = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }
                )
            },
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).padding(16.dp).verticalScroll(scrollState)){
                Text("Preferences:")
                Spacer(modifier = Modifier.height(12.dp))

                VolumeSlider("Music Volume", musicVolume){musicVolume = it}
                VolumeSlider("Sound Volume", soundVolume){soundVolume = it}
                SettingToggle("Dark Mode", darkModeEnabled){darkModeEnabled = it}

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
                Text(label)
                Text("${value.toInt()}")
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
            Text(title)
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
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
                Column {
                    IconButton(onClick = onSave) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }




}