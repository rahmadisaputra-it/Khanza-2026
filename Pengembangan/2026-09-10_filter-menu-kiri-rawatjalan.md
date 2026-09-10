# Filter/Search Menu Kiri Rawat Jalan
Tanggal: 2026-09-10
Status: Selesai

## Tujuan
Menambahkan textbox pencarian/filter di atas panel menu kiri form Rawat Jalan.
Saat user mengetik (misal: `anas`), menu yang tidak mengandung kata tersebut otomatis disembunyikan,
tanpa perlu klik tombol cari. Ketika textbox dikosongkan kembali, semua menu muncul lagi.

## File yang Diubah/Ditambah
- `src/simrskhanza/DlgRawatJalan.java` — tambah JTextField filter di dalam `initComponents()` setelah `PanelAccor.add(ChkAccor, EAST)` (sekitar baris 3475)

## Perubahan Database (jika ada)
Tidak ada perubahan database.

## Detail Teknis
- File tidak punya `.form` (pure code) -> aman edit langsung di `initComponents()`
- Komponen yang ditambah:
  - `TFilterMenu` — JTextField (185x22px, font Tahoma 11)
  - `pFilterMenu` — JPanel wrapper dengan border bawah tipis abu-abu, di-add ke `PanelAccor` posisi `BorderLayout.NORTH`
- Logic filter via `DocumentListener`:
  - Iterasi semua komponen FormMenu (semua turunan AbstractButton)
  - Bandingkan `getText().toLowerCase()` dengan keyword (case-insensitive)
  - Set `setVisible(true/false)` sesuai hasil cocok
  - `FormMenu.revalidate()` + `repaint()` untuk refresh layout

## Catatan/Risiko
- Filter hanya menyembunyikan button secara visual (setVisible), tidak menghapus - aman
- FormMenu menggunakan FlowLayout, saat button disembunyikan layout otomatis merapat
- Tidak ada dampak ke modul lain

## Update: DlgRawatInap.java
Fitur filter dan desain yang sama juga sudah diterapkan di form Rawat Inap (`src/simrskhanza/DlgRawatInap.java`) pada area layout menu di kiri (PanelAccor).
