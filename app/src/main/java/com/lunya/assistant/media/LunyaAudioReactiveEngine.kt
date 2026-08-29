package com.lunya.assistant.media

import android.media.audiofx.Visualizer
import android.util.Log

/**
 * Audio-reactive engine — makes Lunya react to system audio / music.
 */
class LunyaAudioReactiveEngine {

    companion object {
        private const val TAG = "AudioReactive"
    }

    private var visualizer: Visualizer? = null
    var onBeat: ((Float) -> Unit)? = null

    fun start(audioSessionId: Int = 0) {
        try {
            visualizer = Visualizer(audioSessionId).apply {
                captureSize = Visualizer.getCaptureSizeRange()[1]
                setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                    override fun onWaveFormDataCapture(v: Visualizer?, wf: ByteArray?, sr: Int) {}
                    override fun onFftDataCapture(v: Visualizer?, fft: ByteArray?, sr: Int) {
                        if (fft == null) return
                        var sum = 0.0
                        for (i in fft.indices step 2) {
                            val re = fft[i].toInt()
                            val im = fft.getOrElse(i + 1) { 0 }.toInt()
                            sum += re * re + im * im
                        }
                        val energy = (sum / fft.size).toFloat().coerceIn(0f, 1f)
                        if (energy > 0.3f) onBeat?.invoke(energy)
                    }
                }, Visualizer.getMaxCaptureRate() / 2, false, true)
                enabled = true
            }
            Log.d(TAG, "Started")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start visualizer", e)
        }
    }

    fun stop() {
        visualizer?.enabled = false
        visualizer?.release()
        visualizer = null
    }
}
