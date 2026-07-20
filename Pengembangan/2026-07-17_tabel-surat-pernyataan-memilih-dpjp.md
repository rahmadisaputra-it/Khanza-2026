# Tambah Tabel surat_pernyataan_memilih_dpjp
Tanggal: 2026-07-17
Status: Belum Dieksekusi — menunggu persetujuan & implementasi form

## Tujuan
Menyediakan tabel untuk menyimpan data formulir **"Keinginan Pasien Memilih DPJP"** (menu `MnPernyataanMemilihDPJP`) agar isian surat dapat disimpan ke database dan dicetak ulang.

## File yang Diubah/Ditambah
- Belum ada file Java yang diubah — tabel ini akan digunakan saat form input untuk `MnPernyataanMemilihDPJP` dibuat.

## Perubahan Database (jika ada)
- **Tabel**: `surat_pernyataan_memilih_dpjp`
- **Perubahan**: Tabel baru
- **Disetujui oleh**: User (2026-07-17)

### Query SQL

```sql
CREATE TABLE `surat_pernyataan_memilih_dpjp` (
  `no_pernyataan` varchar(20) NOT NULL,
  `no_rawat` varchar(17) DEFAULT NULL,
  `tanggal` date DEFAULT NULL,
  `kd_dokter` varchar(20) NOT NULL,
  `nip` varchar(20) NOT NULL,
  `pembuat_pernyataan` varchar(50) NOT NULL,
  `alamat_pembuat_pernyataan` varchar(100) NOT NULL,
  `tgl_lahir_pembuat_pernyataan` date NOT NULL,
  `jk_pembuat_pernyataan` enum('L','P') NOT NULL,
  `hubungan_pembuat_pernyataan` enum('Diri Sendiri','Istri','Suami','Kerabat','Orang Tua','Anak','Saudara Kandung','Teman','Lain-lain') NOT NULL,
  `saksi_keluarga` varchar(50) NOT NULL,
  PRIMARY KEY (`no_pernyataan`),
  KEY `no_rawat` (`no_rawat`),
  KEY `kd_dokter` (`kd_dokter`),
  KEY `nip` (`nip`),
  CONSTRAINT `surat_pernyataan_memilih_dpjp_ibfk_1` FOREIGN KEY (`no_rawat`) REFERENCES `reg_periksa` (`no_rawat`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `surat_pernyataan_memilih_dpjp_ibfk_2` FOREIGN KEY (`kd_dokter`) REFERENCES `dokter` (`kd_dokter`) ON UPDATE CASCADE,
  CONSTRAINT `surat_pernyataan_memilih_dpjp_ibfk_3` FOREIGN KEY (`nip`) REFERENCES `petugas` (`nip`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
```

## Struktur Kolom

| Kolom | Tipe | Keterangan |
|---|---|---|
| `no_pernyataan` | varchar(20) | Primary key, nomor surat |
| `no_rawat` | varchar(17) | FK → `reg_periksa.no_rawat` |
| `tanggal` | date | Tanggal surat |
| `kd_dokter` | varchar(20) | FK → `dokter.kd_dokter` (DPJP yang dipilih) |
| `nip` | varchar(20) | FK → `petugas.nip` (petugas pembuat) |
| `pembuat_pernyataan` | varchar(50) | Nama pembuat surat (pasien/keluarga) |
| `alamat_pembuat_pernyataan` | varchar(100) | Alamat pembuat |
| `tgl_lahir_pembuat_pernyataan` | date | Tanggal lahir pembuat |
| `jk_pembuat_pernyataan` | enum('L','P') | Jenis kelamin pembuat |
| `hubungan_pembuat_pernyataan` | enum(...) | Hubungan dengan pasien |
| `saksi_keluarga` | varchar(50) | Nama saksi dari keluarga |

## Catatan/Risiko
- Tabel ini memakai `CHARSET=latin1` — konsisten dengan tabel lain di schema `sik`. Jika ada nama dengan karakter khusus (misal é, ñ) pertimbangkan ganti ke `utf8mb4`.
- FK ke `reg_periksa` menggunakan `ON DELETE CASCADE` — jika data registrasi dihapus, data surat ini ikut terhapus otomatis.
- Form input Java dan file `.jasper` untuk cetak surat ini belum dibuat — perlu dikerjakan terpisah.
- Eksekusi query ini wajib di **database lokal/staging** dulu sebelum ke production.
