# Penambahan Project Service SIRANAP (KhanzaHMSServiceSIRSYankes)
Tanggal: 2026-06-29
Status: Selesai

## Tujuan
Membuat dan memasukkan project standalone `KhanzaHMSServiceSIRSYankes` ke dalam folder induk `SIMRS-Khanza` agar sejajar dengan service lain seperti `KhanzaHMSServiceAplicare`. Service ini digunakan secara khusus di komputer Server untuk melakukan auto-update ketersediaan kamar ke Kemenkes tanpa membebani aplikasi client.

## File yang Diubah/Ditambah
- Menambahkan folder project `KhanzaHMSServiceSIRSYankes/` ke dalam struktur direktori `SIMRS-Khanza/`.
- `KhanzaHMSServiceSIRSYankes/src/khanzahmsservicesirsyankes/frmUtama.java` — [Baru] Melakukan migrasi kode dari project `sirs` lama dan menyesuaikan import `ApiKemenkesSirs`.
- `KhanzaHMSServiceSIRSYankes/src/fungsi/` — [Baru] Meng-copy file utilitas `koneksiDB.java`, `sekuel.java`, dan `validasi.java` dari core Khanza.
- `KhanzaHMSServiceSIRSYankes/src/bridging/ApiKemenkesSirs.java` — [Baru] Meng-copy file API dari core Khanza.

## Perubahan Database (jika ada)
- Tabel: -
- Perubahan: -

## Catatan/Risiko
Project ini berjalan secara mandiri dan di-build menjadi JAR terpisah. User wajib memastikan library yang digunakan sama dengan Khanza utama.
