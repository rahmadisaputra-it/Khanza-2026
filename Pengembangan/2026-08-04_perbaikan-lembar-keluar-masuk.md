# Perbaikan Laporan Lembar Keluar Masuk & Tanda Tangan Elektronik
Tanggal: 2026-08-04
Status: Selesai

## Tujuan
Memperbaiki error kompilasi pada laporan "Ringkasan Masuk Keluar" akibat fungsi `SimpleDateFormat` bawaan Java yang tidak cocok dengan tipe data Jasper. Selain itu, menyesuaikan QR Code untuk Tanda Tangan Elektronik (TTE) agar mengambil data hash sidik jari dari masing-masing dokter (Dokter DPJP / Menerima dan Dokter Konsultan), serta menambahkan data Penanggung Jawab dari tabel `surat_persetujuan_umum`.

## File yang Diubah/Ditambah
- `report/rptLembarKeluarMasuk3.jrxml` — 
  1. Mengubah `new SimpleDateFormat` menjadi `new java.text.SimpleDateFormat` untuk mencegah error kompilasi.
  2. Menambahkan `LEFT JOIN surat_persetujuan_umum` di bagian *Query* agar dapat memanggil data `nama_pj` dan `umur_pj`.
  3. Mengatur QR Code menggunakan parameter `$P{finger}` (Dokter Yang Menerima) dan `$P{finger2}` (Dokter Konsultan) yang sudah dikirimkan secara bawaan oleh `RMDataCatatanMasukKeluar.java`.
  4. Menerapkan *markup html* `<font size='1'>` pada Expression nama dokter untuk mengecilkan tulisan (Shrink to Fit) jika panjang nama melebihi batas.

## Perubahan Database (jika ada)
- Tabel: - (Tidak ada)
- Perubahan: - (Hanya penyesuaian query di JRXML, tidak ada ALTER TABLE).
- Disetujui oleh: User

## Catatan/Risiko
Perubahan JRXML ini akan dieksekusi dengan sempurna jika dicetak melalui menu **Catatan Masuk Keluar** (`RMDataCatatanMasukKeluar.java`). Jika dicetak dari menu Pendaftaran, kemungkinan parameter TTE akan kosong karena `DlgReg.java` mem-bypass eksekusi query internal JRXML.
