package kg.adikyrgyz.giphy.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import kg.adikyrgyz.giphy.network.model.GifItem

@Dao
interface CacheDao {
    @Query("SELECT * FROM GifItem")
    fun getCachedGifs(): Single<List<GifItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToCache(list: List<GifItem>)

    @Query("DELETE FROM GifItem")
    suspend fun deleteCache()

}