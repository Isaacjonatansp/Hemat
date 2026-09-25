package com.hemat.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TransactionEntity::class, CategoryEntity::class, BudgetEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HematDatabase : RoomDatabase() {
    abstract fun transactions(): TransactionDao
    abstract fun categories(): CategoryDao
    abstract fun budgets(): BudgetDao
}
