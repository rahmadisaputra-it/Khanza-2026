# Sinkronisasi Pembaruan Modul Bridging & SatuSehat Terbaru dari Upstream
Tanggal: 2026-10-07  
Status: Selesai  

## Tujuan
Menerapkan pembaruan modul `bridging` terbaru dari upstream GitHub (`mas-elkhanza`) ke dalam project SIMRS Khanza lokal, serta melengkapi seluruh dependensi pendukung agar seluruh modul bridging (termasuk SatuSehat TTE, KPTL, dan Composition) dapat di-compile dengan sukses tanpa merusak skema database dan hak akses yang ada.

## File yang Diubah/Ditambah
- `src/widget/Menu.java` — [Baru] Class widget menu turunan `JMenu` dari upstream untuk mendukung UI form bridging.
- `src/widget/MenuItem.java` — [Baru] Class widget menu item turunan `JMenuItem` dari upstream untuk mendukung UI form bridging.
- `src/fungsi/koneksiDB.java` — [Diubah] Penambahan method konfigurasi bridging SatuSehat (`URLAPPLINKSATUSEHAT()`, `IDORGBPJSSATUSEHAT()`, dll).
- `src/fungsi/akses.java` — [Diubah] Penambahan 8 variabel hak akses SatuSehat bridging (`satu_sehat_mapping_kptl_*`, `satu_sehat_tanda_tangan_elektronik`, `satu_sehat_kirim_composition`), getter method-nya, serta proteksi `try-catch` saat membaca dari database `admin` agar login user non-admin tidak crash jika kolom belum ditambahkan di database.
- `src/keuangan/DlgJnsPerawatanOperasi.java` — [Diubah] Penambahan method `setTabData()` untuk pemilihan tab yang dipanggil oleh modul `SatuSehatMapingTindakanOperasiKPTL`.
- `src/bridging/*` — [Diperbarui] Seluruh 260+ file bridging sinkron dengan versi terbaru dari upstream.

## Perubahan Database (jika ada)
- Tabel: `admin`
- Perubahan: Tidak ada perubahan paksa ke database. Pembacaan hak akses baru di `akses.java` dibungkus `try-catch` sehingga kompatibel baik dengan database lama (default `true`) maupun jika nanti di-`ALTER TABLE`.
- Disetujui oleh: User

## Catatan/Risiko
- Seluruh 1661 file source code SIMRS Khanza berhasil di-compile dengan status **BUILD SUCCESSFUL**.
- Fitur bridging baru kini sudah aktif dan siap digunakan.
