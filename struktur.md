# Struktur Direktori SIMRS Khanza

> File ini adalah referensi cepat untuk AI Agent. Baca file ini saat perlu mengetahui letak file, modul, atau komponen tertentu sebelum melakukan perubahan kode.

---

## Root Project

```
SIMRS-Khanza/
├── src/                    # Seluruh source code Java (package per modul)
├── report/                 # Template laporan JasperReports (.jrxml & .jasper)
├── setting/                # Konfigurasi eksternal (database.xml, modem.xml, logo)
├── nbproject/              # Konfigurasi NetBeans IDE & Ant build
├── build/                  # Output kompilasi (di-generate otomatis, jangan diedit)
├── dist/                   # Output distribusi JAR (di-generate otomatis)
├── webapps/                # Modul web PHP (antrian, display, portal internal RS)
├── epasien/                # Portal pasien berbasis web (PHP)
├── edokter/                # Portal dokter berbasis web (PHP)
├── gambar/                 # Aset gambar yang dipakai runtime (foto pasien, dll)
├── suara/                  # Aset audio (notifikasi .mp3)
├── sik.sql                 # Full dump database (struktur + data awal) — KRUSIAL
├── struktur.sql            # DDL saja (CREATE TABLE tanpa data)
├── sik_bridging_lab.sql    # Tambahan tabel untuk bridging laboratorium
├── sik_bridging_radiologi.sql # Tambahan tabel untuk bridging radiologi
├── build.xml               # Ant build script utama
├── GEMINI.md               # Panduan pengembangan untuk AI Agent
└── struktur.md             # File ini — referensi struktur direktori
```

---

## `src/` — Package Java (Jumlah File .java)

| Package | Jumlah .java | Keterangan |
|---|---|---|
| `bridging` | 261 | Integrasi API eksternal (BPJS, SatuSehat, PCare, dll) |
| `rekammedis` | 240 | EMR, SOAP, asesmen klinis, formulir medis & keperawatan |
| `keuangan` | 167 | Kasir, billing, piutang, akuntansi, deposit |
| `grafikanalisa` | 148 | Grafik statistik & analisa data RS |
| `inventory` | 118 | Farmasi, stok obat, penerimaan barang |
| `laporan` | 108 | Form laporan (RL, rekap, statistik) |
| `kepegawaian` | 73 | Data SDM, jadwal, penggajian |
| `simrskhanza` | 72 | Entry point, menu utama, pendaftaran, master data |
| `surat` | 46 | Surat-menyurat RS (rujukan, keterangan, dll) |
| `fungsi` | 46 | Core utilities (DB, query, validasi, format) |
| `ipsrs` | 44 | IPSRS — pemeliharaan sarana & prasarana |
| `dapur` | 36 | Modul dapur/gizi RS |
| `toko` | 35 | Modul toko/kios internal RS |
| `widget` | 35 | Komponen UI kustom reusable |
| `inventaris` | 35 | Inventaris non-farmasi (aset, barang) |
| `setting` | 33 | Form pengaturan aplikasi |
| `viabarcode` | 24 | Integrasi barcode scanner |
| `restore` | 18 | Backup & restore database |
| `permintaan` | 17 | Alur permintaan barang antar unit |
| `perpustakaan` | 16 | Modul perpustakaan RS |
| `ziscsr` | 15 | Modul ZIS/CSR (sosial) |
| `pcraicra` | 12 | PPI (Pencegahan dan Pengendalian Infeksi) |
| `informasi` | 11 | Papan informasi / display publik |
| `tranfusidarah` | 10 | Modul transfusi darah |
| `parkir` | 3 | Modul parkir |
| `smsui` | 6 | Antarmuka layanan SMS |

---

## File Kritis di `src/` (Wajib Diketahui Sebelum Edit)

### Entry Point & Menu Utama
| File | Lokasi | Fungsi |
|---|---|---|
| `SIMRSKhanza.java` | `src/simrskhanza/` | `main()` — titik masuk aplikasi |
| `frmUtama.java` | `src/simrskhanza/` | MDI Parent, semua menu & navigasi (~2.4 MB) ⚠️ |

### Core Utilities (`src/fungsi/`)
| File | Fungsi |
|---|---|
| `koneksiDB.java` | Koneksi MySQL, baca `database.xml`, AES decrypt |
| `sekuel.java` | Wrapper eksekusi SQL (INSERT, UPDATE, SELECT) |
| `validasi.java` | Validasi input, format teks/angka/tanggal, filler combo |
| `akses.java` | Manajemen hak akses user per modul |
| `WarnaTable*.java` | Utility pewarnaan baris JTable berdasarkan kondisi |
| `cacherawatjalan.java` | Cache data sesi rawat jalan |
| `cacherawatinap.java` | Cache data sesi rawat inap |
| `cacheigd.java` | Cache data sesi IGD |

### Form Pelayanan Utama (`src/simrskhanza/`)
| File | Fungsi |
|---|---|
| `DlgReg.java` | Dialog pendaftaran pasien baru |
| `DlgRawatJalan.java` | Form pelayanan rawat jalan |
| `DlgRawatInap.java` | Form pelayanan rawat inap |
| `DlgIGD.java` | Form pelayanan IGD |
| `DlgPasien.java` | Master data pasien |

### Keuangan (`src/keuangan/`)
| File | Fungsi |
|---|---|
| `DlgBilingRalan.java` | Billing rawat jalan |
| `DlgBilingRanap.java` | Billing rawat inap |
| `DlgAkunPiutang.java` | Manajemen piutang |
| `DlgDeposit.java` | Deposit pasien |

### Bridging (`src/bridging/`)
| File / Prefix | Fungsi |
|---|---|
| `ApiBPJS.java` | API utama BPJS VClaim |
| `ApiSatuSehat.java` | API SatuSehat Kemenkes |
| `ApiPcare.java` | API PCare FKTP |
| `BPJS*.java` | Operasi VClaim (SEP, rujukan, klaim, dll) |
| `SatuSehat*.java` | Kirim data FHIR ke SatuSehat |
| `PCare*.java` | Operasi PCare (pendaftaran, tindakan, obat) |
| `koneksiDB*.java` | Koneksi ke DB sistem LIS eksternal (ELIMS, SLIMS, dll) |

---

## `setting/` — Konfigurasi Runtime
| File | Fungsi |
|---|---|
| `database.xml` | Koneksi DB (host, port, dbname, user, password — AES encrypted) |
| `database.ini` | Versi alternatif konfigurasi DB |
| `modem.xml` | Konfigurasi SMS gateway |
| `logo.jpg` | Logo RS yang tampil di aplikasi |
| `wallpaper.jpg` | Wallpaper form utama |

---

## `webapps/` — Modul Web PHP
Sub-modul utama yang ada di `webapps/`:
- Antrian farmasi, antrian loket, antrian laborat, antrian radiologi
- Tampilan bed/kamar rawat inap
- Billing, rekam medis web, radiologi
- Presensi pegawai, jadwal operasi, jadwal dokter

## `epasien/` — Portal Pasien (Web)
Antarmuka web untuk pasien: cek antrian, hasil pemeriksaan, informasi.

## `edokter/` — Portal Dokter (Web)
Antarmuka web untuk dokter: jadwal, rekam medis ringkas.

---

## Tabel Database Krusial (`sik.sql`)
| Tabel | Fungsi |
|---|---|
| `pasien` | Master demografi pasien |
| `reg_periksa` | Pendaftaran/kunjungan (Ralan, Ranap, IGD) |
| `rawat_jl_dr` | Tindakan dokter rawat jalan |
| `rawat_inap_dr` | Tindakan dokter rawat inap |
| `kamar_inap` | Alokasi tempat tidur |
| `resep_obat` | Header resep obat |
| `detail_pemberian_obat` | Detail pemberian obat per resep |
| `tagihan_sadewa` | Tagihan pasien (billing) |
| `piutang_pasien` | Piutang RS terhadap pasien |
| `nota_bayar` | Nota pembayaran |
| `bridging_sep` | Data SEP BPJS yang sudah dikirim |
| `bridging_satusehat` | Log pengiriman data ke SatuSehat |

---

## Companion Projects (Sub-project Terpisah di Root)
| Direktori | Fungsi |
|---|---|
| `KhanzaAntrianLoket/` | Aplikasi display antrian loket (standalone) |
| `KhanzaAntrianPoli/` | Aplikasi display antrian poli |
| `KhanzaAntrianApotek/` | Aplikasi display antrian apotek |
| `KhanzaHMSAnjungan/` | Kios anjungan mandiri pasien |
| `KhanzaHMSServiceSatuSehat/` | Service daemon SatuSehat |
| `KhanzaHMSServicePCare/` | Service daemon PCare |
| `KhanzaSecurity16bit/` | Library enkripsi AES internal |
| `KhanzaUpdater/` | Auto-updater aplikasi |
| `KhanzaPengenkripsiTeks/` | Tool enkripsi teks untuk `database.xml` |
