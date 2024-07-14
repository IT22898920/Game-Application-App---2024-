package com.example.new_game

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.new_game.GameTask
import com.example.new_game.GameView

class MainActivity : AppCompatActivity(), GameTask {
    private lateinit var rootLayout: LinearLayout
    private lateinit var startButton: Button
    private lateinit var mGameView: GameView
    private lateinit var uscore: TextView
    private lateinit var highScoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        uscore = findViewById(R.id.score)
        highScoreTextView = findViewById(R.id.highScoreTextView)
        mGameView = GameView(this, this)

        startButton.setOnClickListener {
            startGame()
        }

        uscore.setOnClickListener {
            toggleHighScoreVisibility()
        }
    }

    private fun startGame() {
        mGameView.setBackgroundResource(R.drawable.gamebackground)
        rootLayout.addView(mGameView)
        startButton.visibility = View.GONE
        uscore.visibility = View.GONE
        highScoreTextView.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun toggleHighScoreVisibility() {
        highScoreTextView.visibility = if (highScoreTextView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        highScoreTextView.text = "High Score: ${getHighScore()}"
    }

    @SuppressLint("SetTextI18n")
    override fun closeGame(mScore: Int) {
        uscore.text = "Score: $mScore"

        val currentHighScore = getHighScore()
        if (mScore > currentHighScore) {
            saveHighScore(mScore)
            highScoreTextView.text = "High Score: $mScore"
        }

        rootLayout.removeView(mGameView)
        startButton.visibility = View.VISIBLE
        uscore.visibility = View.VISIBLE
        highScoreTextView.visibility = View.VISIBLE

        mGameView.resetGameState()
    }

    private fun saveHighScore(score: Int) {
        val sharedPreferences = getSharedPreferences("game_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(HIGH_SCORE_KEY, score)
        editor.apply()
    }

    private fun getHighScore(): Int {
        val sharedPreferences = getSharedPreferences("game_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getInt(HIGH_SCORE_KEY, 0)
    }

    companion object {
        private const val HIGH_SCORE_KEY = "high_score"
    }
}