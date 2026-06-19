# 🏥 SIMRS Khanza - Local Development

## 📖 Deskripsi Aplikasi
SIMRS Khanza adalah Sistem Informasi Manajemen Rumah Sakit yang sangat komprehensif berbasis Java (Desktop/Swing). Repository ini merupakan versi *clone* lokal untuk kebutuhan *customization* (pengembangan kostumisasi pribadi) pada tahun 2026. Aplikasi ini mencakup seluruh operasional rumah sakit mulai dari pendaftaran, pelayanan medis, apotek, kasir, hingga bridging dengan layanan eksternal (seperti BPJS dan SatuSehat).

## ✨ Fitur Utama
- **Rekam Medis Terintegrasi**: Manajemen rekam medis pasien lengkap, dari rawat jalan hingga rawat inap.
- **Bridging BPJS & Kemenkes**: Integrasi langsung dengan API BPJS (VClaim, PCare, Antrian) dan SatuSehat.
- **Manajemen Apotek & Inventori**: Mengelola stok obat, alur resep, hingga inventaris non-medis.
- **Billing & Keuangan**: Transparansi tagihan pasien, integrasi kasir, hingga laporan akuntansi.
- **Modul Penunjang**: Mencakup laboratorium, radiologi, bank darah, gizi/dapur, dan loundry/IPSRS.
- **Portal Pasien & Dokter**: Terdapat modul web pendamping seperti `epasien` dan `edokter`.
- **E-Sign & Paperless**: Mendukung fitur tanda tangan elektronik.

## 🛠️ Teknologi & Library
**Teknologi Inti:**
- **Bahasa**: Java 8 (Standard Edition)
- **UI Framework**: Java Swing (dengan FlatLaf Look & Feel) & JavaFX
- **Database**: MySQL / MariaDB
- **Build System**: Apache Ant (via NetBeans IDE)

**Library Pendukung Utama:**
- **Reporting**: JasperReports & iReport
- **ORM & Data**: Hibernate 3, JPA, Apache Commons DBCP
- **Web & API**: Spring Framework, Apache HttpClient, Jackson, Gson
- **PDF & Dokumen**: iText, PDFBox, Apache POI (Excel/Word)
- **Komponen Ekstra**: JFreeChart (Grafik), Batik (SVG), Barbecue/Barcode4j, SMSLib

## 🧩 Modul Tersedia
Sistem ini dibangun secara modular di dalam package `src/`. Modul-modul utamanya meliputi:
- `simrskhanza` (Modul Utama)
- `rekammedis` (Manajemen Rekam Medis)
- `keuangan` (Kasir & Billing)
- `kepegawaian` (SDM & Presensi)
- `inventory` & `inventaris` (Manajemen Aset & Obat)
- `bridging` (Integrasi Pihak Ketiga)
- `laporan` & `grafikanalisa` (Reporting & Dashboard)
- Dan puluhan modul spesifik lainnya seperti `dapur`, `parkir`, `perpustakaan`, `tranfusidarah`, `toko`, dll.

## ⚙️ Cara Instalasi & Konfigurasi Database
1. **Persiapan Tools:**
   - Install Java JDK (rekomendasi Java 8).
   - Install NetBeans IDE (versi yang mensupport Apache Ant).
   - Install XAMPP / MariaDB Server.
2. **Setup Database:**
   - Buat database baru di MySQL (contoh: `sik`).
   - Import struktur database utama melalui file `sik.sql` yang ada di root project.
   - (Opsional) Import file sql bridging jika diperlukan, seperti `sik_bridging_lab.sql` atau `sik_bridging_radiologi.sql`.
3. **Konfigurasi Koneksi:**
   - Buka project di NetBeans.
   - Atur koneksi database (URL, username, password) pada pengaturan konfigurasi koneksi (`src/setting/database.xml` atau langsung pada konfigurasi class koneksi).
4. **Build & Run:**
   - Lakukan `Clean and Build` melalui NetBeans untuk mendownload/menyiapkan dependencies dan men-compile file `.class`.
   - Jalankan `Main Class` pada `simrskhanza.SIMRSKhanza`.

## 📂 Struktur Folder
```text
SIMRS-Khanza/
├── .git/                  # Git repository data
├── build/                 # Hasil kompilasi ant build
├── dist/                  # File JAR hasil build
├── lib/                   # Kumpulan library JAR pendukung
├── nbproject/             # Konfigurasi project NetBeans (build.xml, project.properties)
├── src/                   # Source code Java (.java, .form, dll)
│   ├── bridging/          # Modul bridging API
│   ├── keuangan/          # Modul akuntansi & kasir
│   ├── rekammedis/        # Modul rekam medis
│   ├── simrskhanza/       # Main entry aplikasi
│   └── ... (modul lainnya)
├── webapps/               # Aplikasi web pendukung (PHP/HTML)
├── epasien/, edokter/     # Web Apps Portal Pasien & Dokter
├── api-bpjs.../           # Berbagai script integrasi API
├── report/                # File-file report JRXML Jasper
├── sik.sql                # Dump file struktur database utama
└── README.md              # Dokumentasi ini
```

## 📝 Changelog
*(Kosong, akan diisi secara manual sesuai progress pengembangan)*

## 💡 Catatan Pengembangan Pribadi
- **Tujuan Clone**: Pengembangan fitur custom untuk kebutuhan spesifik rumah sakit pada tahun 2026.
- **Fokus Area**: *(Silakan isi dengan modul/fitur yang sedang dikembangkan)*
- **Catatan Tambahan**: Pastikan selalu mem-backup database sebelum mengeksekusi `ALTER TABLE` saat menambah atau memodifikasi fitur baru.

---
*Dibuat khusus untuk mempermudah dokumentasi lokal pengembangan SIMRS Khanza.*
