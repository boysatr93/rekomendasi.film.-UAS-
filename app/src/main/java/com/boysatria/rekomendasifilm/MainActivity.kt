package com.boysatria.rekomendasifilm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.boysatria.rekomendasifilm.base.BaseActivity
import com.boysatria.rekomendasifilm.databinding.ActivityMainBinding
import com.boysatria.rekomendasifilm.ui.favorite.FavoriteActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater))

        //variable untuk navigationView bottomnavigationview
        val navView: BottomNavigationView = binding.navView

        //variable fragment untuk menampilkan ui
        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        //memasangkan navController ke navView
        navView.setupWithNavController(navController)

        //Intent tujuan ke halaman favorit
        binding.btnFav.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
        }
    }
}