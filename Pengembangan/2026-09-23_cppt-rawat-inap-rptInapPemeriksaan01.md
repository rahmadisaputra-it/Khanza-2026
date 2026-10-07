# CPPT Rawat Inap — rptInapPemeriksaan01
Tanggal: 2026-09-23
Status: Selesai

## Tujuan
Membuat laporan CPPT (Catatan Perkembangan Pasien Terintegrasi) untuk modul Rawat Inap,
dengan layout portrait identik dengan CPPT Rawat Jalan (rptJalanPemeriksaan.jrxml).
Fitur utama: kolom Ruang di tabel CPPT diisi berdasarkan kamar pasien SAAT entri ditulis,
sehingga historis perpindahan kamar tersimpan secara alami per entri.

## File yang Diubah/Ditambah
- `src/simrskhanza/DlgRawatInap.java` — 4 titik perubahan:
  1. INSERT Admin Utama (baris ~11577): tambah `kd_kamar` (21 field, nilai dari variabel `kamar`)
  2. INSERT user biasa (baris ~11594): tambah `kd_kamar` (21 field, nilai dari variabel `kamar`)
  3. UPDATE Admin Utama (baris ~5441): tambah `,kd_kamar='"+kamar+"'` di SET clause
  4. UPDATE user biasa (baris ~5483): tambah `,kd_kamar='"+kamar+"'` di SET clause
  5. PRINT query (baris ~5067): ganti ke `rptInapPemeriksaan01.jasper`, tambah field demografi
     pasien (tgl_lahir, jk, tmp_lahir, umurdaftar, sttsumur, agama, pekerjaan, no_tlp, pnd,
     alamat, no_ktp, no_peserta), tambah `pegawai.jbtn`, `COALESCE(pemeriksaan_ranap.kd_kamar,'') as kd_kamar`
     Order by diubah menjadi ASC (tgl_perawatan, jam_rawat) agar urutan kronologis.
- `report/rptInapPemeriksaan01.jrxml` — [BARU] Template CPPT Portrait Rawat Inap:
  - Layout A4 Portrait (595x842)
  - Header demografi pasien 7 baris (No.RM, Nama, Tgl Lahir, NIK, No.BPJS, Agama, Jenis Kelamin)
    + kolom kanan (Pendidikan, Pekerjaan, No Telp, Alamat)
  - Judul: "CATATAN PERKEMBANGAN PASIEN TERINTEGRASI"
  - Tabel CPPT 5 kolom: Hari/Tgl/Jam | Ruang | Profesi | SOAP | Verifikasi DPJP
  - SOAP tanpa Lingkar Perut (tidak ada di pemeriksaan_ranap)
  - Kolom Ruang dari `$F{kd_kamar}` (bukan Poliklinik)

## Perubahan Database (perlu dijalankan manual oleh developer)
- Tabel: `pemeriksaan_ranap`
- Perubahan: `ALTER TABLE pemeriksaan_ranap ADD COLUMN kd_kamar varchar(15) DEFAULT NULL;`
- Disetujui oleh: User (persetujuan lisan di chat, 2026-09-23)

## Langkah Selanjutnya untuk Developer
1. Jalankan SQL: `ALTER TABLE pemeriksaan_ranap ADD COLUMN kd_kamar varchar(15) DEFAULT NULL;`
2. Buka `report/rptInapPemeriksaan01.jrxml` di iReport atau Jaspersoft Studio
3. Compile → akan menghasilkan `rptInapPemeriksaan01.jasper`
4. Pastikan `rptInapPemeriksaan01.jasper` ada di folder `report/`, `build/report/`, dan `dist/report/`
5. Clean & Build project di NetBeans

## Catatan/Risiko
- Data lama di `pemeriksaan_ranap` akan punya `kd_kamar = NULL` — aman, karena di query print
  sudah digunakan `COALESCE(pemeriksaan_ranap.kd_kamar,'')` sehingga tampil kosong bukan error.
- Variabel `kamar` di DlgRawatInap.java diisi via `setKamar()` dari pemanggil form (frmUtama/DlgKasirRalan).
  Jika form dibuka tanpa setKamar(), nilai `kamar` adalah string kosong — aman.
- Kode formulir di jrxml masih `FORM RM / 12.B / RSUPM` — sesuaikan manual di jrxml sesuai ketetapan RS.
- Modul terdampak: hanya DlgRawatInap.java + rptInapPemeriksaan01.jrxml. Tidak ada modul lain yang
  memakai `pemeriksaan_ranap` untuk INSERT dari UI (bridging PCare punya INSERT sendiri, tidak berubah).
