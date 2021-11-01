package kg.adikyrgyz.giphy.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Entity
data class GifItem(
    @PrimaryKey val id: String,
    val title: String,
    val url: String,
    val images: Images
)

data class Images(
    @SerializedName("original")
    val original: ImageItem,
    @SerializedName("downsized_medium")
    val downsized: ImageItem
)

data class ImageItem(
    val url: String
)

class ImagesTypeConverter {
    @TypeConverter
    fun fromImages(images: Images): String {
        return Gson().toJson(images)
    }
    @TypeConverter
    fun toImages(data:String): Images {
        return Gson().fromJson(data, Images::class.java)
    }
}