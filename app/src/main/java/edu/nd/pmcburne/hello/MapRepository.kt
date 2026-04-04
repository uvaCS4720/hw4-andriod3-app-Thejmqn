package edu.nd.pmcburne.hello

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.URL

class MapRepository(private val mapDao: MapDao) {

    val allPlacemarks: Flow<List<Placemark>> = mapDao.getAllPlacemarks()

    suspend fun syncPlacemarks() {
        withContext(Dispatchers.IO) {
            val url = "https://www.cs.virginia.edu/~wxt4gm/placemarks.json"
            val responseText = URL(url).readText()
            val jsonParser = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
            val placemarksFromApi = jsonParser.decodeFromString<List<Placemark>>(responseText)
            
            mapDao.insertPlacemarks(placemarksFromApi)

            placemarksFromApi.forEach { placemark ->
                mapDao.updatePlacemarkManual(
                    id = placemark.id,
                    name = placemark.name,
                    description = placemark.description,
                    tagList = placemark.tag_list,
                    visualCenter = placemark.visual_center
                )
            }
        }
    }
}
