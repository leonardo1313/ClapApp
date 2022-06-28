package com.optimahorizonapps.clapapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    private lateinit var playedTV: TextView
    private lateinit var dueTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.clapping_seekBar)
        handler = Handler(Looper.getMainLooper())

        val playBtn = findViewById<FloatingActionButton>(R.id.play_btn)
        val pauseBtn = findViewById<FloatingActionButton>(R.id.pause_btn)
        val stopBtn = findViewById<FloatingActionButton>(R.id.stop_btn)

        playedTV = findViewById(R.id.played_tv)
        dueTV = findViewById(R.id.due_tv)

        playBtn.setOnClickListener {
            if(mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.clapping)
                initializeSeekBar()
            }
            mediaPlayer?.start()
        }
        pauseBtn.setOnClickListener {
            mediaPlayer?.pause()
        }
        stopBtn.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null

            handler.removeCallbacks(runnable)
            seekBar.progress = 0
            playedTV.text = ""
            dueTV.text = ""
        }
    }

    private fun initializeSeekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition

            val playedTime = mediaPlayer!!.currentPosition / 1000
            playedTV.text = "$playedTime sec"

            val duration = mediaPlayer!!.duration / 1000
            val dueTime = duration - playedTime
            dueTV.text = "$dueTime sec"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}