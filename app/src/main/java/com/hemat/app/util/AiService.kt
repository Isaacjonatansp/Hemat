package com.hemat.app.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object AiService {

    suspend fun fetchAiResponse(
        provider: String,
        apiKey: String,
        systemContext: String,
        userPrompt: String
    ): String? = withContext(Dispatchers.IO) {
        if (apiKey.isBlank() || provider == "local") return@withContext null

        try {
            when (provider.lowercase()) {
                "gemini" -> callGeminiApi(apiKey, systemContext, userPrompt)
                "openai" -> callOpenAiApi(apiKey, systemContext, userPrompt)
                else -> null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun callGeminiApi(apiKey: String, systemContext: String, userPrompt: String): String? {
        val urlString = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        conn.doOutput = true
        conn.connectTimeout = 12000
        conn.readTimeout = 12000

        val fullPrompt = "$systemContext\n\nUser Question: $userPrompt"

        val rootJson = JSONObject().apply {
            val contentsArray = JSONArray().apply {
                val contentObj = JSONObject().apply {
                    val partsArray = JSONArray().apply {
                        put(JSONObject().apply { put("text", fullPrompt) })
                    }
                    put("parts", partsArray)
                }
                put(contentObj)
            }
            put("contents", contentsArray)
        }

        OutputStreamWriter(conn.outputStream, "UTF-8").use { os ->
            os.write(rootJson.toString())
            os.flush()
        }

        if (conn.responseCode == 200) {
            val reader = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
            val responseText = reader.readText()
            reader.close()

            val responseJson = JSONObject(responseText)
            val candidates = responseJson.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    return parts.getJSONObject(0).optString("text").trim()
                }
            }
        }
        return null
    }

    private fun callOpenAiApi(apiKey: String, systemContext: String, userPrompt: String): String? {
        val urlString = "https://api.openai.com/v1/chat/completions"
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        conn.setRequestProperty("Authorization", "Bearer $apiKey")
        conn.doOutput = true
        conn.connectTimeout = 12000
        conn.readTimeout = 12000

        val rootJson = JSONObject().apply {
            put("model", "gpt-4o-mini")
            val messagesArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put("content", systemContext)
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", userPrompt)
                })
            }
            put("messages", messagesArray)
            put("max_tokens", 800)
        }

        OutputStreamWriter(conn.outputStream, "UTF-8").use { os ->
            os.write(rootJson.toString())
            os.flush()
        }

        if (conn.responseCode == 200) {
            val reader = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
            val responseText = reader.readText()
            reader.close()

            val responseJson = JSONObject(responseText)
            val choices = responseJson.optJSONArray("choices")
            if (choices != null && choices.length() > 0) {
                val firstChoice = choices.getJSONObject(0)
                val message = firstChoice.optJSONObject("message")
                val content = message?.optString("content")
                if (!content.isNullOrBlank()) {
                    return content.trim()
                }
            }
        }
        return null
    }
}
