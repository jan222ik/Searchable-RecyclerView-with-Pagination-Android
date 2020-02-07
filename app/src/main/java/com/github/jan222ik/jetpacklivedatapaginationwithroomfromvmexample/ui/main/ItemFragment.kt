package com.github.jan222ik.jetpacklivedatapaginationwithroomfromvmexample.ui.main

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.jan222ik.jetpacklivedatapaginationwithroomfromvmexample.EntityDatabase
import com.github.jan222ik.jetpacklivedatapaginationwithroomfromvmexample.R
import kotlinx.android.synthetic.main.fragment_item_list.view.*


class ItemFragment : Fragment() {

    lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel

    private var columnCount = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view.list is RecyclerView) {
            with(view.list) {
                layoutManager = LinearLayoutManager(context)
                adapter = PagedAdapter()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        viewModel.filterTextAll.postValue(viewModel.latestFilter)
        view.searchEditText.setOnEditorActionListener { _: TextView, actionId: Int, event: KeyEvent? ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                val search = view.searchEditText.text.toString()
                with(viewModel) {
                    latestFilter = MainViewModel.Filter("%$search%", latestFilter.sortBy)
                    filterTextAll.postValue(latestFilter)
                }
                Toast.makeText(activity!!, "Searching", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }
        val sortByBtn = view.sortByBtn
        sortByBtn.text = viewModel.sorts[viewModel.indexSorts]
        sortByBtn.setOnClickListener {
            with(viewModel) {
                indexSorts = indexSorts.inc().rem(sorts.size)
                latestFilter = MainViewModel.Filter(latestFilter.search, sorts[indexSorts])
                filterTextAll.postValue(latestFilter)
                sortByBtn.text = sorts[indexSorts]
            }
        }
        viewModel.init(EntityDatabase.getDatabase(activity!!, lifecycleScope).wordDAO())
        with(view.list as RecyclerView) {
            viewModel.allWords?.observe(viewLifecycleOwner, Observer {
                val pagedAdapter = view.list.adapter as PagedAdapter
                pagedAdapter.submitList(it)
            })
        }
    }
}