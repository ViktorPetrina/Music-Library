package hr.vpetrina.music.api

import android.content.Context
import android.util.Log
import hr.vpetrina.music.model.Item
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

    fun getSongs(limit: Int, name: String, callback: (MutableList<Item>) -> Unit) {

        val request = songsApi.fetchItems(limit, name)

        request.enqueue(object: Callback<ApiResponse> {
            val songs = mutableListOf<Item>()

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                response.body()?.results?.let {
                    it.forEach { song ->
                        songs.add(Item(
                            null,
                            song.title,
                            song.imageUrl,
                            song.trackUrl,
                            song.artist,
                            song.album)
                        )
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
}