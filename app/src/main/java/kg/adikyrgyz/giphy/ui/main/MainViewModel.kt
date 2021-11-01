package kg.adikyrgyz.giphy.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.adikyrgyz.giphy.Constants
import kg.adikyrgyz.giphy.network.model.GifItem
import kg.adikyrgyz.giphy.network.model.TrendingResponse
import kg.adikyrgyz.giphy.repo.GifsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var repository: GifsRepository): ViewModel() {
    private val disposables = CompositeDisposable()

    val cacheLiveData = MutableLiveData<List<GifItem>>()
    val gifsLiveData = MutableLiveData<TrendingResponse>()
    val errorLiveData = MutableLiveData<Throwable>()

    var searchQuery: String? = null

    fun loadCache() {
        disposables.add(
            repository.getCachedGifs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cacheLiveData.postValue(it)
                }, {
                    errorLiveData.postValue(it)
                })
        )
    }

    fun getGifs(limit: Int, offset: Int) {
        val dataSource = if (searchQuery != null) {
            repository.searchGifs(Constants.API_KEY, searchQuery!!, limit, offset)
        } else {
            repository.getTrending(Constants.API_KEY, limit, offset)
        }
        disposables.add(
            dataSource.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                saveToCache(it)
                gifsLiveData.postValue(it)
            }, {
                errorLiveData.postValue(it)
            })
        )
    }

    private fun saveToCache(response: TrendingResponse) {
        if (response.pagination.offset == 0 && searchQuery == null) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.saveToCache(response.data)
            }
        }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}