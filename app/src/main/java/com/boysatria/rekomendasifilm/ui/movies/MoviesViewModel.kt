package com.boysatria.rekomendasifilm.ui.movies

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.boysatria.rekomendasifilm.data.local.room.entity.FavoriteEntity
import com.boysatria.rekomendasifilm.data.local.room.repo.RoomRepo
import com.boysatria.rekomendasifilm.data.remote.response.DetailMovieResponse
import com.boysatria.rekomendasifilm.data.remote.response.MovieResponse
import com.boysatria.rekomendasifilm.data.repo.MoviesRepo
import com.boysatria.rekomendasifilm.data.repo.pagingsource.MoviesPagingSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import retrofit2.Response

//View Model untuk movie dengan implementasi MoviesRepo dan RoomRepo
class MoviesViewModel(private val moviesRepo: MoviesRepo,
                      private val roomRepo: RoomRepo
) : ViewModel() {

    private val disposable = CompositeDisposable() //disposable untuk rxJava

    val loading = MutableLiveData<Boolean>() //untuk menyimpan data status loading
    val errorMessage = MutableLiveData<String>() //untuk menyimpan data error
    var detailMovieResponse = MutableLiveData<Response<DetailMovieResponse>>() //menyimpan data detail movie

    var isFavorite = false //variable untuk menyimpan data favorite atau tidak

    //request list movie ke API dengan menggunakan Pagination dengan maksimal jumlah page yang akan di load adalah 100 page
    fun getListMovie() : LiveData<PagingData<MovieResponse.Movie>> {
        return Pager(PagingConfig(pageSize = 100)){
            MoviesPagingSource(moviesRepo)
        }.liveData.cachedIn(viewModelScope)
    }

    //request detail movie ke API
    fun getDetailMovie(id: String){
        loading.value = true //set loading true
        viewModelScope.launch { //menjalan kan couroutine scope
            val response = moviesRepo.getDetailPopularMovies(id) //request detail movie kemudian mengambil data feedback dari API

            //jika request berhasil
            if (response.isSuccessful){
                detailMovieResponse.postValue(response) //menyimpan data detail movie ke variable detailMovieResponse
                loading.value = false //set loading false
            } else { //jika request gagal
                errorMessage.value = response.message() //menyimpan error message
                loading.value = false //set loading false
            }
        }
    }

    //menyimpan data movie ke favorite db
    fun addFavorite(favoriteEntity: FavoriteEntity, doSubscribe: () -> Unit, doError:(e: Throwable) -> Unit){
        launch {
            roomRepo.addFavorite(favoriteEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    doSubscribe.invoke()
                }){
                    doError.invoke(it)
                }
        }
    }

    //mengambil data favorite di db untuk mengecek apakah movie ini favorite atau tidak
    fun getFavorite(id: String, doSubscribe: (data: FavoriteEntity) -> Unit, doError:(e: Throwable) -> Unit){
        launch {
            roomRepo.getFavorite(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isFavorite = true
                    doSubscribe.invoke(it)
                }){
                    isFavorite = false
                    doError.invoke(it)
                }
        }
    }

    //menghapus data favorite dari db
    fun deleteFavorite(id: String, doSubscribe: () -> Unit, doError:(e: Throwable) -> Unit){
        launch {
            roomRepo.deleteFavorite(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    doSubscribe.invoke()
                }){
                    doError.invoke(it)
                }
        }
    }

    fun launch(job: () -> Disposable) {
        disposable.add(job())
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}