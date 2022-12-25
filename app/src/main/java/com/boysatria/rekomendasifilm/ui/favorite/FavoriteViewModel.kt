package com.boysatria.rekomendasifilm.ui.favorite

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

class FavoriteViewModel(private val roomRepo: RoomRepo
) : ViewModel() {

    private val disposable = CompositeDisposable()

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

    fun getFavorites(doSubscribe: (data: List<FavoriteEntity>) -> Unit, doError:(e: Throwable) -> Unit){
        launch {
            roomRepo.getFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    doSubscribe.invoke(it)
                }){
                    doError.invoke(it)
                }
        }
    }

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