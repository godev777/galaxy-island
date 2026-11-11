# 🏝️ Galaxy Island (Samsung Galaxy S24 Ultra Edition)

Dynamic Island canggih & cerdas berbasis **Jetpack Compose + Material 3** yang dirancang khusus memeluk lubang punch-hole kamera **Samsung Galaxy S24 Ultra**, dilengkapi **Dual Island (Companion Bubble)**, integrasi **S-Pen Hover**, dan animasi **Super Fast Charging 2.0 (45W)**.

---

## ✨ Fitur Unggulan

### 1. 🖊️ S24 Ultra Exclusive Powers
* **S-Pen Smart Hover**: Mendeteksi ujung S-Pen saat mendekati lubang kamera tanpa menyentuh layar (*hovering*) untuk menampilkan preview mini.
* **Super Fast Charging 2.0 (45W)**: Deteksi otomatis protokol pengisian daya 45W khas Samsung One UI dengan animasi cincin energi *cyan/teal neon* dan estimasi menit hingga 100%.
* **Deteksi OTP Otomatis**: Memunculkan tombol **"Salin OTP"** 1-tap saat SMS / WhatsApp berisi kode verifikasi masuk.
* **AMOLED Pitch Black (`#000000`) & Pixel Shift**: Piksel di sekitar kamera mati total (0% konsumsi daya layar) dan bergeser halus setiap beberapa menit untuk mencegah *burn-in*.
* **120Hz LTPO Spring Physics**: Animasi halus layaknya fitur bawaan sistem One UI.

### 2. 🫧 Dual Island & Companion Bubble
* Ketika ada **2 aktivitas bersamaan** (contoh: memutar musik di Spotify + pesanan Gojek/Grab atau Timer aktif), Island membelah menjadi dua:
  * **Pill Utama**: Memeluk lubang kamera (cover lagu & equalizer).
  * **Companion Bubble**: Bola kecil terpisah di samping kanan/kiri kamera (timer countdown / ikon ojek).

### 3. 🎵 Media Player & Equalizer
* Batang equalizer audio bergerak real-time mengikuti alunan musik.
* Wavy seek bar dan kontrol musik (Play/Pause, Previous, Next).

### 4. 📐 Kalibrasi Mandiri (Manual Calibration)
* Slider geser posisi X dan Y, lebar, tinggi, dan sudut kelengkungan (*corner radius*).
* Posisi Companion Bubble dapat dipilih di **Kanan** atau **Kiri** kamera.
* Tombol 1-tap **Reset ke Preset Galaxy S24 Ultra**.

---

## 🚀 Cara Build Otomatis di CI GitHub (Tanpa Perlu Pasang Android Studio di Laptop)

Project ini telah dilengkapi **GitHub Actions CI Pipeline** (`.github/workflows/build-apk.yml`) yang akan mengompilasi APK secara otomatis di cloud GitHub:

### Langkah 1: Buat Repository Baru di GitHub
1. Buka [github.com/new](https://github.com/new).
2. Beri nama repository, misalnya: `galaxy-island-samsung`.
3. Pilih **Public** (gratis tanpa batas) atau **Private**.
4. Klik **Create repository** (jangan centang Add README atau gitignore karena sudah ada di folder ini).

### Langkah 2: Hubungkan & Push dari Laptop Anda
Buka terminal / PowerShell di folder ini, lalu jalankan:

```powershell
git init
git add .
git commit -m "feat: Galaxy Island for Samsung S24 Ultra with Dual Island & 45W Charging"
git branch -M main
git remote add origin https://github.com/USERNAME_ANDA/galaxy-island-samsung.git
git push -u origin main
```
*(Ganti `USERNAME_ANDA` dengan username GitHub Anda)*

### Langkah 3: Unduh APK Hasil Build di HP S24 Ultra
1. Buka repository Anda di browser HP S24 Ultra.
2. Buka tab **Actions**.
3. Klik proses build terbaru (**Build Android APK**).
4. Gulir ke bagian **Artifacts** di bagian bawah halaman.
5. Unduh file **`DynamicIsland-Samsung-Release.apk`** atau **`DynamicIsland-Samsung-Debug.apk`**.
6. Pasang (*Install*) langsung di Samsung Galaxy S24 Ultra Anda!

---

## 🛡️ Panduan Agar Tidak Dimatikan Samsung One UI (Staying Alive)

1. **Aplikasi Tidak Pernah Tidur**:
   * Masuk ke *Pengaturan HP > Perawatan Perangkat (Device Care) > Baterai > Batas Penggunaan Latar Belakang > Aplikasi yang tidak pernah tidur > Tambah Galaxy Island*.
2. **Optimasi Baterai**:
   * Tekan tahan ikon *Galaxy Island > Info Aplikasi > Baterai > Pilih 'Tidak Dibatasi' (Unrestricted)*.
3. **Kunci di Recents**:
   * Buka layar aplikasi terakhir (*Recents screen*) > Ketuk ikon Galaxy Island di atas pratinjau > Pilih *Kunci aplikasi ini (Lock this app)*.
