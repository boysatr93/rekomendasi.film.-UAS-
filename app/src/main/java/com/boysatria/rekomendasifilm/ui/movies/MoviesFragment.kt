package com.boysatria.rekomendasifilm.ui.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boysatria.rekomendasifilm.base.BaseFragment
import com.boysatria.rekomendasifilm.data.remote.response.MovieResponse
import com.boysatria.rekomendasifilm.databinding.FragmentMoviesBinding
import com.boysatria.rekomendasifilm.utils.FooterLoadingAdapter
import org.koin.android.ext.android.inject

class MoviesFragment : BaseFragment<FragmentMoviesBinding>() {

    private val viewModel: MoviesViewModel by inject() //inisialisasi viewmodel ke fragment
    private val moviesAdapter : MoviesAdapter by lazy { MoviesAdapter(requireContext()) } //inisialisasi adapter untuk recyclerview

    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMoviesBinding {
        return FragmentMoviesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews() //fungsi yg berkaitan dengan ui
        getPopularMovies() //request data movie
    }

    private fun setupViews() {
        //setup untuk recyclerview
        binding.rvMovies.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2) // berbentuk grid dengan 2 baris
            adapter = moviesAdapter.withLoadStateFooter(
                footer = FooterLoadingAdapter { moviesAdapter.retry() } //setup recyclerview dengan adapter ditambah footer untuk loading
            )

            //setup lanjutan untuk adapter
            moviesAdapter.apply {
                //operasi jika item di recyclerview diklik
                setOnItemClickListener(object : MoviesAdapter.OnItemClickListener{
                    override fun onClick(data: MovieResponse.Movie) {
                        startActivity(Intent(context, DetailMovieActivity::class.java).apply {
                            putExtra(DetailMovieActivity.TITLE_MOVIE, data.title)
                            putExtra(DetailMovieActivity.ID_MOVIE, data.id)
                        })
                    }
                })

                //listener untuk mengecek status recyclerview apakah status sedang loading atau tidak
                addLoadStateListener { loadState ->
                    if (loadState.refresh is LoadState.Loading){
                        binding.progressbar.visibility = View.VISIBLE
                    } else {
                        binding.progressbar.visibility = View.GONE
                    }
                }
            }
        }
    }

    //fungsi untuk request data movie
    private fun getPopularMovies() {
        viewModel.getListMovie().observe(viewLifecycleOwner) {
            moviesAdapter.submitData(lifecycle, it) //setelah mendapatkan data movie, data akan disubmit/ditambah ke adapter
        }
    }
}