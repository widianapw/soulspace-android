package com.soulspace.app.presentation.psychologist.navigation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.*
import kotlin.math.abs

@Composable
fun rememberCompassHeading(context: Context): State<Float> {
    // Use mutableStateOf to hold the heading so Compose can observe changes
    val headingState = remember { mutableStateOf(0f) }
    var lastUpdate by remember { mutableStateOf(0L) } // To track time between updates
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    // Threshold for significant heading changes (degrees)
    val headingThreshold = 2f
    // Throttle the sensor updates (milliseconds)
    val updateInterval = 500L // e.g., update every 500ms (0.5 seconds)

    // Arrays to store sensor values for rotation matrix
    val rotationMatrix = FloatArray(9)
    val orientationAngles = FloatArray(3)

    // Sensor event listener to capture orientation changes
    val sensorListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                        // Convert rotation vector to rotation matrix
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, it.values)
                        // Compute orientation angles from the rotation matrix
                        SensorManager.getOrientation(rotationMatrix, orientationAngles)
                        var newHeading = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()

//                        newHeading -= 45
                        // Normalize the heading to be within 0-360 degrees
                        if (newHeading < 0) {
                            newHeading += 360
                        }

                        newHeading = abs(360-newHeading)

                        val currentTime = System.currentTimeMillis()

                        // Check if the heading change exceeds the threshold or enough time has passed
                        if (abs(newHeading - headingState.value) > headingThreshold && (currentTime - lastUpdate > updateInterval)) {
                            headingState.value = newHeading
                            lastUpdate = currentTime
                            Log.d("CompassHeading", "Heading updated: ${headingState.value}")
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        // Register the rotation vector sensor
        val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        sensorManager.registerListener(sensorListener, rotationSensor, SensorManager.SENSOR_DELAY_GAME)

        onDispose {
            // Unregister the sensor listener when the composable leaves the composition
            sensorManager.unregisterListener(sensorListener)
        }
    }

    // Return the heading state so that changes trigger recomposition
    return headingState
}
