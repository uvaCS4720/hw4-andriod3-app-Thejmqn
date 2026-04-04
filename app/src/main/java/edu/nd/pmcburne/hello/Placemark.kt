package edu.nd.pmcburne.hello

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class VisualCenter(
    val latitude: Double,
    val longitude: Double
)

@Serializable
@Entity(tableName = "placemarks")
data class Placemark(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val tag_list: List<String>,
    val visual_center: VisualCenter
)

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String = Json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> = Json.decodeFromString(value)

    @TypeConverter
    fun fromVisualCenter(value: VisualCenter): String = Json.encodeToString(value)

    @TypeConverter
    fun toVisualCenter(value: String): VisualCenter = Json.decodeFromString(value)
}
