package com.hemat.app.data

import com.hemat.app.data.local.BudgetEntity
import com.hemat.app.data.local.CategoryEntity
import com.hemat.app.data.local.HematDatabase
import com.hemat.app.data.local.TransactionEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Repository(private val db: HematDatabase) {
    val recent = db.transactions().recent(80)
    val categories = db.categories().all()
    val budgets = db.budgets().all()

    fun monthRange(offsetMonths: Int = 0): Pair<Long, Long> {
        val c = Calendar.getInstance().apply {
            add(Calendar.MONTH, offsetMonths)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val from = c.timeInMillis
        c.add(Calendar.MONTH, 1)
        val to = c.timeInMillis - 1
        return from to to
    }

    fun monthLabel(offsetMonths: Int = 0, lang: String = "id"): String {
        val c = Calendar.getInstance().apply {
            add(Calendar.MONTH, offsetMonths)
        }
        val locale = if (lang == "id") Locale("id", "ID") else Locale.ENGLISH
        val fmt = SimpleDateFormat("MMMM yyyy", locale)
        return fmt.format(c.time)
    }

    fun monthTransactions(from: Long, to: Long) = db.transactions().between(from, to)
    fun sumIn(from: Long, to: Long) = db.transactions().sumByKind("IN", from, to)
    fun sumOut(from: Long, to: Long) = db.transactions().sumByKind("OUT", from, to)

    suspend fun add(amount: Long, kind: String, category: String, note: String) {
        db.transactions().insert(
            TransactionEntity(amount = amount, kind = kind, category = category, note = note)
        )
    }

    suspend fun update(item: TransactionEntity) {
        db.transactions().insert(item)
    }

    suspend fun remove(item: TransactionEntity) = db.transactions().delete(item)

    suspend fun addCategory(name: String, kind: String) {
        if (name.isNotBlank()) {
            db.categories().insert(CategoryEntity(name.trim(), kind))
        }
    }

    suspend fun deleteCategory(name: String) = db.categories().delete(name)

    suspend fun setBudget(category: String, limit: Long) {
        db.budgets().upsert(BudgetEntity(category, limit))
    }

    suspend fun removeBudget(category: String) = db.budgets().delete(category)

    suspend fun seedDefaults() {
        val ins = listOf(
            CategoryEntity("Gaji", "IN"),
            CategoryEntity("Bonus", "IN"),
            CategoryEntity("Makan", "OUT"),
            CategoryEntity("Transport", "OUT"),
            CategoryEntity("Belanja", "OUT"),
            CategoryEntity("Tagihan", "OUT"),
            CategoryEntity("Hiburan", "OUT"),
            CategoryEntity("Lainnya", "OUT")
        )
        ins.forEach { db.categories().insert(it) }
    }

    suspend fun clearAll() = db.transactions().clear()
}
