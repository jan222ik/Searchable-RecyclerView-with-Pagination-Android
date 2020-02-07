package com.github.jan222ik.jetpacklivedatapaginationwithroomfromvmexample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.jan222ik.jetpacklivedatapaginationwithroomfromvmexample.Word
import com.github.jan222ik.jetpacklivedatapaginationwithroomfromvmexample.WordDAO


class MainViewModel : ViewModel() {

    val sorts: List<String> = listOf("word asc", "word desc", "freq asc", "freq desc", "id asc", "id desc")
    var indexSorts = 0
    var latestFilter = MainViewModel.Filter("%%", sorts[indexSorts])

    var allWords: LiveData<PagedList<Word>>? = null
    var filterTextAll = MutableLiveData<Filter>()

    fun init(wordDAO: WordDAO) {
        val config = PagedList.Config.Builder().setPageSize(10).build()
        allWords =
            Transformations.switchMap<Filter, PagedList<Word>>(filterTextAll) { input: Filter? ->
                if (input == null) {
                    return@switchMap LivePagedListBuilder(wordDAO.loadAllWords(), config).build()
                } else {
                    println("CURRENTINPUT: $input")
                    return@switchMap LivePagedListBuilder(
                        //wordDAO.loadAllWordsByIdf(input),
                        wordDAO.loadWords(input.search, input.sortBy),
                        config
                    ).build()
                }
            }
    }

    data class Filter(
        val search: String,
        val sortBy: String
    )
}