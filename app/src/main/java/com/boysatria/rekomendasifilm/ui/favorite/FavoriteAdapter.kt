package com.boysatria.rekomendasifilm.ui.favorite

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.boysatria.rekomendasifilm.data.local.room.entity.FavoriteEntity
import com.boysatria.rekomendasifilm.data.remote.response.MovieResponse
import com.boysatria.rekomendasifilm.databinding.ItemMovieBinding
import com.bumptech.glide.Glide

class FavoriteAdapter(val context: Context) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private var movieList = arrayListOf<FavoriteEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        return ViewHolder(ItemMovieBinding.inflate(view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    open inner class ViewHolder(
        val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FavoriteEntity){
            binding.apply {
                Glide.with(ivMovie.rootView)
                    .load("https://image.tmdb.org/t/p/w342${data.poster_path}")
                    .into(ivMovie)
                tvName.text = data.title
                rbMovie.rating = data.vote_average.toFloat()/2

                tvType.apply {
                    visibility = View.VISIBLE
                    text = when(data.type){
                        "movie" -> "Movie"
                        "tv_series" -> "TV Series"
                        else -> ""
                    }
                }

                root.setOnClickListener {
                    mListener!!.onClick(data)
                }
            }
        }
    }

    fun addItems(data: List<FavoriteEntity>){
        this.movieList.clear()
        this.movieList.addAll(data)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(data: FavoriteEntity)
    }
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}
