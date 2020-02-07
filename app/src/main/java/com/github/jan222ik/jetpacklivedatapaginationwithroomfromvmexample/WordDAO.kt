package com.github.jan222ik.jetpacklivedatapaginationwithroomfromvmexample

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
abstract class WordDAO {

    @Insert
    abstract suspend fun insert(word: Word)

    @Query("SELECT * FROM Word order by word asc")
    abstract fun loadAllWords(): DataSource.Factory<Int?, Word>

    @Query("SELECT * FROM Word where word LIKE :idf order by freq")
    abstract fun loadAllWordsByIdf(idf: String?): DataSource.Factory<Int?, Word>

    @RawQuery(observedEntities = [Word::class])
    internal abstract fun loadRaw(rawQuery: SupportSQLiteQuery): DataSource.Factory<Int?, Word>

    fun loadWords(search: String? = null, sortBy: String? = null): DataSource.Factory<Int?, Word> {
        var query = "SELECT * FROM Word"
        var where = false
        if (search != null) {
            where = true
            query += " WHERE word like ?"
        }
        if (sortBy != null) {
            if (!where) {
                where = true
                query += " WHERE"
            }
            query += " order by $sortBy"
        }
        return loadRaw(SimpleSQLiteQuery(query, arrayOf(search)))
    }
}