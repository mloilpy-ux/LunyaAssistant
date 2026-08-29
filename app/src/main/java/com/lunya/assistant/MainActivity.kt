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
import com.lunya.assistant.ai.LunyaReactionEngine
import com.lunya.assistant.databinding.ActivityMainBinding
import com.lunya.assistant.generator.CharacterPOVDialogueEngine
import com.lunya.assistant.generator.InfiniteItemFactory
import com.lunya.assistant.service.LunyaOverlayService
import com.lunya.assistant.wardrobe.LunyaWardrobe
import com.lunya.assistant.wardrobe.MegaWardrobeCatalog
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val overlayPermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { if (Settings.canDrawOverlays(this)) startOverlayService() }
    private val notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { checkAndRequestOverlay() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Toast.makeText(this, CharacterPOVDialogueEngine.greet().spoken, Toast.LENGTH_LONG).show()

        binding.btnStartLunya.setOnClickListener { requestPermissionsAndStart(); generateReaction("entry") }
        binding.btnStopLunya.setOnClickListener { stopService(Intent(this, LunyaOverlayService::class.java)); generateReaction("idle") }
        binding.btnAccessibility.setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)); generateReaction("notification") }
        binding.btnNotificationAccess.setOnClickListener { startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)); generateReaction("notification") }
        binding.btnGenerateLunya.setOnClickListener { generateReaction("entry") }

        val wardrobe = LunyaWardrobe(this)
        binding.btnWardrobe.setOnClickListener {
            val outfit = wardrobe.next()
            binding.wardrobeStatus.text = "Луня переодета: ${outfit.setName}"
            generateReaction("success")
        }
        binding.wardrobeStatus.text = "Одежда: ${wardrobe.current().setName}"

        val sets = MegaWardrobeCatalog.OUTFIT_SETS.size
        val item = InfiniteItemFactory.generate()
        binding.root.post { Toast.makeText(this, "Сетов: $sets | ${item.name} ★${item.rarity}", Toast.LENGTH_LONG).show() }
    }

    private fun generateReaction(event: String) {
        val key = binding.apiKeyInput.text?.toString()?.trim().orEmpty()
        if (key.isBlank()) return
        binding.generationProgress.visibility = View.VISIBLE
        binding.btnGenerateLunya.isEnabled = false
        lifecycleScope.launch {
            try {
                val url = LunyaReactionEngine(this@MainActivity, key).reaction(event)
                binding.generatedImage.loadFromUrl(url)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Реакция: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.generationProgress.visibility = View.GONE
                binding.btnGenerateLunya.isEnabled = true
            }
        }
    }

    private fun requestPermissionsAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS); return
        }
        checkAndRequestOverlay()
    }
    private fun checkAndRequestOverlay() {
        if (!Settings.canDrawOverlays(this)) overlayPermissionLauncher.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))) else startOverlayService()
    }
    private fun startOverlayService() {
        val intent = Intent(this, LunyaOverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent) else startService(intent)
        Toast.makeText(this, "Луня запущена!~", Toast.LENGTH_SHORT).show()
    }
}

private fun android.widget.ImageView.loadFromUrl(url: String) {
    val request = okhttp3.Request.Builder().url(url).get().build()
    okhttp3.OkHttpClient().newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) { post { Toast.makeText(context, "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show() } }
        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) { response.use { val bytes = it.body?.bytes() ?: return; val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return; post { setImageBitmap(bitmap) } } }
    })
}
