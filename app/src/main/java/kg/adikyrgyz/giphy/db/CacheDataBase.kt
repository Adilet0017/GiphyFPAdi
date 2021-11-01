package kg.adikyrgyz.giphy.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kg.adikyrgyz.giphy.network.model.GifItem
import kg.adikyrgyz.giphy.network.model.ImagesTypeConverter

@Database(entities = [GifItem::class], version = 1)
@TypeConverters(ImagesTypeConverter::class)
abstract class CacheDataBase: RoomDatabase() {
    abstract fun cacheDao(): CacheDao
}