package com.volynkin.nasaimageandvideolibrary.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.volynkin.nasaimageandvideolibrary.R
import com.volynkin.nasaimageandvideolibrary.data.ViewModel
import com.volynkin.nasaimageandvideolibrary.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: ViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search()
        binding.tVTitle.text = args.title
        binding.tvDescription.text = args.description
        viewModel.url.observe(viewLifecycleOwner) { bindItem(it) }
        viewModel.isLoading.observe(viewLifecycleOwner, ::updateLoadingState)
    }

    private fun bindItem(url: String) {
        if (args.type == "image") {
            binding.iV.visibility = View.GONE
            initImageView(url)
        } else if (args.type == "video") {
            binding.iV.visibility = View.GONE
            initVideoView(url)
        }
    }

    private fun initImageView(url: String) {
        Glide
            .with(this)
            .load(url)
            .placeholder(R.drawable.ic_twotone_photo_24)
            .error(R.drawable.ic_baseline_error_24)
            .into(binding.iV)
        binding.iV.visibility = View.VISIBLE
    }

    private fun initVideoView(url: String) {
        val videoView = binding.vV
        val uri = Uri.parse(url)
        videoView.setVideoURI(uri)
        val mc = MediaController(requireContext())
        mc.setAnchorView(binding.lL)
        videoView.setMediaController(mc)
        videoView.requestFocus(0)
        videoView.setOnErrorListener { _, _, _ ->
            binding.tVError.visibility = View.VISIBLE
            binding.bRefresh.visibility = View.VISIBLE
            refresh()
            return@setOnErrorListener true
        }
        videoView.setZOrderOnTop(true)
        videoView.start()
        videoView.visibility = View.VISIBLE
    }

    private fun search() {
        viewModel.searchUrl(args.type, args.link)
    }

    private fun refresh() {
        binding.bRefresh.setOnClickListener {
            search()
            binding.tVError.visibility = View.GONE
            binding.bRefresh.visibility = View.GONE
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        binding.pB.isVisible = isLoading
    }
}