# Perbaikan Urutan Parameter Simpan Pasien (kd_prop Error)
Tanggal: 2026-10-10
Status: Selesai

## Tujuan
Memperbaiki error `java.sql.SQLException: Incorrect integer value: 'TIDAK ADA' for column 'kd_prop' at row 1` saat menekan tombol Simpan di form pendaftaran/master pasien (`DlgPasien.java`). Error ini terjadi karena ketidaksesuaian urutan array parameter pada query `INSERT INTO pasien VALUES (...)` dengan struktur urutan fisik kolom tabel `pasien` di database.

## File yang Diubah/Ditambah
- `src/simrskhanza/DlgPasien.java` — Memperbaiki urutan parameter array pada 5 panggilan `Sequel.menyimpantf2` / `Sequel.menyimpantf` di method `BtnSimpanActionPerformed`. Nilai `cmbKepercayaan` dan `txtKepercayaanKet` dipindahkan ke posisi setelah `kdcacat`, sebelum `EMail`, sesuai urutan kolom tabel `pasien`:
  - Kolom 30: `suku_bangsa`
  - Kolom 31: `bahasa_pasien`
  - Kolom 32: `cacat_fisik`
  - Kolom 33: `kepercayaan_budaya`
  - Kolom 34: `kepercayaan_budaya_ket`
  - Kolom 35: `email`
  - Kolom 36: `nip`
  - Kolom 37: `kd_prop`
  - Kolom 38: `propinsipj`

## Perubahan Database (jika ada)
Tidak ada perubahan database (struktur database tetap dipertahankan sesuai instruksi).

## Catatan/Risiko
- Perubahan hanya pada method `BtnSimpanActionPerformed` di file `DlgPasien.java`.
- Modul lain dan tombol Ganti (`BtnGantiActionPerformed`) tidak terdampak karena query update secara eksplisit sudah menggunakan klausa `SET nama_kolom=?`.
