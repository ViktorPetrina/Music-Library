package hr.vpetrina.music

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.vpetrina.music.framework.setBooleanPreference
import hr.vpetrina.music.framework.startActivity

class SongsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.setBooleanPreference(DATA_IMPORTED)
        context.startActivity<HostActivity>()
    }
}