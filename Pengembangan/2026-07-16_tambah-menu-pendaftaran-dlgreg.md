# Tambah Menu Pendaftaran di DlgReg
Tanggal: 2026-07-16 (diupdate 2026-07-17)
Status: Dalam Pengerjaan

## Tujuan
Merapikan menu klik kanan (`jPopupMenu1`) di `DlgReg.java` dengan menambahkan menu parent baru **"Pendaftaran"** beserta sub-menu untuk Pendaftaran Rawat Jalan.

## File yang Diubah/Ditambah
- `src/simrskhanza/DlgReg.java` — Menambahkan:
  - `MnPendaftaran` (JMenu parent baru, posisi urutan pertama di jPopupMenu1)
  - `MnCetakAntrian` (JMenuItem baru, action handler stub siap diisi)
  - Referensi `MnPersetujuanUmum` dan `MnSEP` dipasang ulang sebagai sub-menu di MnPendaftaran (tanpa duplikasi kode, hanya dipasang di dua tempat)

## Susunan Sub-menu Pendaftaran
1. **Persetujuan Umum** (`MnPersetujuanUmum`) — Surat gabungan Hak & Kewajiban Pasien + General Consent, sudah ada di menu Surat Persetujuan, di-share ke sini
2. **SEP BPJS** (`MnSEP`) — Buat SEP BPJS, sudah ada di menu Bridging, di-share ke sini
3. **Cetak Antrian** (`MnCetakAntrian`) — Item baru, action handler stub tersedia di baris ~9940, logika cetak belum diimplementasi

## Perubahan Database (jika ada)
- Tidak ada

## Catatan/Risiko
- File `.form` tidak tersedia, perubahan hardcode di `initComponents()`. Jika dikemudian hari ada regenerasi GUI, bagian ini perlu diperiksa ulang.
- `MnPersetujuanUmum` dan `MnSEP` sekarang muncul di DUA tempat (menu Pendaftaran + menu asalnya). Di Java Swing, satu JMenuItem hanya bisa ada di SATU parent menu sekaligus. **Ini perlu ditangani** — opsi: buat item baru yang memanggil action yang sama, atau pindahkan sepenuhnya ke menu Pendaftaran dan hapus dari menu lama.
