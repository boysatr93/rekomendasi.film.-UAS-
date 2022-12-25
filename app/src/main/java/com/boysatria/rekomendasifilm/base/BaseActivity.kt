package com.boysatria.rekomendasifilm.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewbinding.ViewBinding

//BaseActivity merupakan class base/pondasi dari sebuah activity yang sudah dipasang data binding
open class BaseActivity<B : ViewBinding> : AppCompatActivity(){
	//inisialisasi variable data binding
	protected lateinit var binding: B
		private set

	override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
		super.onCreate(savedInstanceState, persistentState)

		//untuk menonaktifkan night mode
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
	}

	//untuk set layout content menggunakan data binding
	protected fun setContentView(binding: B) {
		this.binding = binding
		super.setContentView(binding.root)
	}
}