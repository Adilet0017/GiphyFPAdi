package kg.adikyrgyz.giphy.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PagedScrollListener(val layoutManager: GridLayoutManager): RecyclerView.OnScrollListener() {
    companion object {
        val PAGE_SIZE = 20
    }
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount =  layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (canLoadMore()) {
            if ((visibleItemCount + firstItemPosition) >= totalItemCount 
                    && firstItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                        loadNextPage()
            }
        }
    }

    abstract fun canLoadMore(): Boolean
    abstract fun loadNextPage()
}