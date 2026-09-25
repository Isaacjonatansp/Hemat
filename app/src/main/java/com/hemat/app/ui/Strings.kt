package com.hemat.app.ui

object T {
    fun s(lang: String, key: String): String {
        val id = lang == "id"
        return when (key) {
            "home" -> if (id) "Beranda" else "Home"
            "add" -> if (id) "Tambah" else "Add"
            "budget" -> if (id) "Budget" else "Budget"
            "setting" -> if (id) "Pengaturan" else "Settings"
            "balance" -> if (id) "Saldo" else "Balance"
            "income" -> if (id) "Masuk" else "Income"
            "expense" -> if (id) "Keluar" else "Expense"
            "recent" -> if (id) "Terakhir" else "Recent"
            "empty" -> if (id) "Belum ada catatan" else "No records yet"
            "amount" -> if (id) "Nominal" else "Amount"
            "category" -> if (id) "Kategori" else "Category"
            "note" -> if (id) "Catatan (opsional)" else "Note (optional)"
            "save" -> if (id) "Simpan" else "Save"
            "limit" -> if (id) "Batas" else "Limit"
            "used" -> if (id) "Terpakai" else "Spent"
            "left" -> if (id) "Sisa" else "Left"
            "over" -> if (id) "Lewat batas" else "Over budget"
            "set_budget" -> if (id) "Atur budget" else "Set budget"
            "lang" -> if (id) "Bahasa" else "Language"
            "delete" -> if (id) "Hapus" else "Delete"
            "edit" -> if (id) "Edit" else "Edit"
            "edit_tx" -> if (id) "Edit Transaksi" else "Edit Transaction"
            "add_category" -> if (id) "Tambah Kategori" else "Add Category"
            "category_name" -> if (id) "Nama Kategori" else "Category Name"
            "manage_categories" -> if (id) "Kelola Kategori" else "Manage Categories"
            "clear" -> if (id) "Hapus semua data" else "Clear all data"
            "confirm_clear" -> if (id) "Yakin hapus semua transaksi?" else "Delete all transactions?"
            "cancel" -> if (id) "Batal" else "Cancel"
            "close" -> if (id) "Tutup" else "Close"
            "yes" -> if (id) "Ya" else "Yes"
            "invalid" -> if (id) "Nominal tidak valid" else "Invalid amount"
            "scan_receipt" -> if (id) "Scan Struk Belanja (Beta)" else "Scan Receipt (Beta)"
            "scan_desc" -> if (id) "Masih dalam pengembangan • Foto struk langsung, total menghitung otomatis" else "In development • Take photo directly, auto calculates"
            "scanning" -> if (id) "Menganalisis foto struk..." else "Analyzing receipt photo..."
            "scan_success" -> if (id) "Struk terdeteksi! Total mengurangi saldo:" else "Receipt detected! Total deducted:"
            "scan_failed" -> if (id) "Gagal membaca struk. Coba foto yang lebih jelas." else "Failed to read receipt. Try a clearer photo."
            "take_photo" -> if (id) "Kamera" else "Camera"
            "gallery" -> if (id) "Galeri" else "Gallery"
            "auto_deleted" -> if (id) "Foto struk otomatis dihapus (HP tidak penuh)" else "Photo deleted automatically"
            "chart" -> if (id) "Diagram Kategori" else "Category Chart"
            "sort" -> if (id) "Urutkan" else "Sort"
            "sort_newest" -> if (id) "Terbaru" else "Newest"
            "sort_oldest" -> if (id) "Terlama" else "Oldest"
            "sort_highest" -> if (id) "Nominal Terbesar" else "Highest Amount"
            "sort_lowest" -> if (id) "Nominal Terkecil" else "Lowest Amount"
            "sort_category" -> if (id) "A-Z Kategori" else "Category A-Z"
            "all_categories" -> if (id) "Semua" else "All"
            "pct" -> if (id) "Persentase" else "Percentage"
            "no_data_chart" -> if (id) "Belum ada data transaksi di bulan ini" else "No transaction data for this month"
            "filter_category" -> if (id) "Filter Kategori" else "Filter Category"
            else -> key
        }
    }
}

fun Long.rp(): String {
    val s = this.toString()
    val b = StringBuilder()
    var c = 0
    for (i in s.length - 1 downTo 0) {
        b.append(s[i])
        c++
        if (c == 3 && i != 0) { b.append('.'); c = 0 }
    }
    return "Rp " + b.reverse().toString()
}
