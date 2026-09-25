package com.hemat.app.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.hemat.app.data.local.TransactionEntity
import com.hemat.app.ui.theme.HematTheme
import com.hemat.app.util.ReceiptScanner
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AppRoot(vm: HomeViewModel) {
    val lang by vm.lang.collectAsState()
    var tab by remember { mutableIntStateOf(0) }
    val titles = listOf(T.s(lang, "home"), T.s(lang, "add"), T.s(lang, "budget"), T.s(lang, "setting"))
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Add, Icons.Filled.PieChart, Icons.Filled.Settings)

    Scaffold(
        topBar = { SimpleBar(titles[tab]) },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                titles.forEachIndexed { i, t ->
                    NavigationBarItem(
                        selected = tab == i,
                        onClick = { tab = i },
                        icon = { Icon(icons[i], contentDescription = t) },
                        label = { Text(t, fontWeight = if (tab == i) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }
        }
    ) { pad ->
        Crossfade(
            targetState = tab,
            animationSpec = tween(220),
            label = "tab",
            modifier = Modifier.padding(pad)
        ) { t ->
            when (t) {
                0 -> HomeScreen(vm, lang)
                1 -> AddScreen(vm, lang) { tab = 0 }
                2 -> BudgetScreen(vm, lang)
                else -> SettingsScreen(vm, lang)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleBar(title: String) {
    TopAppBar(
        title = {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

// Helper untuk memilih Icon Kategori
fun getCategoryIcon(name: String): ImageVector {
    return when (name.lowercase(Locale.ROOT)) {
        "gaji", "salary" -> Icons.Default.AccountBalanceWallet
        "bonus" -> Icons.Default.Stars
        "makan", "food", "kuliner" -> Icons.Default.Restaurant
        "transport", "transportasi", "bensin" -> Icons.Default.DirectionsCar
        "belanja", "shopping" -> Icons.Default.ShoppingBag
        "tagihan", "bills", "listrik", "air" -> Icons.AutoMirrored.Filled.ReceiptLong
        "hiburan", "entertainment", "game" -> Icons.Default.SportsEsports
        else -> Icons.Default.Category
    }
}

// ---------- HOME & CATEGORY BAR CHART ----------

@Composable
private fun CategoryBarChartCard(
    monthTx: List<TransactionEntity>,
    lang: String
) {
    var selectedKind by remember { mutableStateOf("OUT") } // OUT or IN

    val filtered = monthTx.filter { it.kind == selectedKind }
    val totalSum = filtered.sumOf { it.amount }

    val categoryGrouped = remember(filtered) {
        filtered.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Icon, Title & Segmented Switcher (Pengeluaran / Pemasukan)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.BarChart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        T.s(lang, "chart"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                SingleChoiceSegmentedButtonRow {
                    SegmentedButton(
                        selected = selectedKind == "OUT",
                        onClick = { selectedKind = "OUT" },
                        shape = SegmentedButtonDefaults.itemShape(0, 2),
                        label = { Text(T.s(lang, "expense"), style = MaterialTheme.typography.labelSmall) }
                    )
                    SegmentedButton(
                        selected = selectedKind == "IN",
                        onClick = { selectedKind = "IN" },
                        shape = SegmentedButtonDefaults.itemShape(1, 2),
                        label = { Text(T.s(lang, "income"), style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            if (totalSum == 0L) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        T.s(lang, "no_data_chart"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                categoryGrouped.forEach { (categoryName, amount) ->
                    val percentage = if (totalSum > 0) (amount.toDouble() / totalSum * 100) else 0.0
                    val frac = (percentage / 100).toFloat().coerceIn(0f, 1f)
                    val animProgress by animateFloatAsState(frac, animationSpec = tween(500), label = "chartBar")

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    getCategoryIcon(categoryName),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    categoryName,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.padding(end = 6.dp)
                                ) {
                                    Text(
                                        String.format(Locale.US, "%.1f%%", percentage),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    amount.rp(),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        // Bar visual
                        LinearProgressIndicator(
                            progress = { animProgress },
                            color = if (selectedKind == "OUT") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(vm: HomeViewModel, lang: String) {
    val sumIn by vm.sumIn.collectAsState()
    val sumOut by vm.sumOut.collectAsState()
    val monthTx by vm.monthTx.collectAsState()
    val monthLabel by vm.monthLabel.collectAsState()
    val balance = sumIn - sumOut

    var editingTx by remember { mutableStateOf<TransactionEntity?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var sortOrder by remember { mutableStateOf("newest") } // newest, oldest, highest, lowest, category
    var showSortMenu by remember { mutableStateOf(false) }

    val availableCategories = remember(monthTx) {
        monthTx.map { it.category }.distinct().sorted()
    }

    val filteredTx = remember(monthTx, selectedCategoryFilter, sortOrder) {
        var list = monthTx
        if (selectedCategoryFilter != null) {
            list = list.filter { it.category == selectedCategoryFilter }
        }
        when (sortOrder) {
            "oldest" -> list.sortedBy { it.timestamp }
            "highest" -> list.sortedByDescending { it.amount }
            "lowest" -> list.sortedBy { it.amount }
            "category" -> list.sortedBy { it.category }
            else -> list.sortedByDescending { it.timestamp }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // --- SUMMARY CARD (Gradient Emerald) ---
        item(key = "summary") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        // Month Navigator Switcher
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { vm.prevMonth() },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .size(36.dp)
                            ) {
                                Icon(
                                    Icons.Filled.ChevronLeft,
                                    contentDescription = "Prev",
                                    tint = Color.White
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.clickable { vm.resetMonth() }
                            ) {
                                Text(
                                    text = monthLabel,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }

                            IconButton(
                                onClick = { vm.nextMonth() },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .size(36.dp)
                            ) {
                                Icon(
                                    Icons.Filled.ChevronRight,
                                    contentDescription = "Next",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Balance Display
                        Text(
                            T.s(lang, "balance"),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            balance.rp(),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 32.sp
                        )

                        Spacer(Modifier.height(16.dp))

                        // Income & Expense Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Income Pill
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.25f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.TrendingUp,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            T.s(lang, "income"),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                        Text(
                                            sumIn.rp(),
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }

                            // Expense Pill
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.25f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.TrendingDown,
                                            contentDescription = null,
                                            tint = Color(0xFFFFB4AB),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            T.s(lang, "expense"),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                        Text(
                                            sumOut.rp(),
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- DIAGRAM BATANG KATEGORI CARD ---
        item(key = "chart") {
            CategoryBarChartCard(monthTx = monthTx, lang = lang)
        }

        // --- RECENT HEADER & SORT/FILTER CONTROLS ---
        item(key = "header") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        T.s(lang, "recent"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Box {
                        OutlinedButton(
                            onClick = { showSortMenu = true },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(
                                when (sortOrder) {
                                    "oldest" -> T.s(lang, "sort_oldest")
                                    "highest" -> T.s(lang, "sort_highest")
                                    "lowest" -> T.s(lang, "sort_lowest")
                                    "category" -> T.s(lang, "sort_category")
                                    else -> T.s(lang, "sort_newest")
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(T.s(lang, "sort_newest")) },
                                onClick = { sortOrder = "newest"; showSortMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text(T.s(lang, "sort_oldest")) },
                                onClick = { sortOrder = "oldest"; showSortMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text(T.s(lang, "sort_highest")) },
                                onClick = { sortOrder = "highest"; showSortMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text(T.s(lang, "sort_lowest")) },
                                onClick = { sortOrder = "lowest"; showSortMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text(T.s(lang, "sort_category")) },
                                onClick = { sortOrder = "category"; showSortMenu = false }
                            )
                        }
                    }
                }

                // Category Filter Chips
                if (availableCategories.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCategoryFilter == null,
                                onClick = { selectedCategoryFilter = null },
                                label = { Text(T.s(lang, "all_categories"), style = MaterialTheme.typography.labelSmall) },
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                        items(availableCategories) { cat ->
                            FilterChip(
                                selected = selectedCategoryFilter == cat,
                                onClick = { selectedCategoryFilter = if (selectedCategoryFilter == cat) null else cat },
                                label = { Text(cat, style = MaterialTheme.typography.labelSmall) },
                                leadingIcon = {
                                    Icon(getCategoryIcon(cat), contentDescription = null, modifier = Modifier.size(14.dp))
                                },
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // --- TRANSACTION LIST ITEMS ---
        if (filteredTx.isEmpty()) {
            item(key = "empty") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ReceiptLong,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            T.s(lang, "empty"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredTx, key = { it.id }) { tx ->
                TxRow(
                    tx = tx,
                    lang = lang,
                    onEdit = { editingTx = tx },
                    onDelete = { vm.remove(tx) }
                )
            }
        }
    }

    editingTx?.let { tx ->
        EditTransactionDialog(
            tx = tx,
            vm = vm,
            lang = lang,
            onDismiss = { editingTx = null }
        )
    }
}

@Composable
private fun TxRow(
    tx: TransactionEntity,
    lang: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val fmt = remember { SimpleDateFormat("d MMM, HH:mm", Locale.getDefault()) }
    val isIn = tx.kind == "IN"
    val icon = getCategoryIcon(tx.category)

    val iconBg = if (isIn) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val iconTint = if (isIn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onEdit() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon Badge
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = tx.category,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    tx.category,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (tx.note.isNotBlank()) {
                    Text(
                        tx.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    fmt.format(Date(tx.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(Modifier.width(8.dp))

            // Amount
            Text(
                (if (isIn) "+" else "-") + tx.amount.rp(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = if (isIn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )

            // Actions
            IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = T.s(lang, "edit"),
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(18.dp)
                )
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = T.s(lang, "delete"),
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun EditTransactionDialog(
    tx: TransactionEntity,
    vm: HomeViewModel,
    lang: String,
    onDismiss: () -> Unit
) {
    var amountText by remember { mutableStateOf(tx.amount.toString()) }
    var note by remember { mutableStateOf(tx.note) }
    var category by remember { mutableStateOf(tx.category) }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = { Text(T.s(lang, "edit_tx"), fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { c -> c.isDigit() }; isError = false },
                    label = { Text(T.s(lang, "amount")) },
                    prefix = { Text("Rp ") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    isError = isError,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text(T.s(lang, "category")) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(T.s(lang, "note")) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toLongOrNull() ?: 0L
                    if (amount <= 0 || category.isBlank()) {
                        isError = true
                        return@Button
                    }
                    vm.update(tx.copy(amount = amount, category = category.trim(), note = note.trim()))
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(T.s(lang, "save"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(T.s(lang, "cancel"))
            }
        }
    )
}

// ---------- ADD & SCAN STRUK ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddScreen(vm: HomeViewModel, lang: String, onSaved: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val cats by vm.categories.collectAsState()
    var amountText by remember { mutableStateOf("") }
    var isIn by remember { mutableStateOf(false) }
    var picked by remember { mutableStateOf<String?>(null) }
    var note by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    var isScanning by remember { mutableStateOf(false) }
    var scanMessage by remember { mutableStateOf<String?>(null) }
    var tempPhotoFile by remember { mutableStateOf<File?>(null) }

    // Launcher untuk mengambil foto LANGSUNG dengan Kamera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val file = tempPhotoFile
        if (success && file != null && file.exists()) {
            isScanning = true
            scanMessage = T.s(lang, "scanning")
            scope.launch {
                val uri = Uri.fromFile(file)
                val res = ReceiptScanner.scanReceipt(context, uri)
                isScanning = false

                // Hapus file foto sementara langsung agar HP tidak penuh!
                try {
                    if (file.exists()) { file.delete() }
                } catch (_: Exception) {}

                if (res != null && res.amount > 0) {
                    amountText = res.amount.toString()
                    isIn = false // Struk belanja selalu Pengeluaran (OUT)
                    picked = res.categorySuggestion
                    note = if (res.merchantName.isNotBlank()) "Struk ${res.merchantName}" else "Struk Belanja"
                    scanMessage = "${T.s(lang, "scan_success")} ${res.amount.rp()} • ${T.s(lang, "auto_deleted")}"
                } else {
                    scanMessage = T.s(lang, "scan_failed")
                }
            }
        } else {
            // Batal ambil foto -> langsung hapus file temp
            file?.delete()
        }
    }

    // Launcher opsional dari Galeri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            isScanning = true
            scanMessage = T.s(lang, "scanning")
            scope.launch {
                val res = ReceiptScanner.scanReceipt(context, uri)
                isScanning = false
                if (res != null && res.amount > 0) {
                    amountText = res.amount.toString()
                    isIn = false
                    picked = res.categorySuggestion
                    note = if (res.merchantName.isNotBlank()) "Struk ${res.merchantName}" else "Struk Belanja"
                    scanMessage = "${T.s(lang, "scan_success")} ${res.amount.rp()}"
                } else {
                    scanMessage = T.s(lang, "scan_failed")
                }
            }
        }
    }

    val kind = if (isIn) "IN" else "OUT"
    val visible = cats.filter { it.kind == kind }.map { it.name }
    val fallback = if (kind == "IN") listOf("Gaji", "Bonus") else listOf("Makan", "Transport", "Belanja", "Tagihan", "Hiburan", "Lainnya")
    val options = (visible + fallback).distinct()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // --- SCAN STRUK BANNER CARD ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.DocumentScanner,
                            contentDescription = "Scan Struk",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            T.s(lang, "scan_receipt"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            T.s(lang, "scan_desc"),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Tombol Foto Langsung (Kamera)
                    Button(
                        onClick = {
                            try {
                                val file = File.createTempFile("receipt_", ".jpg", context.cacheDir)
                                tempPhotoFile = file
                                val uri = FileProvider.getUriForFile(context, "com.hemat.app.fileprovider", file)
                                cameraLauncher.launch(uri)
                            } catch (_: Exception) {
                                scanMessage = T.s(lang, "scan_failed")
                            }
                        },
                        enabled = !isScanning,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isScanning) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Filled.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(T.s(lang, "take_photo"), fontWeight = FontWeight.Bold)
                        }
                    }

                    // Tombol Opsi dari Galeri
                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        enabled = !isScanning,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(T.s(lang, "gallery"), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Notification Message for Scan Status
        scanMessage?.let { msg ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    msg,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
        }

        // Segmented Switcher
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = !isIn,
                onClick = { isIn = false; picked = null },
                shape = SegmentedButtonDefaults.itemShape(0, 2),
                label = { Text(T.s(lang, "expense"), fontWeight = FontWeight.SemiBold) }
            )
            SegmentedButton(
                selected = isIn,
                onClick = { isIn = true; picked = null },
                shape = SegmentedButtonDefaults.itemShape(1, 2),
                label = { Text(T.s(lang, "income"), fontWeight = FontWeight.SemiBold) }
            )
        }

        // Amount Input
        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it.filter { c -> c.isDigit() }; error = false },
            label = { Text(T.s(lang, "amount")) },
            prefix = { Text("Rp ", fontWeight = FontWeight.Bold) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            isError = error,
            shape = RoundedCornerShape(16.dp),
            textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth()
        )
        if (error) {
            Text(
                T.s(lang, "invalid"),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium
            )
        }

        // Category Section
        Text(
            T.s(lang, "category"),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.chunked(2).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { c ->
                        val icon = getCategoryIcon(c)
                        FilterChip(
                            selected = picked == c,
                            onClick = { picked = c },
                            label = { Text(c, fontWeight = FontWeight.Medium) },
                            leadingIcon = {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }

        // Note Input
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text(T.s(lang, "note")) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        // Save Button
        Button(
            onClick = {
                val amount = amountText.toLongOrNull() ?: 0L
                val cat = picked
                if (amount <= 0 || cat == null) {
                    error = amount <= 0
                    return@Button
                }
                vm.add(amount, kind, cat, note.trim())
                amountText = ""
                note = ""
                picked = null
                onSaved()
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                T.s(lang, "save"),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ---------- BUDGET ----------

@Composable
private fun BudgetScreen(vm: HomeViewModel, lang: String) {
    val budgets by vm.budgets.collectAsState()
    val monthTx by vm.monthTx.collectAsState()
    val cats by vm.categories.collectAsState()
    var picked by remember { mutableStateOf("Makan") }
    var limitText by remember { mutableStateOf("") }

    val outCats = (cats.filter { it.kind == "OUT" }.map { it.name } +
        listOf("Makan", "Transport", "Belanja", "Tagihan", "Hiburan", "Lainnya")).distinct()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item(key = "form") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        T.s(lang, "set_budget"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                val i = outCats.indexOf(picked)
                                picked = outCats[(i - 1 + outCats.size) % outCats.size]
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("<") }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                picked,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                val i = outCats.indexOf(picked)
                                picked = outCats[(i + 1) % outCats.size]
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) { Text(">") }
                    }

                    OutlinedTextField(
                        value = limitText,
                        onValueChange = { limitText = it.filter { c -> c.isDigit() } },
                        label = { Text(T.s(lang, "limit")) },
                        prefix = { Text("Rp ", fontWeight = FontWeight.Bold) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            val v = limitText.toLongOrNull() ?: return@Button
                            if (v > 0) {
                                vm.setBudget(picked, v)
                                limitText = ""
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(T.s(lang, "save"), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        items(budgets, key = { it.category }) { b ->
            val used = monthTx.filter { it.kind == "OUT" && it.category == b.category }.sumOf { it.amount }
            val frac = if (b.limit > 0) (used.toFloat() / b.limit).coerceIn(0f, 1f) else 0f
            val animProgress by animateFloatAsState(frac, animationSpec = tween(400), label = "bar")
            val over = used > b.limit

            val barColor by animateColorAsState(
                targetValue = when {
                    over -> MaterialTheme.colorScheme.error
                    frac > 0.8f -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.primary
                },
                label = "barColor"
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                getCategoryIcon(b.category),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                b.category,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        IconButton(onClick = { vm.removeBudget(b.category) }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = T.s(lang, "delete"),
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = { animProgress },
                        color = barColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${T.s(lang, "used")} ${used.rp()} / ${b.limit.rp()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            if (over) T.s(lang, "over") else "${T.s(lang, "left")} ${(b.limit - used).coerceAtLeast(0).rp()}",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (over) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ---------- SETTINGS ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(vm: HomeViewModel, lang: String) {
    var confirmClear by remember { mutableStateOf(false) }
    var showAddCategory by remember { mutableStateOf(false) }
    var newCatName by remember { mutableStateOf("") }
    var newCatKind by remember { mutableStateOf("OUT") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Language Selector Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    T.s(lang, "lang"),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = lang == "id",
                        onClick = { vm.setLang("id") },
                        shape = SegmentedButtonDefaults.itemShape(0, 2),
                        label = { Text("Indonesia", fontWeight = FontWeight.Medium) }
                    )
                    SegmentedButton(
                        selected = lang == "en",
                        onClick = { vm.setLang("en") },
                        shape = SegmentedButtonDefaults.itemShape(1, 2),
                        label = { Text("English", fontWeight = FontWeight.Medium) }
                    )
                }
            }
        }

        // Manage Categories Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    T.s(lang, "manage_categories"),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Button(
                    onClick = { showAddCategory = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(T.s(lang, "add_category"), fontWeight = FontWeight.Bold)
                }
            }
        }

        // Clear Data Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { confirmClear = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(T.s(lang, "clear"), fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showAddCategory) {
        AlertDialog(
            onDismissRequest = { showAddCategory = false },
            shape = RoundedCornerShape(24.dp),
            title = { Text(T.s(lang, "add_category"), fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = newCatKind == "OUT",
                            onClick = { newCatKind = "OUT" },
                            shape = SegmentedButtonDefaults.itemShape(0, 2),
                            label = { Text(T.s(lang, "expense")) }
                        )
                        SegmentedButton(
                            selected = newCatKind == "IN",
                            onClick = { newCatKind = "IN" },
                            shape = SegmentedButtonDefaults.itemShape(1, 2),
                            label = { Text(T.s(lang, "income")) }
                        )
                    }
                    OutlinedTextField(
                        value = newCatName,
                        onValueChange = { newCatName = it },
                        label = { Text(T.s(lang, "category_name")) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCatName.isNotBlank()) {
                            vm.addCategory(newCatName.trim(), newCatKind)
                            newCatName = ""
                            showAddCategory = false
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(T.s(lang, "save"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCategory = false }) {
                    Text(T.s(lang, "cancel"))
                }
            }
        )
    }

    if (confirmClear) {
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            shape = RoundedCornerShape(24.dp),
            title = { Text(T.s(lang, "clear"), fontWeight = FontWeight.Bold) },
            text = { Text(T.s(lang, "confirm_clear")) },
            confirmButton = {
                Button(
                    onClick = { vm.clearAll(); confirmClear = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(T.s(lang, "yes"))
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmClear = false }) {
                    Text(T.s(lang, "cancel"))
                }
            }
        )
    }
}

// ---------- PREVIEWS ----------

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HematTheme {
        Surface {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text("Saldo Bulan Ini", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.8f))
                        Text("Rp 12.500.000", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
