package kg.adikyrgyz.giphy.ui.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kg.adikyrgyz.giphy.adapter.GifsAdapter
import kg.adikyrgyz.giphy.databinding.ActivityMainBinding
import kg.adikyrgyz.giphy.network.model.Pagination
import kg.adikyrgyz.giphy.util.PagedScrollListener
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private var adapter: GifsAdapter? = null

    private var currentOffset = 0
    private var isLastPage = false
    private var isLoading = false
    set(value) {
        field = value
        binding.progress.isInvisible = !value
        if (!value) {
            binding.swipeRefresh.isRefreshing = value
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        initViews()
        initSearch()
        initObservers()
        loadCache()
        loadGifs()
    }



    private fun initViews() {
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addOnScrollListener(object : PagedScrollListener(layoutManager){
            override fun canLoadMore(): Boolean {
                return !isLastPage && !isLoading
            }

            override fun loadNextPage() {
                loadGifs()
            }
        })
        adapter = GifsAdapter()
        binding.recyclerView.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener {
            resetPagination()
            loadGifs()
        }
    }
    @SuppressLint("CheckResult")
    private fun initSearch() {
        val searchSubject = PublishSubject.create<String>()
        binding.etSearch.addTextChangedListener {
            val text = it?.toString()?.toLowerCase(Locale.getDefault())?.trim()?: ""
            binding.ivSearchCancel.isVisible = text.isNotEmpty()
            searchSubject.onNext(text)
        }
        searchSubject.debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                resetPagination()
                viewModel.searchQuery = if (it.isEmpty()) null else it
                loadGifs()
            }
        binding.ivSearchCancel.setOnClickListener {
            binding.etSearch.setText("")
        }
    }

    private fun initObservers() {
        viewModel.gifsLiveData.observe(this) {
            isLoading = false
            if (it.pagination.offset == 0) {
                adapter?.clearData()
            }
            adapter?.addData(it.data)
            checkPagination(it.pagination)
        }
        viewModel.errorLiveData.observe(this) {
            isLoading = false
            it.printStackTrace()
        }
        viewModel.cacheLiveData.observe(this) {
            adapter?.addData(it)
        }
    }

    private fun loadCache() {
        viewModel.loadCache()
    }

    private fun loadGifs() {
        isLoading = true
        viewModel.getGifs(PagedScrollListener.PAGE_SIZE, currentOffset)
    }

    private fun checkPagination(pagination: Pagination) {
        currentOffset = pagination.offset + pagination.count
        isLastPage = currentOffset >= pagination.total
    }

    private fun resetPagination() {
        currentOffset = 0
        isLastPage = false
    }
}