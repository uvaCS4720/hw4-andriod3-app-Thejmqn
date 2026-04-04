package edu.nd.pmcburne.hello

import android.app.Application

class MapApplication: Application() {
    private val database by lazy { MapDatabase.getDatabase(this) }
    val repository by lazy { MapRepository(database.mapDao()) }
}
