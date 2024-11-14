package eu.maxkim.boredombuster1.activity.framework.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.maxkim.boredombuster1.activity.model.Activity

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity")
    fun getAll(): LiveData<List<Activity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: Activity)

    @Delete
    suspend fun delete(activity: Activity)

    @Query("SELECT EXISTS(SELECT * FROM activity WHERE `key` = :key)")
    suspend fun isActivitySaved(key : String) : Boolean
}