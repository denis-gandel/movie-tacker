package com.example.movietracker.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movietracker.R
import com.example.movietracker.databinding.ActivityMainBinding
import com.example.movietracker.db.dao.MovieDao
import com.example.movietracker.db.models.Movie
import com.example.movietracker.ui.adapters.MovieAdapter
import com.example.movietracker.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var movieDao: MovieDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.allButton.setOnClickListener {
            changeListAllMovies()
        }

        binding.watchedButton.setOnClickListener {
            changeListWatchedMovies()
        }

        binding.favoritesButton.setOnClickListener {
            changeListFavoriteMovies()
        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchMovie(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.searchMovie(newText)
                }
                return true
            }
        })

        setupRecyclerView()
        setupObservers()
        viewModel.getMovies()
    }

    private fun setupObservers() {
        viewModel.movies.observe(this) {
            val adapter = binding.moviesViewList.adapter as MovieAdapter
            adapter.setData(it)
        }
    }

    private fun setupRecyclerView() {
        val adapter = MovieAdapter(
            arrayListOf(),
            onItemClicked = { movieId ->
                val intent = Intent(this, MovieActivity::class.java)
                intent.putExtra("ID", movieId)
                startActivity(intent)
            },
            onFavoriteClicked = { movie ->
                toggleFavorite(movie)
            }
        )

        binding.moviesViewList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
            this.adapter = adapter
        }
    }

    private fun toggleFavorite(movie: Movie) {
        lifecycleScope.launch {
            val isCurrentlyFavorite = movieDao.getMovie(movie.id)?.isFavorite ?: false
            val newFavoriteStatus = !isCurrentlyFavorite

            movieDao.updateMovieFavorite(movie.id, newFavoriteStatus)

            viewModel.getMovies()
        }
    }

    private fun changeListAllMovies() {
        viewModel.getMovies()
        binding.allButton.backgroundTintList = getColorStateList(R.color.purple_500)
        binding.allButton.setTextColor(getColor(R.color.white))
        binding.watchedButton.backgroundTintList = getColorStateList(R.color.button_inactive)
        binding.watchedButton.setTextColor(getColor(R.color.button_inactive_text))
        binding.favoritesButton.backgroundTintList = getColorStateList(R.color.button_inactive)
        binding.favoritesButton.setTextColor(getColor(R.color.button_inactive_text))

    }

    private fun changeListWatchedMovies() {
        viewModel.getWatchedMovies()
        binding.allButton.backgroundTintList = getColorStateList(R.color.button_inactive)
        binding.allButton.setTextColor(getColor(R.color.button_inactive_text))
        binding.watchedButton.backgroundTintList = getColorStateList(R.color.purple_500)
        binding.watchedButton.setTextColor(getColor(R.color.white))
        binding.favoritesButton.backgroundTintList = getColorStateList(R.color.button_inactive)
        binding.favoritesButton.setTextColor(getColor(R.color.button_inactive_text))
    }

    private fun changeListFavoriteMovies() {
        viewModel.getFavoriteMovies()
        binding.allButton.backgroundTintList = getColorStateList(R.color.button_inactive)
        binding.allButton.setTextColor(getColor(R.color.button_inactive_text))
        binding.watchedButton.backgroundTintList = getColorStateList(R.color.button_inactive)
        binding.watchedButton.setTextColor(getColor(R.color.button_inactive_text))
        binding.favoritesButton.backgroundTintList = getColorStateList(R.color.purple_500)
        binding.favoritesButton.setTextColor(getColor(R.color.white))
    }
}