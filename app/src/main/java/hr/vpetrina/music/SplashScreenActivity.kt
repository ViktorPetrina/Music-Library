package hr.vpetrina.music

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.vpetrina.music.databinding.ActivitySplashScreenBinding
import hr.vpetrina.music.framework.applyAnimation
import hr.vpetrina.music.framework.callDelayed
import hr.vpetrina.music.framework.isOnline
import hr.vpetrina.music.framework.startActivity

private const val DELAY = 3000L
private const val NO_INTERNET_DELAY = 4500L

class SplashScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        binding.tvSplash.applyAnimation(R.anim.blink)
        binding.ivSplash.applyAnimation(R.anim.rotate)
    }

    private fun redirect() {
        if (isOnline()) {
            callDelayed(DELAY) { startActivity<HostActivity>() }
        } else {
            binding.tvSplash.text = getString(R.string.no_internet)
            callDelayed(NO_INTERNET_DELAY) { finish() }
        }
    }
}