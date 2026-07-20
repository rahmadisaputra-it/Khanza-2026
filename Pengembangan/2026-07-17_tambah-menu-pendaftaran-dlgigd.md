# Tambah Menu Pendaftaran di DlgIGD
Tanggal: 2026-07-17
Status: Selesai (sebagian — 3 item masih stub)

## Tujuan
Menambahkan menu "Pendaftaran" di klik kanan `jPopupMenu1` pada form IGD (`DlgIGD.java`) agar petugas IGD bisa langsung mengakses formulir-formulir pendaftaran tanpa harus berpindah menu.

## File yang Diubah/Ditambah
- `src/simrskhanza/DlgIGD.java` — Menambahkan:
  - `MnPendaftaran` (JMenu parent baru, posisi pertama di jPopupMenu1)
  - `MnEvaluasiKelengkapanRM` (JMenuItem baru — stub)
  - `MnCatatanMasukKeluarRI` (JMenuItem baru — stub)
  - `MnNaikKelas` (JMenuItem baru — stub)
  - 4 item lama di-share ke MnPendaftaran: `MnPersetujuanUmum`, `MnPersetujuanRawatInap`, `MnPernyataanPasienUmum`, `MnPernyataanMemilihDPJP`

## Susunan Sub-menu Pendaftaran di DlgIGD
| # | Label Menu | Variabel | Status |
|---|---|---|---|
| 1 | Lembar Evaluasi Kelengkapan Berkas RM | `MnEvaluasiKelengkapanRM` | ⏳ Stub — tunggu `rptEvaluasiKelengkapanRM.jasper` |
| 2 | Persetujuan Umum (Hak & Kewajiban + General Consent) | `MnPersetujuanUmum` | ✅ Fungsional |
| 3 | Persetujuan Rawat Inap | `MnPersetujuanRawatInap` | ✅ Fungsional |
| 4 | Catatan Masuk & Keluar RI | `MnCatatanMasukKeluarRI` | ⏳ Stub — tunggu `rptCatatanMasukKeluarRI.jasper` |
| 5 | Pernyataan Bersedia Jadi Pasien Umum | `MnPernyataanPasienUmum` | ✅ Fungsional |
| 6 | Surat Pernyataan Naik Kelas | `MnNaikKelas` | ⏳ Stub — tunggu `rptSuratNaikKelas.jasper` |
| 7 | Formulir Keinginan Pasien Memilih DPJP | `MnPernyataanMemilihDPJP` | ✅ Fungsional |

## Perubahan Database (jika ada)
- Tidak ada

## Catatan/Risiko
- File `.form` tidak tersedia, perubahan hardcode di `initComponents()`.
- 4 item yang di-share (reuse) dari menu Surat Persetujuan: di Java Swing satu JMenuItem hanya bisa ada di satu parent. Item-item ini akan **berpindah** ke MnPendaftaran dan hilang dari MnSuratPersetujuan saat dijalankan.
- Stub handler menampilkan dialog "Fitur sedang dalam pengembangan" — perlu diganti dengan logika cetak JasperReports saat file `.jasper` tersedia.
- Nama file `.jasper` yang perlu dibuat:
  - `rptEvaluasiKelengkapanRM.jasper`
  - `rptCatatanMasukKeluarRI.jasper`
  - `rptSuratNaikKelas.jasper`
