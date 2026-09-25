package com.hemat.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC LIMIT :limit")
    fun recent(limit: Int = 60): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE timestamp BETWEEN :from AND :to ORDER BY timestamp DESC")
    fun between(from: Long, to: Long): Flow<List<TransactionEntity>>

    @Query("SELECT IFNULL(SUM(amount),0) FROM transactions WHERE kind = :kind AND timestamp BETWEEN :from AND :to")
    fun sumByKind(kind: String, from: Long, to: Long): Flow<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TransactionEntity): Long

    @Delete
    suspend fun delete(item: TransactionEntity)

    @Query("SELECT COUNT(*) FROM transactions")
    fun count(): Flow<Int>

    @Query("DELETE FROM transactions")
    suspend fun clear()
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun all(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: CategoryEntity)

    @Query("DELETE FROM categories WHERE name = :name")
    suspend fun delete(name: String)
}

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets")
    fun all(): Flow<List<BudgetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: BudgetEntity)

    @Query("DELETE FROM budgets WHERE category = :category")
    suspend fun delete(category: String)
}
