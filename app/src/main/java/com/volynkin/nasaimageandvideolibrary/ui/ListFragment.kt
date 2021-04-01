package com.volynkin.nasaimageandvideolibrary.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.volynkin.nasaimageandvideolibrary.R
import com.volynkin.nasaimageandvideolibrary.adapters.AdapterPaged
import com.volynkin.nasaimageandvideolibrary.data.ViewModel
import com.volynkin.nasaimageandvideolibrary.databinding.FragmentListBinding
import com.volynkin.nasaimageandvideolibrary.extensions.ItemOffsetDecoration
import com.volynkin.nasaimageandvideolibrary.extensions.autoCleared
import com.volynkin.nasaimageandvideolibrary.networking.NetworkState


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private var itemAdapter: AdapterPaged by autoCleared()
    private val viewModel: ViewModel by viewModels()
    private var inputText: String = "initial"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        bindViewModel()
        initToolbar()
    }

    private fun initList() {
        itemAdapter = AdapterPaged { title, description, link, type ->
            val actions = ListFragmentDirections
                .actionListFragmentToDetailsFragment(title, description, link, type)
            findNavController().navigate(actions)
        }
        with(binding.rV) {
            adapter = itemAdapter
            val span =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    1
                } else {
                    2
                }
            layoutManager = GridLayoutManager(requireContext(), span).apply {
                orientation = RecyclerView.VERTICAL
            }
            setHasFixedSize(true)
            val dividerItemDecoration =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)
            addItemDecoration(
                ItemOffsetDecoration(
                    requireContext(),
                    8,
                    8,
                    8,
                    8
                )
            )
        }
    }

    private fun initToolbar() {
        val searchItem = binding.toolbar.menu.findItem(R.id.search)
        (searchItem.actionView as SearchView).queryHint = "Search in NASA media Library"
        (searchItem.actionView as SearchView).maxWidth = Int.MAX_VALUE
        (searchItem.actionView as SearchView).setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                inputText = query.toString()
                viewModel.fetch(inputText)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun bindViewModel() {
        viewModel.listPaged.observe(viewLifecycleOwner) {
            itemAdapter.submitList(it)
        }
        viewModel.fetch(inputText)
        viewModel.networkState.observe(viewLifecycleOwner) { updateLoadingState(it) }
        binding.sR.setColorSchemeColors(requireContext().getColor(R.color.red_light))
        binding.sR.setOnRefreshListener {
            viewModel.fetch(inputText)
        }
    }

    private fun updateLoadingState(state: NetworkState) {
        binding.sR.isRefreshing = (state == NetworkState.RUNNING)
    }
}