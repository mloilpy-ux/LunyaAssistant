package com.lunya.assistant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.lunya.assistant.databinding.ActivityMainBinding
import com.lunya.assistant.generator.CharacterPOVDialogueEngine
import com.lunya.assistant.generator.InfiniteItemFactory
import com.lunya.assistant.service.LunyaOverlayService
import com.lunya.assistant.wardrobe.MegaWardrobeCatalog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Settings.canDrawOverlays(this)) startOverlayService()
        else Toast.makeText(this, "Нужно разрешение оверлея", Toast.LENGTH_LONG).show()
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { if (it) checkAndRequestOverlay() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // POV greeting
        val greet = CharacterPOVDialogueEngine.greet()
        Toast.makeText(this, greet.spoken, Toast.LENGTH_LONG).show()

        binding.btnStartLunya.setOnClickListener { requestPermissionsAndStart() }
        binding.btnStopLunya.setOnClickListener {
            stopService(Intent(this, LunyaOverlayService::class.java))
            Toast.makeText(this, "Луня отозвана", Toast.LENGTH_SHORT).show()
        }
        binding.btnAccessibility.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
        binding.btnNotificationAccess.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        // Extra: cycle wardrobe / spawn procedural item via long labels if buttons exist
        // Show catalog size
        val sets = MegaWardrobeCatalog.OUTFIT_SETS.size
        val genSample = InfiniteItemFactory.generate()
        binding.root.post {
            Toast.makeText(
                this,
                "Сетов: $sets | Пример предмета: ${genSample.name} (★${genSample.rarity})",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun requestPermissionsAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }
        checkAndRequestOverlay()
    }

    private fun checkAndRequestOverlay() {
        if (!Settings.canDrawOverlays(this)) {
            overlayPermissionLauncher.launch(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
            )
        } else startOverlayService()
    }

    private fun startOverlayService() {
        val intent = Intent(this, LunyaOverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent)
        else startService(intent)
        Toast.makeText(this, "Луня запущена!~", Toast.LENGTH_SHORT).show()
    }
}
