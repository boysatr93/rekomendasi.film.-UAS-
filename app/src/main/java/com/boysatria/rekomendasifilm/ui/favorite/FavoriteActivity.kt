package com.boysatria.rekomendasifilm.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.boysatria.rekomendasifilm.R
import com.boysatria.rekomendasifilm.base.BaseActivity
import com.boysatria.rekomendasifilm.data.local.room.entity.FavoriteEntity
import com.boysatria.rekomendasifilm.databinding.ActivityFavoriteBinding
import com.boysatria.rekomendasifilm.ui.movies.DetailMovieActivity
import org.koin.android.ext.android.inject

//Class FavoriteActiv, untuk menambahkan fitur Favorite
class FavoriteActivity : BaseActivity<ActivityFavoriteBinding>() {

    private val viewModel: FavoriteViewModel by inject()
    private val favoriteAdapter: FavoriteAdapter by lazy { FavoriteAdapter(this@FavoriteActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityFavoriteBinding.inflate(layoutInflater))

        setupViews()
    }

    private fun setupViews(){
        binding.apply {
            toolbar.setNavigationOnClickListener { finish() }

            rvFavorite.apply {
                layoutManager = GridLayoutManager(this@FavoriteActivity, 2)
                adapter = favoriteAdapter.apply {
                    setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener{
                        override fun onClick(data: FavoriteEntity) {
                            startActivity(Intent(this@FavoriteActivity, DetailMovieActivity::class.java).apply {
                                putExtra(DetailMovieActivity.TITLE_MOVIE, data.title)
                                putExtra(DetailMovieActivity.ID_MOVIE, data.id)
                            })
                        }
                    })
                }
            }
        }
    }

    private fun getFavoriteList(){
        viewModel.getFavorites({
            Log.d(FavoriteActivity::class.java.simpleName, "listFavorite: ${it.joinToString()}")
            favoriteAdapter.addItems(it)
        }, {
            Toast.makeText(this@FavoriteActivity, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onResume() {
        super.onResume()
        getFavoriteList()
    }
}