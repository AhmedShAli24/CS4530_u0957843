package com.example.marblegame

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
class MarbleViewModel : ViewModel() {

    private val _state = MutableStateFlow(MarbleState())
    val state: StateFlow<MarbleState> = _state.asStateFlow()

    private var vx = 0f
    private var vy = 0f
    private var lastTimestampNs = 0L

    private var maxX = 0f
    private var maxY = 0f
    private var boundsInitialized = false

    private var useLowPassFilter = false
    private var filteredX = 0f
    private var filteredY = 0f
    private var filterPrimed = false

    fun setUseLowPassFilter(enabled: Boolean) {
        useLowPassFilter = enabled
    }

    fun setBounds(maxX: Float, maxY: Float) {
        this.maxX = maxX.coerceAtLeast(0f)
        this.maxY = maxY.coerceAtLeast(0f)

        val current = _state.value
        _state.value = if (!boundsInitialized) {
            boundsInitialized = true
            current.copy(x = this.maxX / 2f, y = this.maxY / 2f) // start centered
        } else {
            current.copy(
                x = current.x.coerceIn(0f, this.maxX),
                y = current.y.coerceIn(0f, this.maxY),
            )
        }
    }

    fun onSensorPaused() {
        lastTimestampNs = 0L
    }
    fun onSensorReading(rawGx: Float, rawGy: Float, timestampNs: Long) {
        val (gx, gy) = if (useLowPassFilter) lowPass(rawGx, rawGy) else rawGx to rawGy

        // The first reading has no previous timestamp to measure dt against.
        if (lastTimestampNs == 0L || !boundsInitialized) {
            lastTimestampNs = timestampNs
            _state.value = _state.value.copy(gx = gx, gy = gy)
            return
        }

        val dt = ((timestampNs - lastTimestampNs) / NANOS_PER_SECOND).coerceIn(0f, MAX_DT)
        lastTimestampNs = timestampNs

        vx += dt * -GRAVITY_SCALE * gx
        vy += dt * GRAVITY_SCALE * gy

        vx *= FRICTION
        vy *= FRICTION

        var x = _state.value.x + (dt * VELOCITY_SCALE * vx)
        var y = _state.value.y + (dt * VELOCITY_SCALE * vy)

        if (x < 0f) { x = 0f; vx = -vx * BOUNCE }
        if (x > maxX) { x = maxX; vx = -vx * BOUNCE }
        if (y < 0f) { y = 0f; vy = -vy * BOUNCE }
        if (y > maxY) { y = maxY; vy = -vy * BOUNCE }

        _state.value = MarbleState(x = x, y = y, gx = gx, gy = gy)
    }

    /** Exponential low-pass filter to smooth noisy accelerometer readings. */
    private fun lowPass(x: Float, y: Float): Pair<Float, Float> {
        if (!filterPrimed) {
            filteredX = x
            filteredY = y
            filterPrimed = true
        } else {
            filteredX = (ALPHA * filteredX) + ((1 - ALPHA) * x)
            filteredY = (ALPHA * filteredY) + ((1 - ALPHA) * y)
        }
        return filteredX to filteredY
    }

    companion object {
        private const val NANOS_PER_SECOND = 1_000_000_000f
        private const val MAX_DT = 0.05f          // cap dt so a lag spike can't launch the marble
        private const val GRAVITY_SCALE = 100f    // (m/s^2) -> (dp/s^2); higher = faster
        private const val VELOCITY_SCALE = 1f     // velocity is already in dp/s
        private const val FRICTION = 0.98f        // 1.0 = no friction, lower = more drag
        private const val BOUNCE = 0.3f           // fraction of speed kept after hitting a wall
        private const val ALPHA = 0.8f            // low-pass smoothing (higher = smoother)
    }
}