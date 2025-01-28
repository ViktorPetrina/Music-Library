package hr.vpetrina.music

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import hr.vpetrina.music.api.SongsWorker
import hr.vpetrina.music.databinding.ActivitySplashScreenBinding
import hr.vpetrina.music.framework.applyAnimation
import hr.vpetrina.music.framework.callDelayed
import hr.vpetrina.music.framework.getBooleanPreference
import hr.vpetrina.music.framework.isOnline
import hr.vpetrina.music.framework.startActivity


private const val DELAY = 3000L
const val DATA_IMPORTED = "hr.algebra.nasa.data_imported"

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

        if (getBooleanPreference(DATA_IMPORTED)) {
            callDelayed(DELAY) { startActivity<HostActivity>() }
        } else {
            if (isOnline()) {

                WorkManager.getInstance(this).apply {
                    enqueueUniqueWork(
                        DATA_IMPORTED,
                        ExistingWorkPolicy.KEEP,
                        OneTimeWorkRequest.Companion.from(SongsWorker::class.java)
                    )
                }

            } else {
                binding.tvSplash.text = getString(R.string.no_internet)
                callDelayed(DELAY) { finish() }
            }
        }
    }


}