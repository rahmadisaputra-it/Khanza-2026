# Tambah Field Demografi dan Dokter di Laporan CPPT Rawat Jalan
Tanggal: 2026-08-28
Status: Selesai

## Tujuan
Menambahkan kolom jabatan/profesi PPA (dari pegawai.jbtn), serta menambahkan field data pasien (pendidikan, pekerjaan, alamat, no telp, agama, no peserta, no KTP) dan data poli/dokter pendaftaran di Header Laporan CPPT Rawat Jalan (rptJalanPemeriksaan.jrxml).

## File yang Diubah/Ditambah
- src/simrskhanza/DlgRawatJalan.java — Mengubah struktur query Valid.MyReportqry pada laporan CPPT. Menambahkan JOIN ke tabel poliklinik dan dokter, serta memanggil field-field demografi pasien dan jabatan pegawai.jbtn.
- report/rptJalanPemeriksaan.jrxml — Menambahkan definisi <field> untuk variabel baru yang dipanggil dari database. Mengganti field profesi lama dengan $F{jbtn}. Memperbaiki tinggi *band Detail* dari 190px menjadi 120px agar jarak antar baris tidak terlalu jauh.

## Perubahan Database (jika ada)
- Tabel: -
- Perubahan: Tidak ada perubahan skema, hanya mengubah relasi query SQL.
- Disetujui oleh: User

## Catatan/Risiko
- Perubahan pada query report di DlgRawatJalan.java ini mengharuskan compile ulang (Clean and Build) di NetBeans setiap ada modifikasi agar aplikasi memanggil query terbaru.