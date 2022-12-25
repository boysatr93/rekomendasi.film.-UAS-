package com.boysatria.rekomendasifilm.ui.tv_series

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.boysatria.rekomendasifilm.data.local.room.entity.FavoriteEntity
import com.boysatria.rekomendasifilm.data.local.room.repo.RoomRepo
import com.boysatria.rekomendasifilm.data.remote.response.DetailTvSeriesResponse
import com.boysatria.rekomendasifilm.data.remote.response.MovieResponse
import com.boysatria.rekomendasifilm.data.remote.response.TvSeriesResponse
import com.boysatria.rekomendasifilm.data.repo.MoviesRepo
import com.boysatria.rekomendasifilm.data.repo.pagingsource.MoviesPagingSource
import com.boysatria.rekomendasifilm.data.repo.pagingsource.TvSeriesPagingSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import retrofit2.Response

//View Model untuk tv series dengan implementasi MoviesRepo dan RoomRepo
class TvSeriesViewModel(private val moviesRepo: MoviesRepo,
                        private val roomRepo: RoomRepo
) : ViewModel() {
    private val disposable = CompositeDisposable() //disposable untuk rxJava

    val loading = MutableLiveData<Boolean>() //untuk menyimpan data status loading
    val errorMessage = MutableLiveData<String>() //untuk menyimpan data error
    var detailTvSeriesResponse = MutableLiveData<Response<DetailTvSeriesResponse>>() //menyimpan data detail tv series

    var isFavorite = false //variable untuk menyimpan data favorite atau tidak

    //request list tv series ke API dengan menggunakan Pagination dengan maksimal jumlah page yang akan di load adalah 100 page
    fun getListTvSeries() : LiveData<PagingData<TvSeriesResponse.Movie>> {
        return Pager(PagingConfig(pageSize = 100)){
            TvSeriesPagingSource(moviesRepo)
        }.liveData.cachedIn(viewModelScope)
    }

    //request detail tv series ke API
    fun getDetailTvSeries(id: String){
        //set loading true
        loading.value = true
        //menjalan kan couroutine scope
        viewModelScope.launch {
            //request detail tv series kemudian mengambil data feedback dari API
            val response = moviesRepo.getDetailPopularTvSeries(id)

            //jika request berhasil
            if (response.isSuccessful){
                detailTvSeriesResponse.postValue(response) //menyimpan data detail tv series ke variable detailTvSeriesResponse
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