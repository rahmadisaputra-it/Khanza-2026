# Penambahan Skrining Gizi 3 pada Pengkajian Awal Keperawatan Ranap
Tanggal: 2026-09-16
Status: Selesai

## Tujuan
Menambahkan inputan untuk Skrining Gizi 3 (pasien dengan diagnosa khusus) beserta nilainya (`SkalaGizi3`, `NilaiGizi3`, `pasien_diagnosa_khusus`) pada form Pengkajian Awal Keperawatan Umum (Rawat Inap), menyelaraskan dengan form Rawat Jalan.

## File yang Diubah/Ditambah
- `src/rekammedis/RMPenilaianAwalKeperawatanRanap.form` — Komponen GUI ditambahkan secara manual oleh user via NetBeans GUI Builder.
- `src/rekammedis/RMPenilaianAwalKeperawatanRanap.java` —
  - Implementasi logika event `SkalaGizi3ItemStateChanged` (set NilaiGizi3 "0"/"1" dan hitung total skor gizi) serta `SkalaGizi3KeyPressed`.
  - Update method `isTotalGizi()` agar mengikutsertakan `NilaiGizi3` dalam penjumlahan total skor gizi.
  - Set batas input 100 karakter pada `pasien_diagnosa_khusus`.
  - Penambahan kolom pada model tabel `tabMode` (total kolom dari 175 menjadi 178) dan penyesuaian lebar kolom `tbObat`.
  - Penyesuaian method `emptTeks()` untuk mereset komponen gizi baru.
  - Penyesuaian query SELECT dan pemetaan baris pada `tampil()`.
  - Penyesuaian `getData()` untuk membaca data baris tabel `tbObat` ke komponen form.
  - Penyesuaian query UPDATE dan parameter pada `ganti()`.
  - Penyesuaian query INSERT dan parameter pada `simpan()`.
  - Penyesuaian query laporan Jasper (`rptCetakPenilaianAwalKeperawatanRanap.jasper` dan `rptCetakPenilaianAwalKeperawatanRanap2.jasper`).

## Perubahan Database (jika ada)
- Tabel: `penilaian_awal_keperawatan_ranap`
- Perubahan: 
  ```sql
  ALTER TABLE penilaian_awal_keperawatan_ranap 
    ADD COLUMN skrining_gizi3 enum('Tidak','Ya') DEFAULT 'Tidak' AFTER nilai_gizi2,
    ADD COLUMN nilai_gizi3 int(11) DEFAULT 0 AFTER skrining_gizi3,
    ADD COLUMN pasien_diagnosa_khusus varchar(100) DEFAULT '' AFTER nilai_gizi3;
  ```
- Disetujui oleh: user (dikonfirmasi pada 2026-09-16)

## Catatan/Risiko
- Kolom di database dinamai `skrining_gizi3`, `nilai_gizi3`, dan `pasien_diagnosa_khusus` (selaras dengan penamaan `skrining_gizi1` dan `skrining_gizi2`).
- Jika template Jasper (`rptCetakPenilaianAwalKeperawatanRanap.jrxml`) ingin menampilkan baris ke-3 ini pada lembar cetak, tambahkan field tersebut ke layout JRXML template.
