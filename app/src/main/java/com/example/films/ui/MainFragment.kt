package com.example.films.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.films.viewmodel.MainViewModel
import com.example.films.R
import com.example.films.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MainFragment: DaggerFragment(R.layout.fragment_main) {

    private val adapter = FilmsAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel  by lazy {
        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private lateinit var viewBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMainBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (viewBinding) {
            filmsRv.adapter = adapter
            filmsRv.layoutManager = LinearLayoutManager(context)
            filmsRv.setOnTouchListener { _, _ ->
                hideKeyboard()
                return@setOnTouchListener false
            }

            searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (isVisible) viewModel.onSearchTextChange(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
            })

            refreshBtn.setOnClickListener {
                viewModel.onRefresh(searchView.query)
            }

            refreshLayout.setOnRefreshListener {
                refreshLayout.isRefreshing = false
                viewModel.onRefresh(searchView.query)
            }
        }

        observeState()
    }

    override fun onStop() {
        adapter.saveData()
        super.onStop()
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is MainViewModel.State.Loading -> showLoading(it.indicatorLoading)
                is MainViewModel.State.Success -> showSuccess(it)
                is MainViewModel.State.Error -> showError()
                is MainViewModel.State.SearchFailed -> showSearchFailed(it.searchQuery)
            }
        }
    }

    private fun showSuccess(state: MainViewModel.State.Success) {
        with (viewBinding) {
            progressIndicator.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchFail.visibility = View.GONE
            error.visibility = View.GONE
            filmsRv.visibility = View.VISIBLE
        }

        adapter.setItems(state.result)
    }

    private fun showLoading(indicator: Boolean) {
        with (viewBinding) {
            searchFail.visibility = View.GONE
            error.visibility = View.GONE
            if (indicator) progressIndicator.visibility = View.VISIBLE
            else progressBar.visibility = View.VISIBLE

            if (!indicator) {
                filmsRv.visibility = View.GONE
            } else {
                filmsRv.layoutManager?.scrollToPosition(0)
            }
        }
    }

    private fun showError() {
        with (viewBinding) {
            progressIndicator.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchFail.visibility = View.GONE
            filmsRv.visibility = View.GONE
            error.visibility = View.VISIBLE
        }
        hideKeyboard()

        val snackBar = Snackbar.make(requireView(), getString(R.string.snackbar_message), Toast.LENGTH_SHORT)
        snackBar.show()
    }

    private fun showSearchFailed(searchQuery: String) {
        with (viewBinding) {
            progressIndicator.visibility = View.GONE
            progressBar.visibility = View.GONE
            error.visibility = View.GONE
            filmsRv.visibility = View.GONE
            searchFail.visibility = View.VISIBLE
            searchFailMessage.text = getString(R.string.search_failed_message, searchQuery)
        }

        hideKeyboard()
    }

    private fun hideKeyboard() {
        val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}