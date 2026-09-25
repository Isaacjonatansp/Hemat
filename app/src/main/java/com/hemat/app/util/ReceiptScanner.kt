package com.hemat.app.util

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import kotlin.coroutines.resume

data class ReceiptScanResult(
    val amount: Long,
    val merchantName: String,
    val categorySuggestion: String,
    val fullText: String,
)

object ReceiptScanner {

    private val recognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    suspend fun scanReceipt(context: Context, imageUri: Uri): ReceiptScanResult? =
        withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                try {
                    val image = InputImage.fromFilePath(context, imageUri)
                    recognizer.process(image)
                        .addOnSuccessListener { visionText ->
                            val text = visionText.text
                            val parsed = parseReceiptText(text)
                            continuation.resume(parsed)
                        }
                        .addOnFailureListener {
                            continuation.resume(null)
                        }
                } catch (_: Exception) {
                    continuation.resume(null)
                }
            }
        }

    fun parseReceiptText(text: String): ReceiptScanResult {
        val lines = text.split("\n").map { it.trim() }.filter { it.isNotEmpty() }

        var merchant = ""
        for (i in 0 until minOf(3, lines.size)) {
            val line = lines[i]
            if (!line.contains("total", ignoreCase = true) &&
                !line.contains("tanggal", ignoreCase = true) &&
                line.length > 2
            ) {
                merchant = line
                break
            }
        }

        var detectedAmount = 0L
        val numbersFound = mutableListOf<Long>()
        val totalKeywords = listOf("total", "grand total", "jumlah", "bayar", "cash", "tunai", "net", "subtotal")

        for (line in lines) {
            val lower = line.lowercase()
            val containsKeyword = totalKeywords.any { lower.contains(it) }
            val amountsInLine = extractAmountsFromLine(line)

            if (containsKeyword && amountsInLine.isNotEmpty()) {
                detectedAmount = amountsInLine.maxOrNull() ?: 0L
                break
            } else {
                numbersFound.addAll(amountsInLine)
            }
        }

        if (detectedAmount == 0L && numbersFound.isNotEmpty()) {
            val plausibleAmounts = numbersFound.filter { it in 500..50000000L }
            detectedAmount = plausibleAmounts.maxOrNull() ?: (numbersFound.maxOrNull() ?: 0L)
        }

        val lowerText = text.lowercase()
        val category = when {
            lowerText.contains("indomaret") || lowerText.contains("alfamart") || lowerText.contains("supermarket") || lowerText.contains("mart") || lowerText.contains("pasar") -> "Belanja"
            lowerText.contains("resto") || lowerText.contains("kopi") || lowerText.contains("cafe") || lowerText.contains("warung") || lowerText.contains("food") || lowerText.contains("makan") || lowerText.contains("kitchen") -> "Makan"
            lowerText.contains("pertamina") || lowerText.contains("spbu") || lowerText.contains("shell") || lowerText.contains("gojek") || lowerText.contains("grab") || lowerText.contains("parkir") -> "Transport"
            lowerText.contains("pln") || lowerText.contains("pdam") || lowerText.contains("telkom") || lowerText.contains("indihome") -> "Tagihan"
            else -> "Belanja"
        }

        return ReceiptScanResult(
            amount = detectedAmount,
            merchantName = merchant,
            categorySuggestion = category,
            fullText = text,
        )
    }

    private fun extractAmountsFromLine(line: String): List<Long> {
        val result = mutableListOf<Long>()
        val cleanLine = line.replace("Rp", "", ignoreCase = true)
            .replace("IDR", "", ignoreCase = true)
            .trim()

        val pattern = Pattern.compile("(\\b\\d{1,3}(?:[.,]\\d{3})+|\\b\\d{3,8})")
        val matcher = pattern.matcher(cleanLine)

        while (matcher.find()) {
            val rawStr = matcher.group(1) ?: continue
            var digitsOnly = rawStr
            if (digitsOnly.contains(",") && digitsOnly.endsWith(",00")) {
                digitsOnly = digitsOnly.substringBefore(",00")
            } else if (digitsOnly.contains(".") && digitsOnly.endsWith(".00")) {
                digitsOnly = digitsOnly.substringBefore(".00")
            }
            digitsOnly = digitsOnly.replace(".", "").replace(",", "")

            val parsed = digitsOnly.toLongOrNull()
            if (parsed != null && parsed > 0) {
                result.add(parsed)
            }
        }
        return result
    }
}
