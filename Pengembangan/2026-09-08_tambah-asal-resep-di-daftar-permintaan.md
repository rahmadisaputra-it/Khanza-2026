# Menambahkan Data Asal Resep ke Daftar Permintaan Resep & Cari Obat
Tanggal: 2026-09-08
Status: Selesai

## Tujuan
Memastikan kolom `asal_resep` dari tabel `resep_obat` dapat dibaca dan ditampilkan pada tabel di form `DlgDaftarPermintaanResep.java`. Kemudian, ketika tombol Tambah diklik (yang memanggil form `DlgCariObat.java`), nilai combobox Asal Resep (yang sebelumnya ditambahkan manual) akan otomatis terisi sesuai dengan data tersebut.

## File yang Diubah
- `src/inventory/DlgDaftarPermintaanResep.java`
- `src/inventory/DlgCariObat.java`

## Ringkasan Perubahan Kode
**Di `DlgDaftarPermintaanResep.java`:**
1. Mendeklarasikan variabel `AsalResep` pada class scope.
2. Menambahkan "Asal Permintaan" ke array object judul kolom `tabMode` (index 16).
3. Mengubah query SQL pada method `tampil()` -> `doInBackground()` di dua tempat untuk men-select `resep_obat.asal_resep`.
4. Menambahkan `rs.getString("asal_resep")` pada fungsi `publish()` sehingga datanya masuk ke model tabel.
5. Pada method `getData()`, menambahkan assignmen `AsalResep=tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(), 16).toString();`
6. Mengosongkan `AsalResep=""` pada `TeksKosong()`.
7. Memanggil method setter ke DlgCariObat: `obatralan.setAsalResep(AsalResep);` tepat sebelum memanggil `obatralan.isCek()` pada method `panggilform()`.

**Di `DlgCariObat.java`:**
1. Menambahkan custom method `setAsalResep(String asal_resep)` yang akan melakukan `cmbAsalResep.setSelectedItem(asal_resep)` jika nilainya tidak kosong.

## Catatan
- Komponen visual UI (combobox & label) pada `DlgCariObat` dibuat secara manual di luar blok GEN.
- Testing di local DB wajib memastikan field `asal_resep` tidak error.
