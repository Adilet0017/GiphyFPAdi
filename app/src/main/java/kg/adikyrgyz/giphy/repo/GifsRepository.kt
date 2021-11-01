package kg.adikyrgyz.giphy.repo

import io.reactivex.Single
import kg.adikyrgyz.giphy.db.CacheDao
import kg.adikyrgyz.giphy.db.CacheDataBase
import kg.adikyrgyz.giphy.network.ApiService
import kg.adikyrgyz.giphy.network.model.GifItem
import kg.adikyrgyz.giphy.network.model.TrendingResponse
import javax.inject.Inject

class GifsRepository @Inject constructor(var service: ApiService, var cacheDao: CacheDao) {

    fun getTrending(apiKey: String, limit: Int, offset: Int): Single<TrendingResponse> {
        return service.getTrendingGifs(apiKey, limit, offset)
    }

    fun searchGifs(apiKey: String, query: String, limit: Int, offset: Int) : Single<TrendingResponse> {
        return service.searchGifs(apiKey, query, limit, offset)
    }

    fun getCachedGifs(): Single<List<GifItem>> {
        return cacheDao.getCachedGifs()
    }

    suspend fun saveToCache(list: List<GifItem>) {
        cacheDao.deleteCache()
        cacheDao.saveToCache(list)
    }
}