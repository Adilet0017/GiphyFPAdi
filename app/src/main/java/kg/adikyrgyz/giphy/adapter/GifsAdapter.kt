package kg.adikyrgyz.giphy.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kg.adikyrgyz.giphy.databinding.RvItemGifMainBinding
import kg.adikyrgyz.giphy.network.model.GifItem

class GifsAdapter: RecyclerView.Adapter<GifsAdapter.ViewHolder>() {
    private var data = ArrayList<GifItem>()

    fun addData (newData: List<GifItem>) {
        data.addAll(newData)
        notifyDataSetChanged()
    }
    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: RvItemGifMainBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: GifItem) {
                binding.progress.isVisible = true
                Glide.with(binding.imageView)
                    .load(item.images.downsized.url)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progress.isVisible = false
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progress.isVisible = false
                            return false
                        }

                    })
                    .into(binding.imageView)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemGifMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}