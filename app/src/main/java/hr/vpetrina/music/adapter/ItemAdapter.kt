package hr.vpetrina.music.adapter

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
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
import hr.vpetrina.music.framework.playSound
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemAdapter(
    private val context: Context,
    private val items: MutableList<Item>)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>()
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

        holder.itemView.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(context.getString(R.string.song_details))
                setMessage(item.details)
                setIcon(R.drawable.songs_icon)
                setCancelable(true)
                setPositiveButton("Play song") {_, _ -> playSound(item.trackUrl)}
                setNegativeButton("Ok", null)
                show()
            }
        }
        
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(context.getString(R.string.delete))
                setMessage(context.getString(R.string.sure_to_delete))
                setIcon(R.drawable.delete)
                setCancelable(true)
                setPositiveButton("OK") {_, _ -> deleteItem(position)}
                setNegativeButton(context.getString(R.string.cancel), null)
                show()
            }
            true
        }

        holder.bind(item)
    }

    private fun deleteItem(position: Int) {
        val item = items[position]

        context.contentResolver.delete(
            ContentUris.withAppendedId(SONGS_PROVIDER_CONTENT_URI, item._id!!),
            null,
            null)

        items.removeAt(position)
        File(item.picturePath).delete()

        notifyDataSetChanged()
    }
}