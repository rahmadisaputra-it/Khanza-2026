# Melanjutkan Form General Consent
Tanggal: 2026-07-23
Status: Selesai

## Tujuan
Melanjutkan format surat persetujuan umum (General Consent) yang baru sesuai draft `kamera.php` untuk rawat jalan dengan merangkai ulang poin A hingga G.

## File yang Diubah/Ditambah
- `report/rptSuratPersetujuanUmum.jrxml` — Menambahkan field `wewenang_keluarga`, `nama_keluarga_wewenang`, `izin_akses_rs`, `izin_privasi`, dan `permintaan_privasi` ke dalam tag `<queryString>` dan `<field>`. Menyisipkan elemen desain berupa `<staticText>` dan `<textField>` untuk poin C (Keinginan Privasi Pasien), D (Barang-barang Milik Pasien), E (Informasi Rawat Jalan), F (Informasi Biaya), dan G (Kewajiban Pembayaran) di akhir band detail pertama (koordinat Y = 650 s.d 908).

## Perubahan Database (jika ada)
- Tabel: `surat_persetujuan_umum`
- Perubahan: Tidak ada skema yang diubah, hanya menyesuaikan pemanggilan query untuk mengambil 5 kolom (`wewenang_keluarga`, `nama_keluarga_wewenang`, `izin_akses_rs`, `izin_privasi`, `permintaan_privasi`).
- Disetujui oleh: User

## Catatan/Risiko
Perubahan XML secara manual pada template JRXML berisiko menimbulkan hasil cetak yang tumpang tindih (overlap) jika ada sisa elemen teks lama bawaan yang masih ada. Direkomendasikan untuk membuka ulang file ini menggunakan editor visual **Jaspersoft Studio/iReport** guna mengkompilasi file menjadi `.jasper` serta merapikan posisi teks jika dibutuhkan.
