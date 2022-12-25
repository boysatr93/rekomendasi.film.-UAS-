package com.boysatria.rekomendasifilm.ui.tv_series

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
import com.boysatria.rekomendasifilm.data.remote.response.TvSeriesResponse
import com.boysatria.rekomendasifilm.databinding.FragmentTvSeriesBinding
import com.boysatria.rekomendasifilm.utils.FooterLoadingAdapter
import org.koin.android.ext.android.inject

class TvSeriesFragment : BaseFragment<FragmentTvSeriesBinding>() {

    //inisialisasi viewmodel ke fragment
    private val viewModel: TvSeriesViewModel by inject()
    //inisialisasi adapter untuk recyclerview
    private val tvSeriesAdapter: TvSeriesAdapter by lazy { TvSeriesAdapter(requireContext()) }

    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTvSeriesBinding {
        return FragmentTvSeriesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews() //fungsi yg berkaitan dengan ui
        getPopularMovies() //request data movie
    }

    private fun setupViews() {
        //setup untuk recyclerview
        binding.rvTvSeries.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2) // berbentuk grid dengan 2 baris
            //setup recyclerview dengan adapter ditambah footer untuk loading
            adapter = tvSeriesAdapter.withLoadStateFooter(
                footer = FooterLoadingAdapter { tvSeriesAdapter.retry() }
            )

            //setup lanjutan untuk adapter
            tvSeriesAdapter.apply {
                //operasi jika item di recyclerview diklik
                setOnItemClickListener(object : TvSeriesAdapter.OnItemClickListener{
                    override fun onClick(data: TvSeriesResponse.Movie) {
                        //Intent ke halaman detail tv series
                        startActivity(Intent(requireContext(), DetailTvSeriesActivity::class.java).apply {
                            putExtra(DetailTvSeriesActivity.TITLE_MOVIE, data.name)
                            putExtra(DetailTvSeriesActivity.ID_MOVIE, data.id)
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
        viewModel.getListTvSeries().observe(viewLifecycleOwner) { dataMovie ->
            //setelah mendapatkan data movie, data akan disubmit/ditambah ke adapter
            tvSeriesAdapter.submitData(lifecycle, dataMovie)
        }
    }
}