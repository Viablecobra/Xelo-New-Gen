package io.kitsuri.pelauncher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.kitsuri.pelauncher.Launcher.MinecraftActivity

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
        private const val MC_PACKAGE_NAME = "com.mojang.minecraftpe"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LaunchButton(context = this)
                }
            }
        }
    }

    @Composable
    private fun LaunchButton(context: MainActivity) {
        Button(onClick = {
            // In your launcher
            val intent = Intent(this, MinecraftActivity::class.java)
            intent.putExtra("MODS_ENABLED", false)
            startActivity(intent)
        }) {
            Text("Launch Minecraft")
        }
    }
}