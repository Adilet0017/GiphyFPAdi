package kg.adikyrgyz.giphy.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kg.adikyrgyz.giphy.db.CacheDao
import kg.adikyrgyz.giphy.db.CacheDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBModule {

    @Provides
    @Singleton
    fun getDataBase(@ApplicationContext context: Context): CacheDataBase {
        return Room.databaseBuilder(
            context,
            CacheDataBase::class.java,
            "cacheDB"
        ).build()
    }

    @Provides
    @Singleton
    fun getCacheDao(cacheDataBase: CacheDataBase) : CacheDao {
        return cacheDataBase.cacheDao()
    }
}