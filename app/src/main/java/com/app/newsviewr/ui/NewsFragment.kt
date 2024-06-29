package com.app.newsviewr.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.newsviewr.R
import com.app.newsviewr.databinding.FragmentNewsBinding
import com.app.newsviewr.ui.adapter.NewsAdapter
import com.app.newsviewr.ui.viewmodel.NewsViewModel
import com.app.newsviewr.ui.viewmodelfactory.NewsViewModelProviderFactory
import com.app.newsviewr.util.EndlessRecyclerViewListener
import com.app.newsviewr.util.LoadingType
import com.app.newsviewr.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    @Inject
    lateinit var viewModelFactory: NewsViewModelProviderFactory
    private lateinit var viewModel: NewsViewModel

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var scrollListener: EndlessRecyclerViewListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]

        viewModel.filter = "recent"

        binding.toolbar.apply {
            title = getString(R.string.carousell_news)
            inflateMenu(R.menu.menu_main)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_recent -> {
                        viewModel.clearNewsList()
                        viewModel.filter = "recent"
                        true
                    }
                    R.id.menu_popular -> {
                        viewModel.clearNewsList()
                        viewModel.filter = "popular"
                        true
                    }
                    else -> false
                }
            }
            overflowIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_more_horiz_24)
        }

        setupRecyclerView()

        viewModel.newsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showRefreshLoading(false)
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.toList())
                    }

                    //If pagination exists, this needs to be validated
                    binding.rvNews.post { binding.rvNews.scrollToPosition(0 ) }

                    showEmptyState(newsAdapter.itemCount == 0)
                }

                is Resource.Error -> {
                    showRefreshLoading(false)
                    response.message?.let { message ->
                        (activity as MainActivity).showSnackbar(binding.root, getString(R.string.error_alert, message), true)
                    }
                    showEmptyState(newsAdapter.itemCount == 0)
                }

                is Resource.Loading -> {
                    when (response.loadingType) {
                        LoadingType.PAGINATION -> {
                            // Handle pagination loading
                        }

                        LoadingType.REFRESH -> {
                            showRefreshLoading(true)
                        }

                        else -> {
                            showRefreshLoading(true)
                        }
                    }
                    showEmptyState(false)
                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.clearNewsList()
            viewModel.getNewsList()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()

        val gridLayoutManager = GridLayoutManager(requireContext(), 3)

        scrollListener = object : EndlessRecyclerViewListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.loadingType = LoadingType.PAGINATION
                viewModel.getNewsList()
            }
        }

        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            addOnScrollListener(scrollListener)
        }
    }

    private fun showRefreshLoading(isLoading: Boolean) {
        binding.swipeRefresh.isRefreshing = isLoading
    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.tvEmptyNews.visibility = if(isEmpty) View.VISIBLE else View.GONE
        binding.rvNews.visibility = if(isEmpty) View.GONE else View.VISIBLE
    }
}