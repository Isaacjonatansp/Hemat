# 💰 Hemat — Pencatat Keuangan Pribadi

Aplikasi Android native sederhana, cepat, dan modern berbasis **Kotlin + Jetpack Compose (Material 3)**.
Data disimpan **100% secara lokal** menggunakan **Room Database** tanpa membutuhkan koneksi internet, tanpa iklan, dan menjaga privasi data keuangan Anda.

---

## ✨ Fitur Utama

- 📝 **Pencatatan Keuangan Lengkap**: Catat pemasukan dan pengeluaran secara terorganisir lengkap dengan kategori dan catatan opsional.
- 📸 **Scan Struk Belanja Otomatis (OCR)**: Pindai struk belanja secara langsung menggunakan Kamera atau Galeri. Sistem akan menghitung total belanja secara otomatis dan menghapus foto sementara agar memori HP tidak penuh.
- 📊 **Diagram Batang Kategori**: Visualisasi diagram batang pengeluaran/pemasukan per kategori lengkap dengan persentase penggunaan (misal: `45.2%`) dan nominal angka.
- 🔀 **Filter & Urutkan (Sort) di Beranda**:
  - Filter catatan berdasarkan chip kategori.
  - Urutkan transaksi berdasarkan **Terbaru**, **Terlama**, **Nominal Terbesar**, **Nominal Terkecil**, atau **A-Z Kategori**.
- 🎯 **Manajemen Budget & Anggaran**: Tetapkan batas anggaran per kategori lengkap dengan indikator batas dan progress bar warna otomatis.
- 🌐 **Dukungan Multi-bahasa**: Pilihan Bahasa Indonesia dan Bahasa Inggris yang dapat diubah secara instan dari menu Pengaturan.
- ⌨️ **Keyboard & Interface Responsif**: Form penambahan dan edit catatan responsif terhadap Soft Keyboard (*IME Padding & Auto-scroll*).
- 🌙 **Tema Otomatis**: Mendukung Tema Terang (Light Mode) dan Tema Gelap (Dark Mode) sesuai sistem HP.

---

## 🛠️ Teknologi & Arsitektur

- **Bahasa**: Kotlin
- **UI Framework**: Jetpack Compose + Material 3
- **Database**: Room (SQLite local database)
- **OCR Engine**: Google ML Kit Text Recognition (Scan Struk)
- **Asynchronous**: Kotlin Coroutines & Flow
- **Arsitektur**: MVVM (Model-View-ViewModel) + Repository Pattern

---

## 🚀 Cara Menjalankan Project

1. **Persyaratan**:
   - Android Studio (Hedgehog atau versi lebih baru)
   - JDK 17
   - Android Device / Emulator (Minimum Android 8.0 / SDK 26)

2. **Langkah-langkah**:
   ```bash
   # Clone repository ini
   git clone https://github.com/Isaacjonatansp/Hemat.git
   ```
3. Buka folder proyek di **Android Studio**.
4. Lakukan **Sync Gradle**.
5. Jalankan (*Run*) ke Emulator atau Perangkat Fisik.

---

## 📦 Menguji File APK

File APK siap pakai hasil kompilasi tersedia di:
- Folder utama proyek: `Hemat.apk`
- Output Gradle: `app/build/outputs/apk/debug/app-debug.apk`

---

## 📁 Struktur Kode Utama

- `app/src/main/java/com/hemat/app/`
  - `data/local/` — Entities, DAO, dan HematDatabase (Room)
  - `data/Repository.kt` — Agregasi data & query transaksi
  - `data/LanguageStore.kt` — DataStore pilihan bahasa (ID/EN)
  - `ui/AppRoot.kt` — Halaman UI Compose (Beranda, Diagram, Tambah/Scan, Budget, Pengaturan)
  - `ui/HomeViewModel.kt` — State Management & logika bisnis
  - `ui/Strings.kt` — Lokalisasi kamus bahasa & format mata uang Rupiah
  - `util/ReceiptScanner.kt` — Implementasi ML Kit OCR untuk Scan Struk
