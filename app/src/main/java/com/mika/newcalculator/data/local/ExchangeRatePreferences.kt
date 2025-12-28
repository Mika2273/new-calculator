package com.mika.newcalculator.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a DataStore instance named "settings"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Manages local storage for exchange rates using Jetpack DataStore Preferences.
 */
class ExchangeRatePreferences(private val context: Context) {

    companion object {
        // Define keys for storing data
        val RATE_KEY = floatPreferencesKey("exchange_rate")
        val DATE_KEY = stringPreferencesKey("exchange_date")
    }

    // Retrieve the saved exchange rate (returns null if not found)
    val savedRate: Flow<Float?> = context.dataStore.data.map { preferences ->
        preferences[RATE_KEY]
    }

    // Retrieve the saved date
    val savedDate: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[DATE_KEY]
    }

    /**
     * Saves the exchange rate and date to local storage.
     */
    suspend fun saveExchangeRate(rate: Float, date: String) {
        context.dataStore.edit { preferences ->
            preferences[RATE_KEY] = rate
            preferences[DATE_KEY] = date
        }
    }
}