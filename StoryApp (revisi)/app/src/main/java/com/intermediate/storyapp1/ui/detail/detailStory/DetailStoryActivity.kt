package com.intermediate.storyapp1.ui.detail.detailStory

import android.os.Bundle
import android.provider.SyncStateContract
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.intermediate.storyapp1.R
import com.intermediate.storyapp1.data.constant.Constant
import com.intermediate.storyapp1.data.constant.Constant.DETAIL_STORY
import com.intermediate.storyapp1.data.model.Story
import com.intermediate.storyapp1.data.utils.withDateFormat
import com.intermediate.storyapp1.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailStory = intent.getParcelableExtra<Story>(Constant.DETAIL_STORY) as Story

        setupToolBar()
        setupUi(detailStory)
    }

    private fun setupToolBar() {
        title = resources.getString(R.string.detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private fun setupUi(detailStory: Story) {
        Glide.with(this@DetailStoryActivity)
            .load(detailStory.photoUrl)
            .fitCenter()
            .into(binding.storyImageView)

        detailStory.apply {
            binding.nameTextView.setText(name)
            binding.descriptionTextView.setText(description)
            binding.dateTextView.setText(createdAt.withDateFormat())
        }
    }
}