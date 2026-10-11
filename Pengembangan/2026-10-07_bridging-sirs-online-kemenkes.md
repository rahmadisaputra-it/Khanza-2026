# Bridging SIRS Online Kemenkes (Mapping & Update Ketersediaan Kamar)
Tanggal: 2026-10-07
Status: Selesai

## Tujuan
Menambahkan modul integrasi bridging ketersediaan tempat tidur (ranap) ke SIRS Online Kemenkes versi REST API JSON, mencakup fitur referensi kamar fasyankes, pemetaan (mapping) bangsal/kamar SIMRS dengan Tempat Tidur SIRS, serta pengiriman status ketersediaan tempat tidur ke server Kemenkes.

## File yang Diubah/Ditambah
- `src/bridging/AplicareCekReferensiKamarsirs.java` & `.form` — Pencarian referensi tempat tidur SIRS fasyankes via GET API.
- `src/bridging/SIRSMapingKamar.java` & `.form` — Form GUI pemetaan bangsal SIMRS Khanza ke ID TT SIRS Kemenkes, dibersihkan dari dependensi yang tidak terpakai dan bug sisa loop pada tombol Simpan.
- `src/bridging/SIRSUpdateKamar.java` & `.form` — Form sinkronisasi status kamar ke SIRS Kemenkes via PUT payload JSON, diperbaiki query method `tampil()` agar aman dari crash index parameter dan mendukung pencarian data secara dinamis.
- `src/simrskhanza/frmUtama.java` — Penambahan tombol menu "Mapping Kamar SIRS Online" dan "Update Kamar SIRS Online" pada sub-menu Bridging Kemenkes / SIRANAP secara aman di luar `initComponents()`.
- `setting/database.xml` — Pengalihan target database ke `sik_fix`.

## Perubahan Database (jika ada)
- Database: `sik_fix`
- Tabel Baru:
  1. `sirs_mapping_kamar` (`kd_bangsal`, `id_t_tt`, `tt_kelas`, `nm_ruang`)
  2. `sirs_update_kamar` (`kd_bangsal`, `message`, `updated_at`)
- Disetujui oleh: User (dieksekusi langsung oleh user ke database `sik_fix`)

## Catatan/Risiko
- Modul ini menggunakan hak akses `akses.getsiranap_ketersediaan_kamar()`, sehingga user yang memiliki izin akses SIRANAP otomatis dapat mengakses kedua form ini tanpa perlu penambahan kolom pada tabel `user`.
- Endpoint bridging membaca pengaturan `URLAPISIRS` (untuk update) dan `URLAPICORONA` (untuk referensi fasyankes), serta kredensial `IDSIRS` dan `PASSSIRS` di `setting/database.xml`.
