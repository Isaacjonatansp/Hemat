# Hemat - Pencatat Keuangan Pribadi

Aplikasi pencatat keuangan pribadi berbasis Android. Seluruh data tersimpan secara lokal di perangkat menggunakan Room Database tanpa koneksi internet.

## Fitur Utama

- Pencatatan Pemasukan dan Pengeluaran: Catat nominal, pilih kategori, dan tambahkan catatan transaksi.
- Scan Struk Belanja (OCR - Masih Dalam Pengembangan): Membaca total belanja langsung dari foto struk menggunakan kamera atau galeri. Foto sementara dihapus otomatis setelah diproses.
- Diagram Batang Kategori: Menampilkan persentase dan total nominal pengeluaran atau pemasukan berdasarkan kategori.
- Filter dan Urutkan Transaksi:
  - Filter transaksi berdasarkan kategori.
  - Urutkan transaksi berdasarkan terbaru, terlama, nominal terbesar, nominal terkecil, dan nama kategori A-Z.
- Pengaturan Budget: Menentukan batas anggaran per kategori lengkap dengan indikator sisa batas dan warna peringatan.
- Dukungan Multi Bahasa: Pilihan bahasa Indonesia dan bahasa Inggris.
- Tampilan Responsif: Form penambahan dan edit catatan menyesuaikan posisi keyboard.

## Cara Menginstall dan Menggunakan Aplikasi

### 1. Cara Menginstall File APK
1. Download file `Hemat.apk` yang ada di repositori ini ke HP Android Anda.
2. Buka file `Hemat.apk` dari pengelola file (File Manager).
3. Jika muncul peringatan keamanan, izinkan install dari sumber tidak dikenal (Unknown Sources).
4. Klik **Install** dan tunggu hingga proses selesai.

### 2. Cara Mencatat Transaksi Manual
1. Buka aplikasi **Hemat**, lalu pilih tab **Tambah** di bagian bawah.
2. Pilih tipe transaksi: **Keluar** (Pengeluaran) atau **Masuk** (Pemasukan).
3. Masukkan nominal angka pada kolom **Nominal**.
4. Pilih salah satu **Kategori** yang tersedia.
5. Isi kolom **Catatan** (opsional).
6. Klik tombol **Simpan**.

### 3. Cara Menggunakan Fitur Scan Struk Belanja (Masih Dalam Pengembangan)
*Catatan: Fitur ini masih dalam tahap pengembangan dan pengujian. Hasil pembacaan nominal dapat bervariasi tergantung pada kejelasan foto struk.*

1. Buka tab **Tambah**.
2. Pada kartu **Scan Struk Belanja**, pilih salah satu metode:
   - **Kamera**: Ambil foto struk belanja secara langsung.
   - **Galeri**: Pilih gambar struk yang sudah ada di HP.
3. Aplikasi akan menganalisis teks pada foto struk secara otomatis.
4. Nominal total belanja dan usulan kategori akan terisi secara otomatis pada form.
5. Periksa kembali data, lalu klik **Simpan**.

### 4. Cara Membaca Diagram Batang Kategori
1. Buka tab **Beranda**.
2. Lihat kartu **Diagram Kategori** di atas daftar transaksi.
3. Pilih tombol **Keluar** atau **Masuk** pada kartu diagram untuk berganti tampilan.
4. Diagram akan menampilkan nama kategori, persentase penggunaan, total nominal, dan garis grafik warna.

### 5. Cara Memfilter dan Mengurutkan Transaksi
1. Buka tab **Beranda**.
2. **Filter Kategori**: Klik salah satu chip kategori di atas daftar transaksi untuk menampilkan transaksi kategori tersebut. Klik **Semua** untuk menampilkan seluruh transaksi.
3. **Mengurutkan Transaksi**: Klik tombol **Urutkan** di sebelah kanan atas daftar transaksi, lalu pilih opsi pengurutan yang diinginkan (Terbaru, Terlama, Nominal Terbesar, Nominal Terkecil, atau A-Z Kategori).

### 6. Cara Mengatur Budget Kategori
1. Buka tab **Budget**.
2. Pilih kategori yang ingin diatur batas anggarannya.
3. Masukkan batas nominal pada kolom **Batas**.
4. Klik **Simpan**.
5. Kartu budget akan menampilkan total terpakai, sisa anggaran, dan indikator warna (merah jika melewati batas).

### 7. Cara Mengubah Bahasa
1. Buka tab **Pengaturan**.
2. Pada bagian **Bahasa**, pilih **Indonesia** atau **English**.

## Struktur Proyek

- `app/src/main/java/com/hemat/app/data/local/`: Entities, DAO, dan HematDatabase (Room)
- `app/src/main/java/com/hemat/app/data/Repository.kt`: Logika query dan agregasi data
- `app/src/main/java/com/hemat/app/ui/AppRoot.kt`: Komponen UI Compose
- `app/src/main/java/com/hemat/app/ui/HomeViewModel.kt`: ViewModel penampung state
- `app/src/main/java/com/hemat/app/ui/Strings.kt`: Kamus bahasa dan format Rupiah
- `app/src/main/java/com/hemat/app/util/ReceiptScanner.kt`: Pemrosesan OCR struk belanja
