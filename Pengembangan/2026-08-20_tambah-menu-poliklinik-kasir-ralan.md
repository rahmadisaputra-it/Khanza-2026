# Tambah Menu Poliklinik di Klik Kanan Kasir Ralan
Tanggal: 2026-08-20
Status: Selesai (Parent Menu)

## Tujuan
Menambahkan JMenu baru bernama 'Poliklinik' pada popup menu (klik kanan) di DlgKasirRalan.java. Menu ini nantinya akan diisi dengan sub-menu per poli secara bertahap.

## File yang Diubah/Ditambah
- src/simrskhanza/DlgKasirRalan.java —
  - Mendeklarasikan variabel MnPoliklinik tipe javax.swing.JMenu.
  - Menginisialisasi MnPoliklinik di dalam initComponents().
  - Mengatur properti (teks, ikon, font) dan memasukkannya ke jPopupMenu1 sebelum MnDataRM.

## Perubahan Database
- Tidak ada.

## Catatan/Risiko
Karena DlgKasirRalan.form tidak ada, penambahan dilakukan secara hardcode di method initComponents(). Menu ini baru tampil di tab Registrasi Awal (jPopupMenu1). Sub-menu belum ditambahkan.

## Update Tambahan (Pindah Sub-Menu)
Memindahkan sub-menu 'Awal Keperawatan' (MnAwalKeperawatan) dari parent awalnya (MnRMRawatJalan) ke dalam menu baru (MnPoliklinik) di DlgKasirRalan.java.


## Update Tambahan (Clone Awal Keperawatan)
Membuat duplikat (clone) JMenu Awal Keperawatan beserta 6 JMenuItem di dalamnya agar tampil di menu Poliklinik tanpa menghilangkan yang asli di Data Rekam Medis. Menu yang baru di-suffix 'Poli' pada variabelnya namun dihubungkan ke actionPerformed() yang sama.


## Update Tambahan (Clone Data Tindakan Rawat Jalan)
Membuat duplikat (clone) JMenuItem 'Data Tindakan Rawat Jalan' (MnDataRalan) ke dalam menu Poliklinik sebagai MnDataRalanPoli. Dihubungkan ke actionPerformed yang sama dan hak aksesnya juga disamakan.


## Update Tambahan (Clone Risiko Jatuh)
Membuat duplikat (clone) JMenu 'Risiko Jatuh & Fungsional' (MnRMRisikoJatuh) beserta 7 JMenuItem di dalamnya ke dalam menu Poliklinik sebagai MnRMRisikoJatuhPoli. Semua item diarahkan ke action listener dan access rights yang sama.


## Update Tambahan (Perbaikan Clone Risiko Jatuh)
Membuat duplikat (clone) JMenu 'Risiko Jatuh & Fungsional' (MnRMRisikoJatuh) dengan cara membungkus inisialisasinya dalam method terpisah initCloneMenu() untuk menghindari limit 'Code too large' pada initComponents().
