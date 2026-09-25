# Hemat — Pencatat Keuangan Pribadi

Aplikasi Android native (Kotlin + Jetpack Compose, Material3).
Data 100% lokal via Room. Tanpa internet, tanpa iklan, tanpa emoji.

## Fitur v1
- Catat pemasukan / pengeluaran + kategori + catatan
- Ringkasan saldo bulan berjalan di Beranda
- Budget per kategori + progress bar + status sisa / lewat batas
- Ganti bahasa Indonesia / English dari Pengaturan
- Hapus transaksi satuan + hapus semua data

## Kenapa ringan + smooth 120Hz
- Native Compose, tanpa WebView / bridge JS
- List pakai `LazyColumn` + key stabil (tanpa nested scroll boros)
- Animasi hanya `Crossfade` 220ms antar tab + progress bar 300ms
- State via `Flow` + `WhileSubscribed(5000)`, query Room ringan
- Release aktifkan R8 + shrinkResources

## Cara buka
1. Install Android Studio (Hedgehog+), JDK 17
2. Open folder ini sebagai project
3. Sync Gradle, Run ke emulator / HP (minSdk 26 / Android 8.0)

## Struktur
- `data/local/` — Room: entities, DAO, database
- `data/Repository.kt` — agregasi + range bulan
- `data/LanguageStore.kt` — DataStore untuk pilihan bahasa
- `ui/AppRoot.kt` — 4 tab: Beranda, Tambah, Budget, Pengaturan
- `ui/HomeViewModel.kt` — state + aksi
- `ui/Strings.kt` — kamus ID/EN + format Rupiah
- `ui/theme/Theme.kt` — Material3 terang/gelap otomatis

## Catatan build
Mesin ini belum ada JDK / Android SDK, jadi kode belum dikompilasi di sini.
Buka di Android Studio lalu Sync + Run.
