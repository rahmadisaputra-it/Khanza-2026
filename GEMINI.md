# GEMINI.md — Panduan Pengembangan SIMRS Khanza

> Dokumen ini adalah konteks utama untuk AI Agent yang bekerja di project ini.  
> Baca seluruh bagian ini sebelum memberikan saran atau melakukan perubahan kode.

---

## Deskripsi Singkat Project & Tujuannya

SIMRS Khanza adalah Sistem Informasi Manajemen Rumah Sakit (SIMRS) **open-source berbasis desktop Java**. Mencakup seluruh operasional RS: pendaftaran pasien, pelayanan klinis (IGD, Rawat Jalan, Rawat Inap), farmasi/apotek, kasir/billing, inventory, pelaporan, hingga integrasi wajib dengan sistem pemerintah (BPJS, SatuSehat).

---

## Tech Stack Lengkap

| Komponen | Teknologi |
|---|---|
| **Bahasa** | Java SE 8 |
| **UI Framework** | Java Swing + GUI Builder NetBeans (Matisse), FlatLaf |
| **Database** | MySQL / MariaDB (schema: `sik`) |
| **Reporting** | JasperReports 5.x & 6.x + iReport |
| **Database Access** | JDBC native (tanpa ORM; utility via `sekuel.java`) |
| **HTTP / API** | Apache HttpClient, Jackson, Gson |
| **Build** | Apache Ant (`build.xml`) |
| **Keamanan** | AES-128 internal (`KhanzaSecurity16bit`) |
| **Web Companion** | PHP (portal pasien, dokter, antrian display) |

---

## Arsitektur Aplikasi

Model **2-Tier Thick Client** (desktop ↔ database langsung):

```
[User] → [GUI Form .java/.form]
              ↓ validasi input
         [fungsi/validasi.java]
              ↓ eksekusi query
         [fungsi/sekuel.java]
              ↓ koneksi
         [fungsi/koneksiDB.java]
              ↓ JDBC
         [MySQL — schema: sik]
```

Untuk modul bridging, data `ResultSet` dikonversi ke JSON (Jackson/Gson) lalu dikirim via Apache HttpClient ke API eksternal (BPJS/SatuSehat).

---

## Modul-Modul Utama dan Fungsinya

| Modul (`src/`) | File Kunci | Fungsi |
|---|---|---|
| `simrskhanza` | `SIMRSKhanza.java`, `frmUtama.java` | Entry point, menu MDI, pendaftaran, master data |
| `fungsi` | `koneksiDB.java`, `sekuel.java`, `validasi.java` | Core utilities: DB, query, validasi, format, akses |
| `rekammedis` | `DlgSOAP*.java`, `Master*.java` | EMR, SOAP, asesmen awal medis & keperawatan |
| `keuangan` | `DlgBiling*.java`, `DlgAkunPiutang.java` | Billing, kasir, piutang, akuntansi |
| `bridging` | `ApiBPJS.java`, `ApiSatuSehat.java`, `ApiPcare.java` | Integrasi BPJS VClaim, SatuSehat, PCare, INA-CBG |
| `inventory` | *(lihat struktur.md)* | Farmasi, stok obat, penerimaan barang |
| `inventaris` | *(lihat struktur.md)* | Inventaris aset non-medis |
| `kepegawaian` | *(lihat struktur.md)* | SDM, jadwal, penggajian |
| `laporan` | *(lihat struktur.md)* | Laporan RL, statistik, rekap |
| `grafikanalisa` | *(lihat struktur.md)* | Grafik & analisa data RS |

> **Referensi lengkap file per modul → lihat [`struktur.md`](./struktur.md)**

---

## Cara Menjalankan Project di Local (Setup & Development)

1. **Persiapan tools**: JDK 8, Apache NetBeans (v11–v21), MySQL/MariaDB
2. **Setup database**:
   ```sql
   CREATE DATABASE sik;
   -- Lalu import:
   mysql -u root -p sik < sik.sql
   ```
3. **Konfigurasi koneksi**: Edit `setting/database.xml`.  
   ⚠️ Nilai host/user/password di-**AES-encrypt**. Gunakan tool `KhanzaPengenkripsiTeks/` untuk mengenkripsi nilai baru.
4. **Build & Run di NetBeans**:
   - Klik kanan project → `Clean and Build`
   - Jalankan `src/simrskhanza/SIMRSKhanza.java` (Run Project / F6)
5. **Alternatif CLI**:
   ```bash
   java -jar dist/SIMRSKhanza-2026.jar
   ```

---

## Konvensi Koding (Codebase Convention)

- **Monolithic Fat Client** — UI logic, business rule, dan event handling **disatukan dalam satu file** `.java` per form.
- **Tanpa DAO** — Akses DB via method statis di `fungsi.sekuel` atau native SQL string langsung di event listener. Tidak ada abstraction layer DAO/Repository.
- **Validasi terpusat** — Semua format input, angka, tanggal, dan combo filler melewati `fungsi.validasi`.
- **GUI via Builder** — Komponen visual **harus diedit via tab Design NetBeans** (file `.form`). Jangan edit blok `// <editor-fold>` secara manual — akan tertimpa saat regenerate.
- **Naming**: `DlgXxx.java` = dialog form, `MasterXxx.java` = form master data, `ApiXxx.java` = koneksi API.

---

## Area yang Perlu Perhatian

| Area | Risiko / Catatan |
|---|---|
| `frmUtama.java` (~2.4 MB) | Semua menu terpusat di sini. Edit apapun di sini berdampak sistemik. Compile-time lebih lama. **Wajib konfirmasi sebelum menyentuh.** |
| SQL statis tersebar | ALTER TABLE wajib diikuti search & replace manual di semua `.java` yang memakai tabel tersebut. |
| AES Bridging keys | `X-cons-id`, `X-Signature`, secret key tersimpan di `database.xml` (encrypted). Gunakan `KhanzaPengenkripsiTeks` untuk update. |
| GUI Builder blocks | Blok `// <editor-fold>` auto-generated. Edit komponen visual **hanya via NetBeans Design tab**. |
| Skalabilitas | Pola native SQL statis menyulitkan maintenance jangka panjang — tidak ada unit test, tidak ada abstraction layer. |

---

## Catatan Khusus Konteks Rumah Sakit

- **EMR siap akreditasi SNARS/KARS** — Formulir asesmen awal sudah spesifik per spesialisasi (Jantung, Neonatus, Psikiatri, Hemodialisa, dll).
- **Workflow klinis saling terkunci** — `resep_obat` tergantung `reg_periksa`, billing tidak bisa ditutup jika status klinis belum selesai. Selalu testing **end-to-end**, bukan satu form saja.
- **Bridging wajib di RS Indonesia** — BPJS & SatuSehat mandatory. Pastikan testing selalu ke endpoint **Dev/Staging**, bukan Production.
- **Multi-fasyankes** — Kode mendukung konfigurasi untuk poli spesifik, kelas kamar, cara bayar (BPJS/umum/asuransi swasta/Inhealth).

---

## Peta Navigasi Codebase

| Domain | Lokasi | File / Keterangan Penting |
|---|---|---|
| **Entry Point** | `src/simrskhanza/` | `SIMRSKhanza.java` (main), `frmUtama.java` (menu MDI) |
| **Konfigurasi** | `setting/` | `database.xml` (koneksi DB, AES encrypted) |
| **Core Utilities** | `src/fungsi/` | `koneksiDB.java`, `sekuel.java`, `validasi.java`, `akses.java` |
| **Rekam Medis** | `src/rekammedis/` | EMR, SOAP, asesmen awal medis & keperawatan (240 file) |
| **Keuangan** | `src/keuangan/` | Billing, kasir, piutang, akuntansi (167 file) |
| **Bridging** | `src/bridging/` | BPJS, SatuSehat, PCare, INA-CBG (261 file) |
| **Inventory** | `src/inventory/` | Farmasi, stok, penerimaan (118 file) |
| **Laporan** | `src/laporan/` | Laporan RL, rekap statistik (108 file) |
| **Reporting Template** | `report/` | File `.jrxml` & `.jasper` JasperReports |
| **Database DDL** | `sik.sql` / `struktur.sql` | Full dump / DDL-only schema |
| **Web Companion** | `webapps/` | PHP: antrian, display, billing web |

> **Detail lengkap per file di setiap modul → [`struktur.md`](./struktur.md)**

---

## Konteks Pengembang

- **Role**: IT Programmer rumah sakit
- **OS**: Ubuntu, Nginx, PHP 8.3 port 8000, MySQL/MariaDB
- **Tujuan**: Pengembangan & kustomisasi SIMRS Khanza untuk kebutuhan RS

---

## Aturan Wajib untuk Agent

1. **Jangan ubah** blok auto-generated NetBeans GUI Builder (`// <editor-fold>`)
2. **Konfirmasi dulu** sebelum menyentuh `frmUtama.java` — dampaknya sistemik
3. **Selalu gunakan endpoint Dev/Staging** untuk semua testing modul bridging (BPJS/SatuSehat)
4. **Setiap ubah query SQL** — cek dulu form/file lain yang memanggil tabel yang sama
5. **Jangan mulai coding** sebelum rencana/plan disetujui developer
6. **Cek [`struktur.md`](./struktur.md)** saat perlu lokasi file atau modul tertentu — jangan baca ulang seluruh direktori
