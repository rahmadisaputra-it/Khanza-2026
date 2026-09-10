# Tambah Field Kepercayaan Budaya di Cetak Identitas Pasien
Tanggal: 2026-09-05
Status: Selesai

## Tujuan
Menambahkan field kepercayaan_budaya dan kepercayaan_budaya_ket ke dalam query JasperReport rptRM1.jasper (Identitas Pasien) karena user menambahkannya di template jasper dan terjadi error unknown column.

## File yang Diubah/Ditambah
- `src/simrskhanza/DlgPasien.java` --- Mengubah:
  - Query select pada MnIdentitasActionPerformed untuk menambahkan pasien.kepercayaan_budaya dan pasien.kepercayaan_budaya_ket pada string query untuk ptRM1.jasper.

## Perubahan Database (jika ada)
- Tidak ada (Kolom sudah ditambahkan di request sebelumnya)

## Catatan/Risiko
- Hanya ptRM1.jasper (Identitas Pasien Utama) yang query-nya diperbarui dalam request ini. Jika file jasper identitas lain (misal ptRM2.jasper dsb) ditambahkan field yang sama, query di fungsi terkait (seperti MnIdentitas2ActionPerformed) juga harus di-update dengan cara yang sama.
