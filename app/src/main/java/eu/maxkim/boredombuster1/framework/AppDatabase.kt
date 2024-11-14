package eu.maxkim.boredombuster1.framework

import androidx.room.Database
import androidx.room.RoomDatabase
import eu.maxkim.boredombuster1.activity.framework.db.ActivityDao
import eu.maxkim.boredombuster1.activity.model.Activity

@Database(entities = [Activity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
}