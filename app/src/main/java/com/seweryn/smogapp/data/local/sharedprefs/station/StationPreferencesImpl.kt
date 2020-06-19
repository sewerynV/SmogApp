package com.seweryn.smogapp.data.local.sharedprefs.station

import android.content.Context
import android.content.SharedPreferences
import com.seweryn.smogapp.data.local.sharedprefs.SharedPrefs
import com.seweryn.smogapp.data.model.City
import com.seweryn.smogapp.data.model.Station
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class StationPreferencesImpl(context: Context): SharedPrefs(context), StationPreferences {
    private val PREFERENCE_KEY = "stationPrefs"
    private val STATION_ID = "station_id"
    private val STATION_NAME = "station_name"
    private val STATION_CITY_NAME = "station_city"
    private val STATION_PROVINCE_NAME = "station_province"

    private val prefSubject = BehaviorSubject.createDefault(sharedPreferences)

    private val prefChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ ->
            prefSubject.onNext(sharedPreferences)
        }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefChangeListener)
    }

    override fun preferenceKey() = PREFERENCE_KEY

    override fun getStationId(): Int {
        return getInt(STATION_ID)
    }

    override fun getStation(): Station {
        return Station(
            id = getInt(STATION_ID),
            stationName = getString(STATION_NAME),
            city = City(
                name = getString(STATION_CITY_NAME),
                provinceName = getString(STATION_PROVINCE_NAME)
            )
        )
    }

    override fun rememberStation(station: Station) {
        putInt(STATION_ID, station.id)
        putString(STATION_NAME, station.stationName)
        putString(STATION_CITY_NAME, station.city.name)
        putString(STATION_PROVINCE_NAME, station.city.provinceName)
    }

    override fun observeStation(): Observable<Station> {
        return prefSubject.map { getStation() }
    }

}