package com.github.jan222ik.jetpacklivedatapaginationwithroomfromvmexample

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(
    entities = [Word::class],
    version = 1,
    exportSchema = false
)
abstract class EntityDatabase : RoomDatabase() {

    abstract fun wordDAO(): WordDAO

    companion object {
        @Volatile
        private var INSTANCE: EntityDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): EntityDatabase {
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        EntityDatabase::class.java,
                        "JetpackLiveDataPaginationWithRoomFromVMExample_database"
                    ).addCallback(
                        EntityDatabaseCallback(
                            scope
                        )
                    ).build()
                    INSTANCE = instance
                    instance
                }
        }

        private class EntityDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(
                            database.wordDAO()
                        )
                    }
                }
            }
        }

        suspend fun populateDatabase(
            wordDAO: WordDAO
        ) {
            for (i in 0..20000) {
                val rem = i.rem(20)
                wordDAO.insert(Word("W:${i}", rem))
                Log.d("DB", "Inserted: $i")
            }
        }
    }

}
