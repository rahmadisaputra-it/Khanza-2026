# Dokumentasi Pengembangan: Inovasi Logika 3 Level Status Limit Perkiraan Biaya

**Tanggal**: 23 Juni 2026  
**Fitur yang Dikembangkan**: Penambahan logika 3 level persentase pada kolom status "Limit" di menu Perkiraan Biaya Rawat Inap.

---

## 1. Tujuan Pengembangan
Meningkatkan fleksibilitas dan fungsi *early warning* (peringatan dini) bagi petugas terkait pembengkakan biaya pasien. Jika sebelumnya status hanya terbagi 2 level ("Aman" dan "Tidak Aman"), kini sistem dapat mendeteksi kondisi "Mendekati" batas atas agar petugas bisa mengambil tindakan preventif sebelum biaya betul-betul membengkak melebihi standar INA-CBG.

Tiga level status tersebut berdasarkan persentase (Biaya Aktual dibagi Perkiraan Tarif):
- **< 80%** = Aman
- **80% - 99%** = Mendekati
- **≥ 100%** = Tidak Aman

---

## 2. Perubahan Database
Tidak ada perubahan struktur maupun penambahan tabel di database pada pengembangan fitur ini.

---

## 3. Perubahan Source Code
File yang dimodifikasi adalah **`src/keuangan/DlgPerkiraanBiayaRanap.java`**.

**Rincian Modifikasi:**
1. **Method `tampil()`**:
   - Menghapus logika statis lama yang hanya mengecek `if (perkiraantarif <= Jumlah)`.
   - Menggantinya dengan perhitungan matematis: `double persentase = (Jumlah / perkiraantarif) * 100;`.
   - Menambahkan struktur pengecekan berjenjang (*if-else if-else*) untuk menetapkan nilai variabel `pros` menjadi "Aman", "Mendekati", atau "Tidak Aman" sesuai dengan kondisi parameter di atas.
   - Tetap menangani kasus *edge case* jika `perkiraantarif` kebetulan bernilai `0` (untuk menghindari error *Divide by Zero* di level logik).

---

## 4. Alur Logika (Singkat)
1. Sama seperti biasa, sistem menjumlahkan total biaya (tagihan) aktual milik pasien ke dalam variabel `Jumlah`.
2. Sistem menarik nilai `perkiraantarif` (estimasi biaya RS/INA-CBG).
3. Jika nilai `perkiraantarif` > 0, sistem mengkalkulasi persentase: `(Jumlah / perkiraantarif) * 100`.
4. Jika hasil persentasenya 100 atau lebih, statusnya ditulis "**Tidak Aman**".
5. Jika hasil persentasenya antara 80 sampai 99.9, statusnya ditulis "**Mendekati**".
6. Selain itu (di bawah 80%), statusnya ditulis "**Aman**".
7. Hasil ini kemudian di-*render* langsung ke dalam layar GUI tabel petugas.
