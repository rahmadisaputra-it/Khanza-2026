# AGENTS.md — SIMRS Khanza

Software RS produksi nyata (data pasien sungguhan). User: programmer belajar Java sambil kerja.

## Aturan Kerja (ringkas, wajib)
1. Cari konteks modul **secukupnya** — pakai grep/search untuk cari method/class spesifik, JANGAN baca utuh file besar (`frmUtama.java` ~2.4MB) kecuali memang sedang mengedit bagian itu.
2. Analisa dulu, baru rencana singkat (file + perubahan + dampak), baru eksekusi.
3. Scope ketat: hanya modul/file yang diminta. No refactor massal, no "bonus cleanup".
4. DB schema (`ALTER TABLE`/tabel baru): wajib tanya persetujuan dulu.
5. Ikuti pola existing: JDBC statis via `sekuel`/`validasi` (bukan DAO), GUI lewat `.form` Builder.
6. Update `PROJECT_KNOWLEDGE.md`/`README.md` hanya kalau ada perubahan arsitektur signifikan — bukan tiap fitur kecil.

## Mode Kerja
- **Default = Guide-Only.** Kasih instruksi step-by-step ringkas (file, struktur, query) — user ketik sendiri. Jangan jelaskan dasar-dasar yang sudah jelas dari konteks (user sudah programmer).
- **Izin Write**: hanya jika user bilang eksplisit ("izinin write", dll). Konfirmasi scope singkat → edit → ringkas apa yang berubah. Berlaku 1x per task.

## Hemat Kuota
- Jangan baca ulang `PROJECT_KNOWLEDGE.md` di setiap pesan — cukup sekali di awal task baru, atau saat ganti modul.
- Jawaban langsung ke poin; skip rekap/penjelasan panjang kecuali user minta detail.
- Untuk pertanyaan kecil/spesifik (1 method, 1 query), jangan trigger full project exploration — jawab langsung dari konteks yang relevan saja.
- Hindari bolak-balik "konfirmasi rencana" untuk perubahan trivial (≤5 baris, 1 file) — langsung kasih instruksi.

## Dokumentasi Wajib — Folder `Pengembangan/`
Setiap ada perubahan (kode baru, edit file existing, perubahan database), buat/update 1 file `.md` di folder `Pengembangan/` — **wajib**, tanpa kecuali, sekecil apapun perubahannya.

- **Nama file**: `Pengembangan/YYYY-MM-DD_nama-fitur-singkat.md` (mis. `Pengembangan/2026-06-21_tambah-field-rekam-medis-lama.md`)
- **Isi minimal**:
  ```md
  # [Nama Fitur/Perubahan]
  Tanggal: YYYY-MM-DD
  Status: [Selesai / Dalam Pengerjaan]

  ## Tujuan
  [kenapa perubahan ini dibuat, dari permintaan siapa kalau ada]

  ## File yang Diubah/Ditambah
  - `path/file1.java` — [apa yang diubah]
  - `path/file2.form` — [apa yang diubah]

  ## Perubahan Database (jika ada)
  - Tabel: [nama tabel]
  - Perubahan: [ALTER TABLE / kolom baru / tabel baru — sertakan query SQL persis yang dijalankan]
  - Disetujui oleh: [user, sesuai aturan #4]

  ## Catatan/Risiko
  [hal yang perlu diperhatikan ke depannya, modul lain yang terdampak, dll]
  ```
- File ini dibuat **di akhir task**, setelah perubahan selesai dan terverifikasi jalan — bukan di awal sebagai rencana (rencana cukup disampaikan di chat).
- Ini terpisah dari aturan #6 (update `PROJECT_KNOWLEDGE.md`) — `PROJECT_KNOWLEDGE.md` hanya untuk perubahan arsitektur besar, sedangkan `Pengembangan/` adalah **log history tiap perubahan**, sekecil apapun.

## Bahaya Khusus RS
Query data pasien/billing/rekam medis → ingatkan test di DB lokal dulu. Modul saling terhubung — sebut modul lain yang mungkin terdampak, jangan asumsikan perubahan terisolasi. Jangan sarankan test langsung di server production aktif.
