# Tambah Dropdown Kelas BPJS dan Kelas Rawat di DlgReg
Tanggal: 2026-08-20
Status: Selesai

## Tujuan
Menambahkan komponen dropdown (ComboBox) untuk pilihan Kelas BPJS dan Kelas Rawat yang berisi item VIP, Kelas 1, Kelas 2, Kelas 3 pada form pendaftaran rawat jalan (DlgReg.java), tepat di sebelah kanan field Asal Rujukan.

## File yang Diubah/Ditambah
- src/simrskhanza/DlgReg.java —
  - Menginisialisasi CmbKelasBPJS dan CmbKelasRawat.
  - Menambahkan model pilihan VIP, Kelas 1, Kelas 2, Kelas 3.
  - Menambahkan kedua komponen ke layout absolute FormInput di sumbu X & Y (setBounds).
  - Mendeklarasikan kedua variabel di bagian source code.

## Perubahan Database
- Tidak ada perubahan database.

## Catatan/Risiko
Perubahan UI di-inject manual ke initComponents karena file DlgReg.form tidak ditemukan. Layout akan tampil saat di-run, namun tidak bisa dimodifikasi dari tab Design NetBeans. Pastikan Clean and Build agar perubahan UI muncul.

## Update Tambahan (Teks Statis)
Menambahkan komponen teks (Label) 'Kelas Bpjs :' dan 'Kelas Rawat :' di sebelah combobox masing-masing. Diimplementasikan secara hardcode di DlgReg.java (menggeser titik koordinat X combobox agar tidak bertabrakan).
