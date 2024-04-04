package com.example.ghum

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StreamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stream)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the ImageView elements
        val sounds = findViewById<ImageView>(R.id.sounds)
        val stream = findViewById<ImageView>(R.id.stream)

        stream.setOnClickListener {
            Toast.makeText(this, "You are in the streams page!", Toast.LENGTH_SHORT).show()
        }

        sounds.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Hide the status bar and navigation bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

        val topImage: ImageView = findViewById(R.id.topImage)

        // Calculate the desired height percentage (e.g., 30% for covering 30% of the screen)
        val screenHeight = getScreenHeight()
        val desiredHeightPercentage = 0.30 // 30%

        // Calculate the height for the ImageView
        val imageViewHeight = (screenHeight * desiredHeightPercentage).toInt()

        // Set the calculated height to the ImageView
        topImage.layoutParams.height = imageViewHeight
        topImage.requestLayout()
    }

    private fun getScreenHeight(): Int {
        val display: Display? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay
        }

        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display?.getRealMetrics(displayMetrics)
        } else {
            @Suppress("DEPRECATION")
            display?.getMetrics(displayMetrics)
        }
        return displayMetrics.heightPixels
    }
}