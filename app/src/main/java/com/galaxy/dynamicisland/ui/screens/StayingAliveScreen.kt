package com.galaxy.dynamicisland.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galaxy.dynamicisland.ui.theme.DarkSurface
import com.galaxy.dynamicisland.ui.theme.DarkSurfaceVariant
import com.galaxy.dynamicisland.ui.theme.SamsungSuperFastCyan
import com.galaxy.dynamicisland.ui.theme.TextMuted
import com.galaxy.dynamicisland.ui.theme.TextWhite

@Composable
fun StayingAliveScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSurface)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Panduan Anti-Kill Samsung One UI", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Langkah agar Island selalu aktif dan tidak dimatikan oleh sistem Samsung", color = TextMuted, fontSize = 13.sp)

        Spacer(modifier = Modifier.height(18.dp))

        StepCard(
            step = "1. Masukkan ke Aplikasi Tidak Pernah Tidur",
            details = "Buka Pengaturan HP > Perawatan Perangkat (Device Care) > Baterai > Batas Penggunaan Latar Belakang > Aplikasi yang tidak pernah tidur (Never sleeping apps) > Tambahkan Galaxy Island."
        )

        StepCard(
            step = "2. Abaikan Optimasi Baterai",
            details = "Buka Info Aplikasi Galaxy Island > Baterai > Pilih opsi 'Tidak Dibatasi' (Unrestricted)."
        )

        StepCard(
            step = "3. Kunci Aplikasi di Layar Terbuka (Recents)",
            details = "Buka layar aplikasi terakhir (Recents) > Ketuk ikon Galaxy Island di atas pratinjau > Pilih 'Kunci aplikasi ini' (Lock this app) agar tidak terhapus saat Anda klik Tutup Semua."
        )

        StepCard(
            step = "4. Integrasi Shizuku (Opsional)",
            details = "Jika Anda menggunakan Shizuku di S24 Ultra, izin Accessibility dan Overlay dapat diberikan otomatis secara instan tanpa perlu repot buka Pengaturan."
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun StepCard(step: String, details: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(step, color = SamsungSuperFastCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(details, color = TextWhite, fontSize = 12.sp, lineHeight = 18.sp)
        }
    }
}
