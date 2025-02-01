package hr.vpetrina.music.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.vpetrina.music.R
import hr.vpetrina.music.adapter.ItemAdapter
import hr.vpetrina.music.databinding.FragmentItemsBinding
import hr.vpetrina.music.framework.fetchItems
import hr.vpetrina.music.model.Item


class ItemsFragment : Fragment() {

    private lateinit var binding: FragmentItemsBinding
    private lateinit var items: MutableList<Item>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        items = requireContext().fetchItems()
        binding = FragmentItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items.ifEmpty {
            AlertDialog.Builder(requireContext()).apply {
                setTitle(getString(R.string.empty_library))
                setMessage(getString(R.string.empty_library_message))
                setCancelable(true)
                setPositiveButton("Add songs") {_, _ -> redirectToSearch()}
                setNegativeButton("Ok", null)
                show()
            }
            return
        }

        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ItemAdapter(requireContext(), items)
        }
    }

    private fun redirectToSearch() {
        view?.findNavController()?.navigate(R.id.menuSearch)
    }
}