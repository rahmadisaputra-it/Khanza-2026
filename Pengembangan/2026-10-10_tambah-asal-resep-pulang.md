# Penambahan Asal Resep pada Permintaan Resep Pulang dan Resep Pulang
Tanggal: 2026-10-10
Status: Selesai

## Tujuan
Mengatasi error `java.sql.SQLException: Column count doesn't match value count at row 1` saat menyimpan data di menu Permintaan Resep Pulang (`DlgPermintaanResepPulang`) dan Input Resep Pulang (`DlgInputResepPulang`), yang disebabkan oleh adanya kolom `asal_resep` pada tabel `permintaan_resep_pulang` dan `resep_pulang` namun query INSERT pada program belum menyertakan nilai kolom tersebut. Solusi dipilih menggunakan Opsi 2 (penambahan UI dropdown `cmbAsalResep`, auto-detect bangsal/kamar rawat inap pasien, dan pengisian nilai kolom secara lengkap tanpa mengubah struktur tabel database).

## File yang Diubah/Ditambah
- `src/inventory/DlgPermintaanResepPulang.java` — Menambahkan dropdown `cmbAsalResep` dan label `jLabelAsalResep`, auto-detect bangsal kamar di `setNoRm()`, load `asal_resep` di `tampilObat()`, navigasi keyboard, serta melengkapi query simpan/edit menjadi 9 kolom/nilai sesuai struktur tabel `permintaan_resep_pulang`.
- `src/simrskhanza/DlgInputResepPulang.java` — Menambahkan dropdown `cmbAsalResep` dan label `labelAsalResep`, auto-detect bangsal kamar di `setNoRm()`, load `asal_resep` dari `permintaan_resep_pulang` di `tampil2()`, navigasi keyboard, serta memperbaiki query INSERT ke `resep_pulang` dari 11 nilai menjadi 12 nilai sesuai struktur tabel `resep_pulang`.

## Perubahan Database (jika ada)
- Tabel: Tidak ada perubahan database (struktur tabel database dipertahankan sesuai arahan user, penyesuaian dilakukan murni di layer kode aplikasi).
- Perubahan: -
- Disetujui oleh: User (instruksi: "kita jangan ada ubah database lagi ya, ubah saja codingan nya")

## Catatan/Risiko
- Dropdown `cmbAsalResep` di kedua form diinisialisasi secara programmatic di luar blok `initComponents()` Matisse GUI Builder agar aman saat form dibuka kembali di NetBeans GUI Builder.
- Dropdown otomatis mendeteksi nama kamar/bangsal pasien saat form dibuka, namun user tetap dapat mengubah pilihan asal resep secara manual jika diperlukan.
