package com.hemat.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("hemat_prefs")

class LanguageStore(private val ctx: Context) {
    private val KEY_LANG = stringPreferencesKey("lang")
    private val KEY_API_KEY = stringPreferencesKey("ai_api_key")
    private val KEY_AI_PROVIDER = stringPreferencesKey("ai_provider")

    val lang: Flow<String> = ctx.dataStore.data.map { it[KEY_LANG] ?: "id" }
    val apiKey: Flow<String> = ctx.dataStore.data.map { it[KEY_API_KEY] ?: "" }
    val aiProvider: Flow<String> = ctx.dataStore.data.map { it[KEY_AI_PROVIDER] ?: "local" }

    suspend fun set(lang: String) {
        ctx.dataStore.edit { it[KEY_LANG] = lang }
    }

    suspend fun setAiSettings(provider: String, apiKey: String) {
        ctx.dataStore.edit {
            it[KEY_AI_PROVIDER] = provider
            it[KEY_API_KEY] = apiKey.trim()
        }
    }
}
