# Tambah Fitur Asal Resep di Rawat Inap (Permintaan Resep)
Tanggal: 2026-09-10
Status: Selesai

## Tujuan
Menerapkan fitur 'Asal Resep' pada tab Rawat Inap (tbResepRanap) di form DlgDaftarPermintaanResep.java agar sama dengan yang sudah ada di tab Rawat Jalan (tbResepRalan). Ini memastikan data 'Asal Permintaan' resep ditarik dari database dan dikirimkan ke form DlgCariObat2 saat input resep.

## File yang Diubah/Ditambah
- src/inventory/DlgDaftarPermintaanResep.java
  - Ditambahkan 3 kolom baru pada tabMode3 (Tgl.Penyerahan, Jam Penyerahan, Asal Permintaan).
  - Mengubah query SQL pada method tampil3() untuk menarik field tgl_penyerahan, jam_penyerahan, dan asal_resep.
  - Menambahkan array mapping di blok publish() dalam tampil3().
  - Mengubah method getData2() untuk mengekstrak string AsalResep dari kolom index ke-16.
  - Memasukkan parameter AsalResep menggunakan obatranap.setAsalResep(AsalResep) pada method panggilform2().

## Perubahan Database (jika ada)
- Tabel: resep_obat (hanya memanggil field yang sudah ada)
- Perubahan: Tidak ada ALTER TABLE.
- Disetujui oleh: N/A

## Catatan/Risiko
Perlu diuji dengan data resep pasien rawat inap untuk memastikan form input obat berhasil menerima asal resepnya.
