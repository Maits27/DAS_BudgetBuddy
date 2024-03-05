package com.example.budgetbuddy.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


/*******************************************************************************
 ****                Application Settings & User Preferences                ****
 *******************************************************************************/


/*************************************************
 **             Data Store Instance             **
 *************************************************/
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "PREFERENCES_SETTINGS")

@Singleton
class PreferencesRepository @Inject constructor(private val context: Context) : IGeneralPreferences {
    val PREFERENCE_LANGUAGE = stringPreferencesKey("preference_lang")
    val PREFERENCE_THEME_DARK = intPreferencesKey("preference_theme")
    val PRIMERO = booleanPreferencesKey("primero")
    /*------------------------------------------------
    |               Language preference              |
    ------------------------------------------------*/

    /**
     * Collects the first preference item generated by DataStores [Flow] and returns the language.
     */
    override fun language(): Flow<String> = context.dataStore.data.map {
        preferences -> preferences[PREFERENCE_LANGUAGE]?: Locale.getDefault().language
    }
    override suspend fun setLanguage(code: String) {
        context.dataStore.edit { settings ->  settings[PREFERENCE_LANGUAGE]=code}
    }

    override suspend fun saveThemePreference(theme: Int) {
        context.dataStore.edit { preferences ->
            preferences[PREFERENCE_THEME_DARK] = theme
        }
    }

    override fun getThemePreference(): Flow<Int> = context.dataStore.data.map {
        preferences -> preferences[PREFERENCE_THEME_DARK]?: 0
    }

    override fun getPrimero(): Flow<Boolean> = context.dataStore.data.map {
            preferences -> preferences[PRIMERO]?: true
    }
    override suspend fun primero() {
        context.dataStore.edit { preferences ->
            preferences[PRIMERO] = false
        }
    }

}