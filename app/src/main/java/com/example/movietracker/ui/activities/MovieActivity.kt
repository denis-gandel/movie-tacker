package com.example.movietracker.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.example.movietracker.R
import com.example.movietracker.databinding.ActivityMovieBinding
import com.example.movietracker.ui.viewmodels.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieBinding
    private val viewModel: MovieViewModel by viewModels()
    private var id by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        id = intent.getIntExtra("ID", -1)

        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.movieDetailsFav.setOnClickListener {
            viewModel.toggleFavorite(id)
        }

        setupObservers()
        if (id != -1) {
            viewModel.getMovie(id)
            viewModel.markViewed(id)
        }
    }

    private fun setupObservers() {
        viewModel.isFavorite.observe(this) { isFavorite ->
            binding.movieDetailsFav.text = if (isFavorite) {
                "Remove from favorites"
            } else {
                "Add to favorites"
            }
        }
        viewModel.movie.observe(this) {
            binding.movieDetailsTitle.text = it.title
            binding.movieDetailsYear.text = it.year.toString()
            binding.movieDetailsImage.load(it.imageUrl)
        }
    }
}