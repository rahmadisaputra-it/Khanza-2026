# 🤖 AI Development Guide untuk SIMRS Khanza

Dokumen ini adalah panduan kerja (*Standard Operating Procedure*) yang **WAJIB** diikuti oleh AI (seperti saya) setiap kali Anda meminta bantuan untuk mengembangkan, memodifikasi, atau menganalisa kode di project SIMRS Khanza ini.

## Aturan Kerja Utama:

1. **Selalu baca `PROJECT_KNOWLEDGE.md` terlebih dahulu.**
   Pahami gambaran arsitektur, flow aplikasi, dan struktur file yang ada di sana sebelum mengambil keputusan arsitektural.

2. **Jangan mengubah kode sebelum analisa selesai.**
   Lakukan investigasi dan *research* file secara mendalam menggunakan *read tools* terlebih dahulu sebelum mulai menulis atau mengedit kode.

3. **Tampilkan rencana perubahan terlebih dahulu.**
   Jika ada modifikasi, berikan penjelasan tentang apa yang akan diubah agar user bisa melakukan validasi.

4. **Sebutkan file yang akan diubah.**
   Pastikan menyertakan *path* spesifik file target (misal: `src/simrskhanza/DlgReg.java`) yang akan diedit.

5. **Jelaskan dampak perubahan.**
   Terangkan apa yang akan terjadi setelah kode diubah, terutama jika berkaitan dengan integrasi (*bridging*) atau modul lain yang bergantung pada kode tersebut.

6. **Jangan melakukan refactoring massal.**
   Fokus pada target spesifik. Hindari mengubah hal-hal di luar *scope* permintaan, terutama membersihkan kode lama yang berpotensi mematahkan fungsionalitas aplikasi raksasa ini.

7. **Jangan mengubah struktur database tanpa persetujuan.**
   Jika membutuhkan tabel baru, kolom baru, atau `ALTER TABLE`, tanyakan persetujuan user secara eksplisit terlebih dahulu karena dampaknya luas ke puluhan modul.

8. **Pertahankan pola coding yang sudah ada.**
   Gunakan standar yang selaras dengan *codebase*. Jika project tidak menggunakan DAO dan menggunakan pemanggilan statis JDBC (seperti class `sekuel` dan `validasi`), maka ikuti gaya tersebut. Gunakan GUI Builder (`.form`) jika diperlukan untuk menambah visual komponen agar tidak terjadi konflik.

9. **Fokus hanya pada modul yang diminta.**
   Batasi *scope* pengerjaan sesuai dengan modul target yang sedang dikerjakan agar tidak meluas secara tidak perlu.

10. **Setelah perubahan selesai, perbarui dokumentasi jika diperlukan.**
    Update file seperti `README.md`, `PROJECT_KNOWLEDGE.md`, atau dokumentasi pendukung lainnya untuk mencerminkan logika fitur terbaru.

11. **...**
    ...
