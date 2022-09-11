package com.rcalencar.weather.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rcalencar.weather.repository.remote.locations.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM Location")
    suspend fun getAll(): List<Location>

    @Insert
    suspend fun insertAll(vararg location: Location)

    @Query("DELETE FROM Location")
    suspend fun delete()
}