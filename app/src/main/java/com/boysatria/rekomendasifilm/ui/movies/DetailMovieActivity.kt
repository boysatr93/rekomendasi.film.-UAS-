package com.boysatria.rekomendasifilm.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.boysatria.rekomendasifilm.R
import com.boysatria.rekomendasifilm.base.BaseActivity
import com.boysatria.rekomendasifilm.data.local.room.entity.FavoriteEntity
import com.boysatria.rekomendasifilm.data.remote.response.DetailMovieResponse
import com.boysatria.rekomendasifilm.databinding.ActivityDetailMovieBinding
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject

class DetailMovieActivity : BaseActivity<ActivityDetailMovieBinding>() {

    //param untuk mendapatkan data intent dari list
    companion object {
        const val ID_MOVIE = "id_movie"
        const val TITLE_MOVIE = "title_movie"
    }

    private val viewModel: MoviesViewModel by inject() //inisialisasi view model ke activity

    private var idMovie = "" //variable untuk menyimpan id movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityDetailMovieBinding.inflate(layoutInflater))

        //menyimpan data id movie dari intent
        idMovie = intent.getStringExtra(ID_MOVIE)?:""

        setupViews() //fungsi yg berkaitan dengan ui
        getDetailMovies() //request data detail movie
        checkFavoriteStatus() //mengecek apakah movie ini favorit atau tidak
    }

    private fun setupViews(){
        val titleMovie = intent.getStringExtra(TITLE_MOVIE)?:"" //mengambil data title dari intent

        //setup toolbar
        binding.apply {
            toolbar.apply {
                setSupportActionBar(this)
                title = titleMovie
                setNavigationOnClickListener { finish() }
            }
        }
    }

    private fun getDetailMovies(){
        viewModel.getDetailMovie(idMovie) //request data detail movie dari API

        //jika sudah mendapatkan data detail movie maka akan langsung menjalankan fungsi showData
        viewModel.detailMovieResponse.observe(this) { response ->
            showData(response.body()!!)
        }

        //jika terjadi error maka akan menampilkan toast
        viewModel.errorMessage.observe(this) { error ->
            Toast.makeText(this@DetailMovieActivity, error, Toast.LENGTH_SHORT).show()
        }

        //mengecek apakah status loading atau tidak, jika status loading maka akan menampilkan progress bar
        viewModel.loading.observe(this) { loading ->
            binding.progressbar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun showData(data: DetailMovieResponse){
        binding.apply {
            Glide.with(ivMovie.rootView)
                .load("https://image.tmdb.org/t/p/w342${data.posterPath}")
                .into(ivMovie)

            tvName.text = data.title
            tvReleaseDate.text = data.releaseDate
            tvTagline.text = data.tagline
            tvStatus.text = data.status
            tvDesc.text = data.overview
            rbMovie.rating = data.voteAverage!!.toFloat()/2

            btnFav.setOnClickListener {
                if (viewModel.isFavorite){
                    deleteFavorite()
                } else {
                    addFavorite(data)
                }
            }
        }
    }

    //menyimpan data favorite ke db
    private fun addFavorite(data: DetailMovieResponse){
        Toast.makeText(this@DetailMovieActivity, data.id, Toast.LENGTH_SHORT).show()
        viewModel.addFavorite(
            FavoriteEntity(
                id = data.id.toString(),
                title = data.title,
                overview = data.overview,
                poster_path = data.posterPath,
                backdrop_path = data.backdropPath,
                vote_average = data.voteAverage,
                release_date = data.releaseDate,
                type = "movie"
            ), {
                checkFavoriteStatus()
            }
        ) {
            Log.d(DetailMovieActivity::class.java.simpleName, it.message.toString())
        }
    }

    //menghapus data favorite dari db
    private fun deleteFavorite(){
        viewModel.deleteFavorite(idMovie.toString(), {
            checkFavoriteStatus()
        }) {
            Log.d(DetailMovieActivity::class.java.simpleName, it.message.toString())
        }
    }

    //mengecek status favorite dari movie
    private fun checkFavoriteStatus(){
        viewModel.getFavorite(idMovie.toString(), {
            binding.btnFav.setImageDrawable(ContextCompat.getDrawable(this@DetailMovieActivity, R.drawable.ic_fav_active))
        }){
            binding.btnFav.setImageDrawable(ContextCompat.getDrawable(this@DetailMovieActivity, R.drawable.ic_fav_inactive))
        }
    }
}