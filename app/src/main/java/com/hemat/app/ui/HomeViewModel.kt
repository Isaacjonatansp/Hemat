package com.hemat.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hemat.app.data.LanguageStore
import com.hemat.app.data.Repository
import com.hemat.app.data.local.TransactionEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val repo: Repository,
    private val langStore: LanguageStore,
) : ViewModel() {

    val lang = langStore.lang
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "id")

    val monthOffset = MutableStateFlow(0)

    val monthLabel: StateFlow<String> = combine(monthOffset, lang) { offset, l ->
        repo.monthLabel(offset, l)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    private val monthRange = monthOffset.map { repo.monthRange(it) }

    val sumIn = monthRange.flatMapLatest { (from, to) ->
        repo.sumIn(from, to)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val sumOut = monthRange.flatMapLatest { (from, to) ->
        repo.sumOut(from, to)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val monthTx = monthRange.flatMapLatest { (from, to) ->
        repo.monthTransactions(from, to)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories = repo.categories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val budgets = repo.budgets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun prevMonth() { monthOffset.value -= 1 }
    fun nextMonth() { monthOffset.value += 1 }
    fun resetMonth() { monthOffset.value = 0 }

    fun add(amount: Long, kind: String, category: String, note: String) {
        viewModelScope.launch { repo.add(amount, kind, category, note) }
    }

    fun update(item: TransactionEntity) {
        viewModelScope.launch { repo.update(item) }
    }

    fun remove(item: TransactionEntity) {
        viewModelScope.launch { repo.remove(item) }
    }

    fun addCategory(name: String, kind: String) {
        viewModelScope.launch { repo.addCategory(name, kind) }
    }

    fun deleteCategory(name: String) {
        viewModelScope.launch { repo.deleteCategory(name) }
    }

    fun setBudget(category: String, limit: Long) {
        viewModelScope.launch { repo.setBudget(category, limit) }
    }

    fun removeBudget(category: String) {
        viewModelScope.launch { repo.removeBudget(category) }
    }

    fun clearAll() {
        viewModelScope.launch { repo.clearAll() }
    }

    fun setLang(code: String) {
        viewModelScope.launch { langStore.set(code) }
    }
}
