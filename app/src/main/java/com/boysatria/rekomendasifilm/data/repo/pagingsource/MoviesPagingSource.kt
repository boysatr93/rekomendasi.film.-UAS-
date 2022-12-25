package com.boysatria.rekomendasifilm.data.repo.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.boysatria.rekomendasifilm.data.remote.response.MovieResponse
import com.boysatria.rekomendasifilm.data.repo.MoviesRepo
import okio.IOException
import retrofit2.HttpException

//MoviePagingSource merupakan class paging source untuk menghandle pagination list
class MoviesPagingSource(
    private val moviesRepo: MoviesRepo
) : PagingSource<Int, MovieResponse.Movie>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse.Movie> {
        return try {
            val currentLoadingPageKey  = params.key ?: 1 //variable untuk penyimpan halaman terakhir yang di load
            val response = moviesRepo.getPopularMovies(page = currentLoadingPageKey) //perintah untuk mengambil data movie dari repo kemudian mendapatkan response
            val responseData = mutableListOf<MovieResponse.Movie>() //variable untuk menyimpan data response
            val data = response.body()?.movies ?: emptyList() //variable untuk menyimpan data list dari response
            responseData.addAll(data) //menyimpan semua data list dari response ke variable responseData

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1 //mengecek value page sebelum nya
            val nextKey = if (currentLoadingPageKey < 100) currentLoadingPageKey.plus(1) else null //mengecek value page kedepannya

            //akan meng load data ke ui
            LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey = nextKey
            )
        } catch (e: Exception){
            LoadResult.Error(e)
        } catch (e: IOException){
            LoadResult.Error(e)
        } catch (e: HttpException){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponse.Movie>): Int? {
        return null
    }

}
