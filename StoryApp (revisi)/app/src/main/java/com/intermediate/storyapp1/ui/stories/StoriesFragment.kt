package com.intermediate.storyapp1.ui.stories

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.intermediate.storyapp1.data.adapter.ListStoriesAdapter
import com.intermediate.storyapp1.data.adapter.LoadingStateAdapter
import com.intermediate.storyapp1.databinding.FragmentStoriesBinding
import com.intermediate.storyapp1.ui.detail.createStory.CreateStoryActivity
import com.intermediate.storyapp1.ui.register.RegisterActivity
import com.intermediate.storyapp1.utils.ViewModelFactory
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class StoriesFragment : Fragment(){

    private var _binding: FragmentStoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var listStoriesAdapter: ListStoriesAdapter
    private lateinit var factory: ViewModelFactory
    private val homeViewModel: StoryViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupViewModel()
        setupView(root.context)
        getStories()
        createStoryButtonHandler()

        return root
    }


    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun setupView(context: Context) {
        val storiesRv = binding.storiesRv

        if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            storiesRv.layoutManager = GridLayoutManager(context, 2)
        } else {
            storiesRv.layoutManager = LinearLayoutManager(context)
        }

        listStoriesAdapter = ListStoriesAdapter()
        storiesRv.adapter = listStoriesAdapter
    }

    private fun getStories() {
        binding.storiesRv.adapter = listStoriesAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                listStoriesAdapter.retry()
            }
        )

        homeViewModel.getListStory.observe(viewLifecycleOwner) {
            listStoriesAdapter.submitData(lifecycle, it)
        }
    }

    private fun createStoryButtonHandler() {
        binding.createStoryButton.setOnClickListener {
            val intent = Intent(binding.root.context, CreateStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}