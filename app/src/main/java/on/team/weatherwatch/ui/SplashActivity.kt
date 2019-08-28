package on.team.weatherwatch.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import on.team.weatherwatch.R

class SplashActivity : AppCompatActivity() {
    private lateinit var centerAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        centerAnimation = AnimationUtils.loadAnimation(this, R.anim.center_rotate)
        sun.startAnimation(centerAnimation)

        val handler = Handler()
        val runnable = Runnable {
            val intent = Intent(this, MainActivity::class.java)

            sun.clearAnimation()
            startActivity(intent)
            finish()
        }
        handler.postDelayed(runnable, 5000)
    }
}