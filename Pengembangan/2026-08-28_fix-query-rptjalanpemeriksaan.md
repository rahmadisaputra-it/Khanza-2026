# Fix Query Laporan Pemeriksaan Rawat Jalan
Tanggal: 2026-08-28
Status: Selesai

## Tujuan
Memperbaiki bug (error query/null field) pada laporan `rptJalanPemeriksaan.jrxml` dimana field `tgl_lahir` dan `jk` (jenis kelamin) tidak ditemukan dalam query yang dilampirkan, sehingga mengakibatkan null atau gagal ditarik ke dalam report saat dicetak dari UI form `DlgRawatJalan.java`.

## File yang Diubah/Ditambah
- `src/simrskhanza/DlgRawatJalan.java` — Menambahkan kolom `pasien.tgl_lahir` dan `pasien.jk` pada perintah `select` dalam fungsi pemanggilan laporan `Valid.MyReportqry("rptJalanPemeriksaan.jasper", ...)`.
- `report/rptJalanPemeriksaan.jrxml` — Menambahkan kolom `pasien.tgl_lahir` dan `pasien.jk` ke dalam `queryString` laporan.

## Perubahan Database (jika ada)
- Tabel: -
- Perubahan: -
- Disetujui oleh: User

## Catatan/Risiko
Perubahan dilakukan karena file `.jrxml` memiliki deklarasi pemanggilan field `tgl_lahir` dan `jk` (beserta logika kondisi L/P), tetapi di dalam query bawaan dan yang di-pass dari Java, kedua field tersebut belum ada. Dengan penambahan ini, report CPPT Rawat Jalan akan dapat menampilkan tanggal lahir dan jenis kelamin tanpa error.
