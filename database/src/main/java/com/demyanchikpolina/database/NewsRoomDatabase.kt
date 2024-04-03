package com.demyanchikpolina.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.demyanchikpolina.database.dao.ArticleDao
import com.demyanchikpolina.database.models.ArticleDBO
import com.demyanchikpolina.database.utils.Converters

class NewsDatabase internal constructor(private val database: NewsRoomDatabase) {

    val articlesDao: ArticleDao
        get() = database.articlesDao()
}

@Database(entities = [ArticleDBO::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class NewsRoomDatabase : RoomDatabase() {

    abstract fun articlesDao(): ArticleDao
}

fun createDatabase(context: Context): NewsDatabase =
    NewsDatabase(
        database = Room
            .databaseBuilder(
                checkNotNull(context.applicationContext),
                NewsRoomDatabase::class.java,
                "news"
            )
            .build())