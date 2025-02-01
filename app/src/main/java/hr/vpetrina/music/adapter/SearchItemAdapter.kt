package hr.vpetrina.music.adapter

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.vpetrina.music.SONGS_PROVIDER_CONTENT_URI
import hr.vpetrina.music.model.Item
import hr.vpetrina.music.R
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File
import java.io.IOException

class SearchItemAdapter(
    private val context: Context,
    private val items: MutableList<Item>)
    : RecyclerView.Adapter<SearchItemAdapter.ViewHolder>()
{

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        private val tvItem = itemView.findViewById<TextView>(R.id.tvItem)

        fun bind(item: Item) {
            tvItem.text = item.title
            Picasso.get()
                .load(item.picturePath.ifBlank { null })
                .placeholder(R.drawable.song_icon)
                .error(R.drawable.song_icon)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(context.getString(R.string.add_song))
                setMessage(context.getString(R.string.add_song_message))
                setIcon(R.drawable.songs_icon)
                setCancelable(true)
                setPositiveButton("Yes") {_, _ -> addItem(item)}
                setNegativeButton("No", null)
                show()
            }
            true
        }
        
        holder.itemView.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle("Audio")
                setMessage("Play song?")
                setIcon(R.drawable.play_icon)
                setCancelable(true)
                setPositiveButton("Play") {_, _ -> playSong(item)}
                setNegativeButton(context.getString(R.string.cancel), null)
                show()
            }
        }

        holder.bind(item)
    }

    private val mediaPlayer = MediaPlayer()

    private fun playSong(item: Item) {
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())

        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()
        }

        try {
            mediaPlayer.setDataSource(item.trackUrl)
            mediaPlayer.prepare()
            mediaPlayer.start()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.v("MUSIC", "Music is streaming")
    }

    private fun addItem(item: Item) {
        val values = ContentValues().apply {
            put(Item::title.name, item.title)
            put(Item::picturePath.name, item.picturePath)
            put(Item::trackUrl.name, item.trackUrl)
        }

        context.contentResolver.insert(
            SONGS_PROVIDER_CONTENT_URI,
            values
        )
    }
}