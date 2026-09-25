package com.hemat.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("hemat_prefs")

class LanguageStore(private val ctx: Context) {
    private val KEY = stringPreferencesKey("lang")

    val lang: Flow<String> = ctx.dataStore.data.map { it[KEY] ?: "id" }

    suspend fun set(lang: String) {
        ctx.dataStore.edit { it[KEY] = lang }
    }
}
