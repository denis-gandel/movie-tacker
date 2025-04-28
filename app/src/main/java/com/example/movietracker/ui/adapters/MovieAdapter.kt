package com.example.movietracker.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movietracker.db.models.Movie
import com.example.movietracker.databinding.FragmentMovieItemBinding

class MovieAdapter (
    private var data: List<Movie>,
    private val onItemClicked: (Int) -> Unit,
    private val onFavoriteClicked: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentMovieItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, onItemClicked, onFavoriteClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: List<Movie>) {
        this.data = newData
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: FragmentMovieItemBinding,
        private val onItemClicked: (Int) -> Unit,
        private val onFavoriteClicked: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie) {
            binding.apply {
                movieName.text = item.title
                movieYear.text = item.year.toString()
                movieImage.load(item.imageUrl)

                root.setOnClickListener {
                    onItemClicked(item.id)
                }

                Log.d("FavoriteButton", "ITEM: ${item}")

                favoriteButton.text = if (item.isFavorite) {
                    "Remove from favorites"
                } else {
                    "Add to favorites"
                }

                favoriteButton.setOnClickListener {
                    onFavoriteClicked(item)
                }
            }
        }
    }
}

private fun Nothing?.launch(function: () -> Unit) {}
