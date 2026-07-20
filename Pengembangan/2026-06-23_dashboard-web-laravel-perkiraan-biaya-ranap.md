# Dashboard Web — Perkiraan Biaya Rawat Inap (Laravel)
Tanggal: 2026-06-23
Status: Selesai

## Tujuan
Membuat versi web dari `DlgPerkiraanBiayaRanap.java` sebagai dashboard monitoring yang bisa diakses lewat browser. Terhubung langsung ke database `sik` SIMRS Khanza yang sama dengan aplikasi desktop.

## Lokasi Project
`/home/ratra/Workspace/web/simrs-ranap-web/`

## Stack
- **Framework**: Laravel (PHP 8.5)
- **Database**: MySQL `sik` (shared dengan SIMRS Khanza desktop)
- **Frontend**: Blade + Tailwind CSS v4 via Vite
- **Dev server**: `php artisan serve --port=8001`

## Menjalankan
```bash
cd /home/ratra/Workspace/web/simrs-ranap-web
php artisan serve --port=8001 --host=0.0.0.0
# Buka: http://localhost:8001
```

## File yang Dibuat/Ditambah

### Backend
- `app/Services/PerkiraanBiayaService.php` — mirror logika `tampil()` Java: hitung 12 komponen biaya per pasien, perkiraan tarif dari `inacbg_dummy`/`perkiraan_biaya_ranap`, status limit
- `app/Http/Controllers/DashboardController.php` — render halaman + JSON endpoint auto-refresh
- `app/Http/Controllers/InacbgController.php` — CRUD tarif `inacbg_dummy`
- `routes/web.php` — 6 routes: dashboard, API JSON, CRUD inacbg

### Frontend
- `resources/views/layouts/app.blade.php` — sidebar dark mode, header jam real-time, modal detail pasien
- `resources/views/dashboard/index.blade.php` — 4 summary cards, filter bar, tabel pasien, auto-refresh 60 detik
- `resources/views/inacbg/index.blade.php` — tabel tarif INA-CBG dengan inline edit klik langsung + hapus
- `resources/css/app.css` — Tailwind v4 import + custom dark theme variables
- `resources/js/app.js` — helper format Rupiah
- `vite.config.js` — konfigurasi Tailwind + Laravel Vite plugin

### Config
- `.env` — DB_DATABASE=sik, SESSION_DRIVER=file

## Perubahan Database
Tidak ada perubahan schema. Read-only ke DB `sik` yang sudah ada.
Tabel yang diakses (SELECT):
- `kamar_inap`, `reg_periksa`, `pasien`, `kamar`, `bangsal`
- `periksa_lab`, `detail_periksa_lab`, `periksa_radiologi`
- `operasi`, `detail_pemberian_obat`, `tagihan_obat_langsung`, `beri_obat_operasi`
- `rawat_inap_dr`, `rawat_inap_drpr`, `rawat_inap_pr`, `rawat_jl_dr`, `rawat_jl_drpr`, `rawat_jl_pr`
- `tambahan_biaya`, `pengurangan_biaya`, `kamar_inap` (ttl_biaya), `biaya_sekali`, `biaya_harian`
- `detreturjual`, `resep_pulang`, `deposit`, `ranap_gabung`, `rujuk_masuk`
- `inacbg_dummy`, `penyakit`, `perkiraan_biaya_ranap`

Halaman INA-CBG melakukan INSERT/UPDATE/DELETE pada tabel `inacbg_dummy`.

## Fitur
1. **Dashboard Utama**: Summary cards (total pasien, total biaya, deposit, status tidak aman) + tabel pasien aktif dengan semua 15 kolom biaya + badge Aman/Tidak Aman
2. **Detail Modal**: Klik baris → modal popup breakdown semua komponen biaya per pasien
3. **Filter**: Cari nama/no rawat/no RM/kamar + dropdown bangsal
4. **Auto-refresh**: Countdown bar 60 detik, refresh data via AJAX tanpa reload page
5. **Manajemen INA-CBG**: Tambah/edit inline/hapus tarif dummy per kode ICD-10

## Catatan/Risiko
- DB `sik` diakses dengan user `root` — disarankan buat user khusus read-only untuk production
- Server saat ini pakai `php artisan serve` untuk development. Untuk production, konfigurasi Nginx
- Halaman dashboard bisa berat jika pasien aktif banyak (banyak sub-query per pasien) — pertimbangkan caching atau query JOIN tunggal di masa depan
