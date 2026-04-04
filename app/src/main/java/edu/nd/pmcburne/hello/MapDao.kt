package edu.nd.pmcburne.hello

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MapDao {
    @Query("SELECT * FROM placemarks")
    fun getAllPlacemarks(): Flow<List<Placemark>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlacemarks(placemarks: List<Placemark>)

    @Query("""
        UPDATE placemarks 
        SET name = :name, 
            description = :description, 
            tag_list = :tagList, 
            visual_center = :visualCenter 
        WHERE id = :id
    """)
    suspend fun updatePlacemarkManual(
        id: Int, 
        name: String, 
        description: String, 
        tagList: List<String>, 
        visualCenter: VisualCenter
    ): Int
}
