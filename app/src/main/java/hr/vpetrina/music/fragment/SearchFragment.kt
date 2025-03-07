package hr.vpetrina.music.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.vpetrina.music.adapter.SearchItemAdapter
import hr.vpetrina.music.api.API_URL
import hr.vpetrina.music.api.SongsApi
import hr.vpetrina.music.api.SongsFetcher
import hr.vpetrina.music.databinding.FragmentSearchBinding
import hr.vpetrina.music.model.Item
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val songs = mutableListOf<Item>()
    private lateinit var songsApi: SongsApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        songsApi = retrofit.create(SongsApi::class.java)

        binding.svSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { fetchSongs(it) }
                binding.svSearch.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSongs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SearchItemAdapter(requireContext(), songs)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchSongs(query: String) {
        SongsFetcher(requireContext()).getSongs(15, query) { items ->
            songs.clear()
            songs.addAll(items)
            binding.rvSongs.adapter?.notifyDataSetChanged()
        }
    }
}