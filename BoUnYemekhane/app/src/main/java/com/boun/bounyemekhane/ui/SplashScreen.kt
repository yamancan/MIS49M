package com.boun.bounyemekhane.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.boun.bounyemekhane.R

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    private lateinit var imgLogo: ImageView
    private lateinit var txtAppSlogan : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        initializeVariables()
        showAndHideImageAndText()
    }

    private fun initializeVariables(){
        imgLogo = findViewById(R.id.imgLogo)
        txtAppSlogan = findViewById(R.id.txtAppSlogan)
    }

    private fun showAndHideImageAndText(){

        var i = -1

        object: CountDownTimer(7000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                i++

                if (i == 3)
                    showObjects()
                else if (i == 6)
                    hideObjects()
            }

            override fun onFinish() {
                intent = Intent(this@SplashScreen, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }.start()
    }

    private fun showObjects(){
        imgLogo.visibility = View.VISIBLE
        txtAppSlogan.visibility = View.VISIBLE
    }

    private fun hideObjects(){
        imgLogo.visibility = View.INVISIBLE
        txtAppSlogan.visibility = View.INVISIBLE
    }
}