package com.github.jan222ik.jetpacklivedatapaginationwithroomfromvmexample

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    val word: String,
    val freq: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}