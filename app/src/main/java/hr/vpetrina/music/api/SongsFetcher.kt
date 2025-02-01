package hr.vpetrina.music.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.vpetrina.music.SONGS_PROVIDER_CONTENT_URI
import hr.vpetrina.music.SongsReceiver
import hr.vpetrina.music.framework.sendBroadcast
import hr.vpetrina.music.handler.downloadImage
import hr.vpetrina.music.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SongsFetcher(private val context: Context) {

    private var songsApi: SongsApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        songsApi = retrofit.create(SongsApi::class.java)
    }

    fun fetchItems(limit: Int, name: String) {

        val request = songsApi.fetchItems(limit, name)

        request.enqueue(object: Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                response.body()?.results?.let { populateItems(it) }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }
        })
    }

    fun getSongs(limit: Int, name: String, callback: (MutableList<Item>) -> Unit) {

        val request = songsApi.fetchItems(limit, name)

        request.enqueue(object: Callback<ApiResponse> {
            val songs = mutableListOf<Item>()

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                response.body()?.results?.let {
                    it.forEach { song ->
                        //val picturePath = downloadImage(context, song.imageUrl)
                        songs.add(Item(null, song.title, song.imageUrl))
                    }
                }
                callback(songs)
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                callback(mutableListOf())
            }
        })
    }

    private fun populateItems(nasaItems: List<SongItem>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            nasaItems.forEach {
                val picturePath = downloadImage(context, it.imageUrl)

                val values = ContentValues().apply {
                    put(Item::title.name, it.title)
                    put(Item::picturePath.name, picturePath ?: "")
                }

                context.contentResolver.insert(
                    SONGS_PROVIDER_CONTENT_URI,
                    values
                )
            }

            context.sendBroadcast<SongsReceiver>()
        }
    }
}