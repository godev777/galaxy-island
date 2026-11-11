# 🏝️ Galaxy Island (Samsung Galaxy S24 Ultra Edition)

[![Build Android APK](https://github.com/godev777/galaxy-island/actions/workflows/build-apk.yml/badge.svg)](https://github.com/godev777/galaxy-island/actions/workflows/build-apk.yml)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0+-green.svg)](https://android.com)
[![Optimized for](https://img.shields.io/badge/Optimized%20for-Samsung%20Galaxy%20S24%20Ultra-00F0FF.svg)](https://samsung.com)
[![Language](https://img.shields.io/badge/Language-Kotlin%20%2F%20Jetpack%20Compose-purple.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

An intelligent, buttery-smooth **Dynamic Island** built with **Jetpack Compose & Material 3**, tailored specifically for the center punch-hole camera cutout of the **Samsung Galaxy S24 Ultra** and modern Samsung One UI devices.

Includes **Dual Island (Companion Bubble)**, **S-Pen Smart Hover**, **Super Fast Charging 2.0 (45W) Visualizer**, **One-Tap OTP Extraction**, and **AMOLED Pitch-Black Anti-Burn-In Protection**.

---

## 🌟 Key Highlights

### 1. 🖊️ Samsung Galaxy S24 Ultra Exclusive Powers
* **S-Pen Smart Hover & Actions**: Hover the S-Pen tip over the camera cutout without touching the glass to trigger a peek preview. Click the S-Pen button to play/pause media or toggle the island.
* **Super Fast Charging 2.0 (45W)**: Real-time detection of Samsung's 45W charging protocol with an animated neon cyan energy ring, live wattage readout, and minutes remaining until 100%.
* **Automatic OTP Detection & 1-Tap Copy**: Intercepts SMS and WhatsApp verification codes and immediately presents a **"Copy OTP: [Code]"** button right in the island.
* **Pure AMOLED Black (`#000000`) & Pixel Shift**: Pixels around the camera cutout turn completely off (0% battery drain on the LTPO panel). Subtle periodic pixel shifting prevents burn-in during long idle sessions.
* **120Hz LTPO Spring Physics**: Super-responsive, bouncy animations matching native Samsung One UI transitions.

### 2. 🫧 Dual Island & Companion Bubble
When **two activities run at the same time** (e.g., Spotify playing music + an active countdown timer or food delivery ride):
* **Main Pill**: Hugs the camera cutout (rotating album art & dancing equalizer bars).
* **Companion Bubble**: A small detached circular bubble floating to the right or left of the cutout (showing remaining time or driver status).
* Tap the bubble to swap positions or expand full details!

### 3. 🎵 Media Player with Live Equalizer
* Dynamic 3-band audio waveform visualizer moving to the rhythm of your music (Spotify, YouTube Music, Apple Music).
* Wavy seek bar, previous/play/pause/next controls.

### 4. 💬 Intelligent Notifications & Inline Reply
* Rich expandable notification cards with app icon and message preview.
* **Inline Reply with Draft Memory**: Type your reply directly inside the island card without losing half-typed drafts if interrupted by an incoming phone call.
* Step-by-step image expansion from thumbnail to full size.

### 5. 📐 Manual Position Calibration
* Real-time sliders for X offset, Y offset (top margin), Width, Height, and Corner Radius.
* Set Companion Bubble to either the **Right** or **Left** side of the camera, with custom gap spacing.
* 1-Tap **"Reset to Galaxy S24 Ultra Preset"** button.

### 6. 🌐 Dual Language Support (English / Bahasa Indonesia)
* Switch languages easily inside the application or follow your system locale.

---

## 📥 Download Ready-to-Install APK

You can download the pre-compiled APK directly from the **GitHub Actions CI Artifacts**:

1. Go to the [**Actions Tab**](https://github.com/godev777/galaxy-island/actions).
2. Click on the latest workflow run: **Build Dynamic Island for Samsung (APK)**.
3. Scroll down to the **Artifacts** section at the bottom.
4. Download **`DynamicIsland-Samsung-Release.apk`** (Recommended) or **`DynamicIsland-Samsung-Debug.apk`**.
5. Install directly on your Samsung Galaxy device!

---

## 🛡️ Samsung One UI "Staying Alive" Guide

To prevent Samsung One UI's aggressive background cleaner from killing the overlay service:

1. **Add to "Never Sleeping Apps"**:
   * *Settings > Device Care > Battery > Background usage limits > Never sleeping apps > Add Galaxy Island*.
2. **Disable Battery Optimization**:
   * *Long-press app icon > App info > Battery > Select 'Unrestricted'*.
3. **Lock in Recents**:
   * *Open recent apps screen > Tap the Galaxy Island icon at the top of the card > Tap 'Lock this app'*.

---

<details>
<summary>🇮🇩 <b>Petunjuk dalam Bahasa Indonesia (Klik untuk membuka)</b></summary>

### Ringkasan Fitur:
* **Eksklusif S24 Ultra**: S-Pen Hover, Cas Cepat 45W (Super Fast Charging 2.0), Deteksi OTP otomatis 1-tap copy, Layar Hitam Pekat AMOLED, dan Anti-Burn In.
* **Dual Island (Companion Bubble)**: Bola kecil di samping kamera saat ada 2 aktivitas berjalan sekaligus (misal Spotify + Timer).
* **Kalibrasi Manual**: Slider X, Y, Lebar, Tinggi, dan Corner Radius agar memeluk lubang kamera secara presisi.

### Cara Pasang APK:
1. Buka tab [**Actions**](https://github.com/godev777/galaxy-island/actions).
2. Pilih build terbaru yang bercentang hijau.
3. Unduh **DynamicIsland-Samsung-Release.apk** di bagian Artifacts.
4. Pasang di Samsung Galaxy S24 Ultra Anda!
</details>

---

## 📄 License
This project is open-source under the [MIT License](LICENSE).
