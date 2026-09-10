# Fix: Komponen kepercayaan_budaya di DlgPasien
Tanggal: 2026-09-06
Status: Selesai

## Tujuan
Memperbaiki error compile di DlgPasien.java karena komponen cmbKepercayaan dan txtKepercayaanKet
dipakai di banyak method (INSERT, UPDATE, load data) tetapi tidak pernah dideklarasikan.

## File yang Diubah/Ditambah
- src/simrskhanza/DlgPasien.java — Tambah deklarasi field 4 komponen + inisialisasi di konstruktor setelah initComponents()
- src/simrskhanza/DlgPasien.form — Tambah 4 komponen agar sinkron dengan NetBeans Design View

## Detail Komponen Baru
Di panel FormInput, baris y=372 (di bawah Cacat Fisik y=342):
- lblKepercayaan (Label) x=4, y=372 — teks 'Kep. Budaya :'
- cmbKepercayaan (ComboBox) x=102, y=372 — item: TIDAK ADA / ADA
- lblKepercayaanKet (Label) x=240, y=372 — teks 'Ket. Budaya :'
- txtKepercayaanKet (TextBox) x=338, y=372 — input keterangan budaya

## Perubahan Database
Tidak ada — kolom kepercayaan_budaya dan kepercayaan_budaya_ket sudah ada di tabel pasien.

## Catatan/Risiko
- Komponen ditambahkan manual (tidak via GUI Builder NetBeans).
- Field deklarasi aman di luar blok editor-fold, tidak akan tertimpa regenerate.
- Perlu test end-to-end: simpan pasien baru, cek DB, load kembali.

## Update (Lanjutan Fix Gagal Simpan)
Ternyata untuk aksi **Ganti/Edit**, method Sequel.mengedittf("pasien"...) sebelumnya tidak memasukkan field kepercayaan_budaya dan kepercayaan_budaya_ket ke dalam query UPDATE SQL. Sehingga ketika user mengedit data pasien lama, nilainya selalu terabaikan dan tetap sesuai nilai default di database.
- Telah diperbaiki: baris 4195 di DlgPasien.java sudah ditambahkan kepercayaan_budaya=? dan kepercayaan_budaya_ket=? pada query UPDATE, dan jumlah parameter ditambah menjadi 39.
