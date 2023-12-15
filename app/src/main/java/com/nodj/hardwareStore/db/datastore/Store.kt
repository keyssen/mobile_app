package com.nodj.hardwareStore.db.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Store")
        val USERNAME = stringPreferencesKey("username")
        val USERROLE = stringPreferencesKey("userrole")
    }

    fun getUsername(): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[USERNAME] ?: ""
            }
    }

    fun getUserrole(): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[USERROLE] ?: ""
            }
    }

    private suspend fun saveStringValue(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun setUsername(username: String) {
        saveStringValue(USERNAME, username)
    }

    suspend fun setUserrole(userrole: String) {
        saveStringValue(USERROLE, userrole)
    }
}
