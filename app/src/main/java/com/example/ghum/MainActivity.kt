package com.example.ghum

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

class MainActivity : AppCompatActivity() {
    private var popupWindow: PopupWindow? = null
    private var menuBarClickedCount: Int = 0
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the ImageView elements
        val sounds = findViewById<ImageView>(R.id.sounds)
        val stream = findViewById<ImageView>(R.id.stream)
        val menuBar = findViewById<View>(R.id.Menu_bar)
        val mildRainSound = findViewById<ImageView>(R.id.mildRainSound)
        val bitmap = (mildRainSound.drawable as BitmapDrawable).bitmap
        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        roundedBitmapDrawable.isCircular = true
        mildRainSound.setImageDrawable(roundedBitmapDrawable)
        val heavyRainSound = findViewById<ImageView>(R.id.heavyRainSound)
        val bitmapHeavyRain = (heavyRainSound.drawable as BitmapDrawable).bitmap
        val roundedBitmapDrawableHeavyRain = RoundedBitmapDrawableFactory.create(resources, bitmapHeavyRain)
        roundedBitmapDrawableHeavyRain.isCircular = true
        heavyRainSound.setImageDrawable(roundedBitmapDrawableHeavyRain)
        val howlingWindSound = findViewById<ImageView>(R.id.howlingWindSound)
        val bitmapHowlingWind = (howlingWindSound.drawable as BitmapDrawable).bitmap
        val roundedBitmapDrawableHowlingWind = RoundedBitmapDrawableFactory.create(resources, bitmapHowlingWind)
        roundedBitmapDrawableHowlingWind.isCircular = true
        howlingWindSound.setImageDrawable(roundedBitmapDrawableHowlingWind)

        sounds.setOnClickListener {
            Toast.makeText(this, "You are in the sounds page!", Toast.LENGTH_SHORT).show()
        }

        stream.setOnClickListener {
            val intent = Intent(this, StreamActivity::class.java)
            startActivity(intent)
        }

        menuBar.setOnClickListener {
            menuBarClickedCount++
            if (menuBarClickedCount > 1) {
                popupWindow?.dismiss()
                menuBarClickedCount = 0
            } else {
                // Inflate the dropdown_menu.xml layout
                val inflater = getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val popupView = inflater.inflate(R.layout.dropdown_menu, null)

                // Initialize a new instance of PopupWindow
                popupWindow = PopupWindow(
                    popupView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                // Set an elevation for the PopupWindow
                popupWindow?.elevation = 10.0F

                // Set a background drawable for the PopupWindow
                popupWindow?.setBackgroundDrawable(resources.getDrawable(R.drawable.popup_background))

                // Set a click listener for the menu items
                val settings = popupView.findViewById<TextView>(R.id.menu_settings)
                val credits = popupView.findViewById<TextView>(R.id.menu_credits)

                settings.setOnClickListener {
                    // Perform action for Settings
                    Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                    // Dismiss the popup window
                    popupWindow?.dismiss()
                    menuBarClickedCount = 0
                }

                credits.setOnClickListener {
                    // Perform action for Credits
                    Toast.makeText(this, "Credits clicked", Toast.LENGTH_SHORT).show()
                    // Dismiss the popup window
                    popupWindow?.dismiss()
                    menuBarClickedCount = 0
                }

                // Finally, show the popup window
                popupWindow?.showAsDropDown(menuBar, 0, 0, Gravity.START)
            }
        }

        // Set click listener for Sounds
        mildRainSound.setOnClickListener {
            handleSound(R.raw.mild_rain_sound)
        }
        heavyRainSound.setOnClickListener {
            handleSound(R.raw.heavy_rain_sound)
        }
        howlingWindSound.setOnClickListener {
            handleSound(R.raw.howling_wind_sound)
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Dismiss the popup window when the user touches outside of it
        if (popupWindow != null && event?.action == MotionEvent.ACTION_DOWN) {
            if (!popupWindow?.isTouchable!! || !popupWindow?.isOutsideTouchable!!) {
                if (event.x < popupWindow?.contentView?.left!! ||
                    event.x > popupWindow?.contentView?.right!! ||
                    event.y < popupWindow?.contentView?.top!! ||
                    event.y > popupWindow?.contentView?.bottom!!
                ) {
                    popupWindow?.dismiss()
                    menuBarClickedCount = 0
                }
            }
        }
        return super.onTouchEvent(event)
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

    private fun handleSound(soundResource: Int) {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
        mediaPlayer = MediaPlayer.create(this, soundResource)
        mediaPlayer?.start()
    }
}
