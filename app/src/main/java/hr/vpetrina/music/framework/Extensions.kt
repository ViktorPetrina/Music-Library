package hr.vpetrina.music.framework

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import hr.vpetrina.music.SONGS_PROVIDER_CONTENT_URI
import hr.vpetrina.music.mediaPlayer
import hr.vpetrina.music.model.Item
import java.io.IOException


fun View.applyAnimation(id: Int) {
    // view ima context
    startAnimation(AnimationUtils.loadAnimation(this.context, id))
    // radimo start animation nadamnom jer sam ja taj view
}

inline fun <reified T : Activity> Context.startActivity() {
    // refieied -> neka tip prezivi do runtimea jer ne moze prije postojat
    startActivity(
        Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}

inline fun <reified T : BroadcastReceiver> Context.sendBroadcast() {
    sendBroadcast(Intent(this, T::class.java))
}

fun Context.setBooleanPreference(key: String, value: Boolean = true) {
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(key, value)
        .apply()
}

fun Context.setBooleanPreferenceTest(key: String, value: Boolean = true) {
    getSharedPreferences(key, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(key, value)
        .apply()
}

fun Context.getBooleanPreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)

fun Context.getBooleanPreferenceTest(key: String) =
    getSharedPreferences(key, Context.MODE_PRIVATE).getBoolean(key, false)

fun Context.isOnline() : Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { cap ->
            return cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }

    return false
}

fun callDelayed(delay: Long, work: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(work, delay)
}

@SuppressLint("Range")
fun Context.fetchItems(): MutableList<Item> {
    val items = mutableListOf<Item>()

    val cursor = contentResolver?.query(
        SONGS_PROVIDER_CONTENT_URI,
        null,
        null,
        null,
        null
    )

    while (cursor != null && cursor.moveToNext()) {
        items.add(Item(
            cursor.getLong(cursor.getColumnIndex(Item::_id.name)),
            cursor.getString(cursor.getColumnIndex(Item::title.name)),
            cursor.getString(cursor.getColumnIndex(Item::picturePath.name)),
            cursor.getString(cursor.getColumnIndex(Item::trackUrl.name)),
            cursor.getString(cursor.getColumnIndex(Item::artist.name)),
            cursor.getString(cursor.getColumnIndex(Item::album.name))
        ))
    }

    return items
}

fun Context.addItem(item: Item) {
    val values = ContentValues().apply {
        put(Item::title.name, item.title)
        put(Item::picturePath.name, item.picturePath)
        put(Item::trackUrl.name, item.trackUrl)
        put(Item::artist.name, item.artist)
        put(Item::album.name, item.album)
    }

    contentResolver.insert(
        SONGS_PROVIDER_CONTENT_URI,
        values
    )
}

fun Context.askForPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
            return
        }
    }
}

fun playSound(url: String) {
    stopCurrentSound()

    mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )

        try {
            setDataSource(url)
            prepare()
            start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun stopCurrentSound() {
    mediaPlayer?.apply {
        if (isPlaying) {
            stop()
        }
        reset()
    }
}
