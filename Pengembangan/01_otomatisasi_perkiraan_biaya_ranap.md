# Dokumentasi Pengembangan: Otomatisasi Perkiraan Biaya Rawat Inap

**Tanggal**: 21 Juni 2026  
**Fitur yang Dikembangkan**: Perkiraan Tarif Otomatis berdasarkan Diagnosa Awal Pasien

---

## 1. Tujuan Pengembangan
Membuat nilai "Perkiraan Tarif" pada menu Perkiraan Biaya Rawat Inap agar muncul secara otomatis. Sistem tidak lagi bergantung sepenuhnya pada input manual, melainkan langsung mengambil data tarif estimasi berdasarkan kode Diagnosa Awal milik pasien. Jika pasien memiliki lebih dari satu diagnosa (dipisah dengan tanda koma), sistem akan mengakumulasikan total biayanya secara otomatis.

---

## 2. Perubahan Database
Terdapat penambahan 1 tabel master baru ke dalam database `sik_new`.

**Tabel `inacbg_dummy`**: Berfungsi sebagai penyimpan master data tarif estimasi berdasarkan kode penyakit. Tabel ini berelasi langsung dengan tabel `penyakit`.
```sql
CREATE TABLE IF NOT EXISTS `inacbg_dummy` (
  `kd_penyakit` varchar(10) NOT NULL,
  `biaya` double NOT NULL DEFAULT 0,
  PRIMARY KEY (`kd_penyakit`),
  CONSTRAINT `fk_inacbg_dummy_penyakit` FOREIGN KEY (`kd_penyakit`) REFERENCES `penyakit` (`kd_penyakit`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
```

---

## 3. Perubahan Source Code
File utama yang dimodifikasi adalah **`src/keuangan/DlgPerkiraanBiayaRanap.java`**.

**Rincian Modifikasi:**
1. **Method `tampil()`**:
   - Menambahkan logika pemisahan string (split) untuk `diagnosa_awal` (tabel `kamar_inap`) berdasarkan koma (`,`).
   - Melakukan perulangan (*looping*) untuk setiap kode penyakit guna melakukan *query* pengambilan nilai `biaya` dari tabel `inacbg_dummy`.
   - Menjumlahkan akumulasi nilai tersebut dan menjadikannya sebagai angka keluaran di kolom "Perkiraan Tarif" pada tabel `tbBangsal`.
   - Evaluasi kolom `Limit` otomatis diperbarui untuk membandingkan total biaya aktual dengan nilai perkiraan tarif yang baru ini.

2. **Komponen GUI (Matisse / Inspector)**:
   - Menambahkan menu item `MnTambahPerkiraan` pada popup menu riwayat tabel (`jPopupMenu2`). Berfungsi agar pengguna dapat mengakumulasikan nominal baru ke tarif yang sudah ada di database (`perkiraan_biaya_ranap`).

3. **Method Baru `MnTambahPerkiraanActionPerformed()`**:
   - Mengambil angka dari tabel `perkiraan_biaya_ranap` dan menjumlahkannya dengan nilai yang baru dipilih oleh *user* dari tabel `tbNilaiINACBG`, lalu menimpanya (simpan ulang) kembali ke database.

4. **Event `tableChanged` pada tabel `tbNilaiINACBG`**:
   - Menambahkan *TableModelListener* di dalam blok konstruktor kelas.
   - Bertugas memantau perubahan input angka (edit langsung pada tabel kolom index 2 yaitu "Biaya").
   - Jika *user* mengetik angka biaya dummy, angka tersebut akan langsung di-simpan/update ke tabel `inacbg_dummy` secara _real-time_ menggunakan pola perintah *query* `REPLACE INTO`.

---

## 4. Alur Logika (Singkat)
1. User membuka menu. Tabel `tbBangsal` kosong, lalu *query* narik pasien yang belum pulang.
2. Saat menelusuri data tiap pasien, total biaya aktual dihitung.
3. Kemudian sistem mengecek diagnosa awal pasien tersebut. Kode ICD-10 dipecah per koma, dicocokkan dengan `inacbg_dummy`. Semua biaya yang dapat dari sana ditotalkan. Total inilah yang muncul di layar (kolom Perkiraan Tarif otomatis).
4. Kalau ternyata di `inacbg_dummy` belum ada tarifnya, maka sistem mencari input cadangan di tabel manual (`perkiraan_biaya_ranap`).
5. Perbandingan dilakukan: Kalau biaya aktual > perkiraan tarif, muncul limit = "Tidak Aman". Kalau sebaliknya, "Aman".
