# SOP Pengembangan Sistem — SIMRS Khanza

**Versi**: 1.0  
**Tanggal**: 2026-08-30  
**Berlaku untuk**: IT Programmer (Junior)  

---

## Daftar Isi

1. [Tujuan](#1-tujuan)
2. [Alur Kerja Pengembangan](#2-alur-kerja-pengembangan)
3. [Langkah 1 — Terima & Pahami Permintaan](#3-langkah-1--terima--pahami-permintaan)
4. [Langkah 2 — Analisa & Rencana](#4-langkah-2--analisa--rencana)
5. [Langkah 3 — Development (Coding)](#5-langkah-3--development-coding)
6. [Langkah 4 — Testing di Lokal](#6-langkah-4--testing-di-lokal)
7. [Langkah 5 — Deploy ke Staging/Production](#7-langkah-5--deploy-ke-stagingproduction)
8. [Langkah 6 — Dokumentasi](#8-langkah-6--dokumentasi)
9. [Aturan Wajib](#9-aturan-wajib)
10. [Checklist Harian](#10-checklist-harian)

---

## 1. Tujuan

SOP ini dibuat agar proses pengembangan fitur atau perbaikan sistem berjalan **rapi, aman, dan terdokumentasi** — terutama karena SIMRS Khanza adalah sistem produksi yang dipakai nyata di rumah sakit.

---

## 2. Alur Kerja Pengembangan

```
Permintaan masuk
      ↓
Pahami & catat kebutuhan
      ↓
Analisa dampak ke modul lain
      ↓
Buat rencana (file + query + perubahan)
      ↓
[Persetujuan jika ada perubahan DB]
      ↓
Development di lokal
      ↓
Testing di DB lokal
      ↓
Build & verifikasi
      ↓
Deploy ke staging → Production
      ↓
Dokumentasi di folder Pengembangan/
```

---

## 3. Langkah 1 — Terima & Pahami Permintaan

### Yang harus dilakukan:
- [ ] Catat **apa yang diminta** secara spesifik (fitur baru / perbaikan bug / perubahan tampilan)
- [ ] Tanya jika ada yang tidak jelas — jangan langsung asumsi
- [ ] Identifikasi **modul mana** yang terlibat (`rekammedis`, `keuangan`, `inventory`, dll)
- [ ] Cek apakah ada **ketergantungan antar modul** (misal: billing bergantung pada status klinis)

### Contoh pertanyaan yang perlu dijawab sebelum mulai:
- Modul/form mana yang berubah?
- Apakah ada perubahan di database (tabel/kolom)?
- Siapa yang akan memakai fitur ini?
- Ada deadline atau tidak?

---

## 4. Langkah 2 — Analisa & Rencana

### Yang harus dilakukan:
- [ ] Buka file yang relevan — **gunakan search/grep**, jangan baca seluruh file besar
- [ ] Identifikasi fungsi/method yang akan diubah atau ditambah
- [ ] Cek tabel database yang terlibat — buka `sik.sql` atau `struktur.sql` jika perlu
- [ ] Catat file-file yang akan berubah beserta alasannya
- [ ] Estimasi dampak ke modul lain

### Jika ada perubahan database:
> ⚠️ **WAJIB minta persetujuan** sebelum menjalankan `ALTER TABLE` atau membuat tabel baru.  
> Catat query SQL-nya terlebih dahulu, baru eksekusi setelah disetujui.

---

## 5. Langkah 3 — Development (Coding)

### Konvensi wajib diikuti:

| Hal | Aturan |
|-----|--------|
| Akses database | Gunakan `fungsi.sekuel` atau JDBC langsung — **bukan DAO/Repository** |
| Validasi input | Lewatkan `fungsi.validasi` |
| Form GUI | Edit via **NetBeans Design tab** — jangan edit manual blok `// <editor-fold>` |
| Penamaan file | `DlgXxx.java` = dialog, `MasterXxx.java` = form master, `ApiXxx.java` = koneksi API |
| Encoding | UTF-8 |

### Tips praktis:
- Tulis komentar singkat di bagian kode yang kamu tambah/ubah
- Jangan ubah kode yang tidak diminta — tetap dalam **scope yang ditentukan**
- Jangan refactor masal — fokus ke yang diminta saja
- Jika ragu dengan logika yang ada, tanya dulu sebelum mengubah

---

## 6. Langkah 4 — Testing di Lokal

> ⚠️ **Selalu test di database lokal dulu — jangan langsung di server production.**

### Checklist testing:

- [ ] Fungsi baru berjalan sesuai harapan
- [ ] Validasi input bekerja (input kosong, format salah, dll)
- [ ] Tidak ada error di console/log
- [ ] Modul lain yang terhubung masih berjalan normal
- [ ] Data tersimpan/terbaca dengan benar dari database
- [ ] Jika ada bridging (BPJS/SatuSehat) — **test ke endpoint Dev/Staging**, bukan Production

### Cara build & jalankan:
```bash
# Di NetBeans: klik kanan project → Clean and Build
# Atau via CLI:
java -jar dist/SIMRSKhanza-2026.jar
```

---

## 7. Langkah 5 — Deploy ke Staging/Production

### Urutan deploy:
1. **Build ulang** project (`Clean and Build`)
2. **Backup database** production sebelum deploy (jika ada perubahan DB)
3. Jalankan script SQL perubahan di server staging dulu
4. Test ulang di staging
5. Jika aman → deploy ke production
6. Konfirmasi ke user/supervisor bahwa deploy berhasil

### Yang TIDAK boleh dilakukan saat deploy:
- Menjalankan `ALTER TABLE` langsung tanpa backup
- Deploy di jam sibuk (jam pelayanan RS)
- Deploy tanpa memberitahu user/admin RS terlebih dahulu

---

## 8. Langkah 6 — Dokumentasi

> Setiap ada perubahan **wajib** dibuat file dokumentasi di folder `Pengembangan/`.

### Format nama file:
```
Pengembangan/YYYY-MM-DD_nama-fitur-singkat.md
```

**Contoh**: `Pengembangan/2026-08-30_tambah-kolom-diagnosa-igd.md`

### Template isi file:

```markdown
# [Nama Fitur/Perubahan]
Tanggal: YYYY-MM-DD
Status: [Selesai / Dalam Pengerjaan]

## Tujuan
[kenapa perubahan ini dibuat]

## File yang Diubah/Ditambah
- `path/file1.java` — [apa yang diubah]
- `path/file2.form` — [apa yang diubah]

## Perubahan Database (jika ada)
- Tabel: [nama tabel]
- Perubahan: ALTER TABLE / kolom baru / tabel baru
- Query:
  ALTER TABLE nama_tabel ADD COLUMN ...;

## Catatan/Risiko
[hal yang perlu diperhatikan ke depannya]
```

---

## 9. Aturan Wajib

| # | Aturan |
|---|--------|
| 1 | **Jangan ubah** blok `// <editor-fold>` — auto-generated NetBeans |
| 2 | **Konfirmasi dulu** sebelum menyentuh `frmUtama.java` — dampaknya sistemik |
| 3 | **Selalu gunakan endpoint Dev/Staging** untuk testing modul bridging BPJS/SatuSehat |
| 4 | **Tanya persetujuan** sebelum menjalankan perubahan database |
| 5 | **Backup selalu** sebelum deploy ke production |
| 6 | **Dokumentasi wajib** dibuat di folder `Pengembangan/` setelah selesai |
| 7 | **Test di lokal dulu** sebelum ke staging/production |
| 8 | **Scope ketat** — jangan ubah kode yang tidak diminta |

---

## 10. Checklist Harian

Gunakan ini sebelum mulai kerja dan sebelum selesai:

### Sebelum mulai kerja:
- [ ] Cek apakah ada task/permintaan yang pending dari hari sebelumnya
- [ ] Pastikan database lokal berjalan normal
- [ ] Buka project di NetBeans — pastikan tidak ada error compile

### Sebelum selesai kerja:
- [ ] Semua perubahan sudah di-build dan di-test
- [ ] Dokumentasi di `Pengembangan/` sudah dibuat
- [ ] Tidak ada file yang tertinggal belum disimpan
- [ ] Jika ada yang belum selesai — catat di file dokumentasi dengan status "Dalam Pengerjaan"

---

> **Ingat**: SIMRS Khanza dipakai di rumah sakit nyata. Setiap bug bisa berdampak ke pelayanan pasien.  
> Kalau ragu, **tanya dulu** — lebih baik lambat tapi benar daripada cepat tapi rusak.
