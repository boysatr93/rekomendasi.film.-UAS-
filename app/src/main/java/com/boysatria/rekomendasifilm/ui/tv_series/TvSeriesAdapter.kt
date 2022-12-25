package com.boysatria.rekomendasifilm.ui.tv_series

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.boysatria.rekomendasifilm.data.remote.response.MovieResponse
import com.boysatria.rekomendasifilm.data.remote.response.TvSeriesResponse
import com.boysatria.rekomendasifilm.databinding.ItemMovieBinding
import com.boysatria.rekomendasifilm.ui.movies.DetailMovieActivity
import com.bumptech.glide.Glide

//adapter class untuk menampilkan list item di recyclerview menggunakan pagination
class TvSeriesAdapter(
    private val context: Context
) : PagingDataAdapter<TvSeriesResponse.Movie, TvSeriesAdapter.ViewHolder>(
    DataDifferntiator
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        return ViewHolder(ItemMovieBinding.inflate(view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    open inner class ViewHolder(
        val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        //menampilkan data
        fun bind(data: TvSeriesResponse.Movie){
            binding.apply {
                //menampilkan image menggunakan Glide
                Glide.with(ivMovie.rootView)
                    .load("https://image.tmdb.org/t/p/w342${data.posterPath}")
                    .into(ivMovie)
                //menampilkan judul
                tvName.text = data.name
                //menampilkan rating
                rbMovie.rating = data.rating/2

                root.setOnClickListener {
                    mListener!!.onClick(data)
                }
            }
        }
    }

    //interface untuk listener jika terjadi interaksi click
    interface OnItemClickListener {
        fun onClick(data: TvSeriesResponse.Movie)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }

    //fungsi untuk mencegah adanya data yang sama/duplicated data
    object DataDifferntiator : DiffUtil.ItemCallback<TvSeriesResponse.Movie>() {

        override fun areItemsTheSame(oldItem: TvSeriesResponse.Movie, newItem: TvSeriesResponse.Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TvSeriesResponse.Movie, newItem: TvSeriesResponse.Movie): Boolean {
            return oldItem == newItem
        }
    }
}