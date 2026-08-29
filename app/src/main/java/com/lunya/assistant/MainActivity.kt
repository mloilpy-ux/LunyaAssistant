package com.lunya.assistant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.lunya.assistant.ai.NanaBananaApi
import com.lunya.assistant.ai.NanaBananaTaskPoller
import com.lunya.assistant.databinding.ActivityMainBinding
import com.lunya.assistant.generator.CharacterPOVDialogueEngine
import com.lunya.assistant.generator.InfiniteItemFactory
import com.lunya.assistant.service.LunyaOverlayService
import com.lunya.assistant.wardrobe.MegaWardrobeCatalog
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val overlayPermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (Settings.canDrawOverlays(this)) startOverlayService()
        else Toast.makeText(this, "Нужно разрешение оверлея", Toast.LENGTH_LONG).show()
    }
    private val notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) checkAndRequestOverlay() else checkAndRequestOverlay()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Toast.makeText(this, CharacterPOVDialogueEngine.greet().spoken, Toast.LENGTH_LONG).show()

        binding.btnStartLunya.setOnClickListener { requestPermissionsAndStart() }
        binding.btnStopLunya.setOnClickListener { stopService(Intent(this, LunyaOverlayService::class.java)) }
        binding.btnAccessibility.setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        binding.btnNotificationAccess.setOnClickListener { startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }

        val sets = MegaWardrobeCatalog.OUTFIT_SETS.size
        val item = InfiniteItemFactory.generate()
        binding.root.post { Toast.makeText(this, "Сетов: $sets | ${item.name} ★${item.rarity}", Toast.LENGTH_LONG).show() }

        // Use a locally configured runtime key. Never hard-code the secret into source control.
        binding.btnGenerateLunya.setOnClickListener { generateLunyaImage() }
    }

    private fun generateLunyaImage() {
        val taskId = binding.taskIdInput.text?.toString()?.trim().orEmpty()
        val key = binding.apiKeyInput.text?.toString()?.trim().orEmpty()
        if (taskId.isBlank() || key.isBlank()) {
            Toast.makeText(this, "Укажи taskId и API key", Toast.LENGTH_SHORT).show()
            return
        }
        binding.generationProgress.visibility = View.VISIBLE
        binding.btnGenerateLunya.isEnabled = false
        lifecycleScope.launch {
            try {
                val url = NanaBananaTaskPoller(NanaBananaApi(key)).awaitImage(taskId)
                binding.generatedImage.loadFromUrl(url)
                Toast.makeText(this@MainActivity, "Луня готова ✨", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Ошибка генерации: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.generationProgress.visibility = View.GONE
                binding.btnGenerateLunya.isEnabled = true
            }
        }
    }

    private fun requestPermissionsAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            return
        }
        checkAndRequestOverlay()
    }

    private fun checkAndRequestOverlay() {
        if (!Settings.canDrawOverlays(this)) overlayPermissionLauncher.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
        else startOverlayService()
    }

    private fun startOverlayService() {
        val intent = Intent(this, LunyaOverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent) else startService(intent)
        Toast.makeText(this, "Луня запущена!~", Toast.LENGTH_SHORT).show()
    }
}

private fun android.widget.ImageView.loadFromUrl(url: String) {
    val request = okhttp3.Request.Builder().url(url).get().build()
    val client = okhttp3.OkHttpClient()
    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) { post { Toast.makeText(context, "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show() } }
        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            response.use {
                val bytes = it.body?.bytes() ?: return
                val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return
                post { setImageBitmap(bitmap) }
            }
        }
    })
}
