# Edit Template Surat Pernyataan Pasien Umum
Tanggal: 2026-08-06
Status: Selesai

## Tujuan
Menyesuaikan template cetakan JasperReport Surat Pernyataan Pasien Umum (`rptSuratPernyataanPasienUmum.jrxml`) agar isinya persis 100% dengan format dokumen Word (.docx) RS Permata Madina Panyabungan. Secara khusus:
- Menambahkan parameter `pasien.no_ktp` ke dalam query select.
- Menambahkan kolom tampilan untuk Nama Pasien, Alamat Pasien, dan Nomor KTP/SIM Pasien di bagian atas surat (sebelum isi pernyataan).

## File yang Diubah/Ditambah
- `report/rptSuratPernyataanPasienUmum.jrxml` — Menambahkan `pasien.no_ktp` ke dalam query, menggeser letak Y-koordinat komponen teks pernyataan ke bawah untuk memberikan ruang, lalu menyisipkan komponen (Text Field & Static Text) baru untuk identitas pasien lengkap.

## Perubahan Database (jika ada)
- Tabel: -
- Perubahan: Tidak ada perubahan skema database (data pasien dan no KTP ditarik langsung dari tabel `pasien` yang sudah ada via JOIN).
- Disetujui oleh: User (diminta sesuaikan JRXML)

## Catatan/Risiko
- Form Java `SuratPernyataanPasienUmum.java` tidak perlu diubah karena proses penyimpanan data Penanggung Jawab sudah lengkap. KTP Pasien murni hanya butuh di-query saat mencetak saja.
- Pastikan iReport/Jaspersoft Studio dapat membaca format JRXML ini dengan normal tanpa error compilasi. Koordinat Y sengaja digeser +85 point agar muat.
