# 🧠 Project Knowledge: SIMRS Khanza (2026 Clone)

Dokumen ini berisi hasil analisa komprehensif terhadap source code project SIMRS Khanza lokal untuk membantu proses development lebih lanjut.

## 1. Gambaran Umum Aplikasi
SIMRS Khanza adalah Sistem Informasi Manajemen Rumah Sakit berbasis Desktop (Java Swing). Aplikasi ini menerapkan model Monolithic Client-Server (Desktop application terkoneksi langsung ke *database server* tersentralisasi). Sistem ini tidak hanya berfokus pada pelayanan medis inti, tapi mencakup *billing*, *inventory*, bridging (BPJS, SatuSehat, dsb), hingga modul-modul pendukung (parkir, dapur, IPSRS).

## 2. Struktur Folder Penting
- `src/`: Berisi seluruh *source code* Java (GUI `.java`, `.form`, dan `.jrxml` untuk *reporting*).
- `lib/`: Kumpulan file `.jar` atau dependensi pihak ketiga.
- `setting/`: (Di root folder) Berisi file konfigurasi eksternal, terutama `database.xml` untuk koneksi.
- `nbproject/`: File *project definition* untuk NetBeans dan Ant Build (`project.properties`, `build.xml`).
- `webapps/`, `epasien/`, `edokter/`: Modul aplikasi tambahan berbasis web yang mendampingi *desktop app*.
- `sik.sql`: *Dump* file database utama yang membentuk struktur tabel sistem.

## 3. Teknologi dan Library yang Digunakan
- **Core**: Java SE 8.
- **UI Framework**: Java Swing (dibantu plugin WindowBuilder/Matisse NetBeans), FlatLaf (tema UI modern), JavaFX.
- **Database**: MySQL/MariaDB.
- **Database Access**: JDBC Driver langsung (tanpa ORM besar seperti Hibernate secara menyeluruh, meskipun `ejb3-persistence.jar` ada, mayoritas query adalah *native* SQL).
- **Reporting**: JasperReports (versi 5.1.2 & 6.8.0) dipadukan dengan iReport.
- **API/HTTP**: Apache HttpClient, Jackson, Gson (untuk *bridging* data JSON).
- **Security**: AES Encryption internal (`KhanzaSecurity16bit.jar`).
- **Build System**: Apache Ant.

## 4. Entry Point Aplikasi
- **File**: `src/simrskhanza/SIMRSKhanza.java`
- **Penjelasan**: Merupakan *class* yang mengandung metode `public static void main(String[] args)`. Di dalamnya, antrean thread Swing (`WidgetUtilities.invokeLater`) memanggil instance dari form utama (`frmUtama`).

## 5. Arsitektur Aplikasi
Arsitektur yang digunakan adalah **2-Tier Client-Server / Thick Client**:
1. **Presentation & Business Logic Layer**: Semua logika GUI, validasi (*business rule*), dan event handling tergabung di dalam satu layer besar pada file GUI (`.java` file di bawah package modul seperti `simrskhanza` atau `rekammedis`).
2. **Data Access Layer**: Akses data ke MySQL dialirkan melalui class utilitas statis. Tidak ada sistem DAO (Data Access Object) yang sangat terpisah/ketat.
3. **Database Layer**: Server MySQL (skema `sik`).

## 6. Konfigurasi Database
- **File Konfigurasi**: `setting/database.xml` (berada di luar *source folder* tapi di dalam *root project*).
- **Handler/Parser Code**: `src/fungsi/koneksiDB.java`.
- **Mekanisme**: Properti database seperti `HOST`, `DATABASE`, `PORT`, `USER`, dan `PAS` disimpan dalam bentuk *encrypted string* (dienkripsi menggunakan AES). `koneksiDB.java` akan membaca `database.xml`, melakukan dekripsi menggunakan `AESsecurity.EnkripsiAES`, dan membangun `MysqlDataSource`.

## 7. Modul-Modul Utama Beserta Fungsinya
Berada di dalam struktur folder `src/`:
- **`simrskhanza`**: Modul pusat operasional. Menampung form-form penting seperti IGD, Rawat Jalan (`DlgRawatJalan.java`), Rawat Inap, Registrasi (`DlgReg.java`), dan Master Data.
- **`rekammedis`**: Menampung formulir SOAP, riwayat pasien, asesmen awal, dan pelaporan medis.
- **`keuangan`**: Berisi manajemen kasir, piutang, tagihan, hingga jurnal akuntansi keuangan RS.
- **`bridging`**: Menangani koneksi *outbound* ke layanan BPJS (VClaim, PCare), INA-CBG, SatuSehat Kemenkes, dan vendor pihak ketiga lainnya (misal: LIS Laboratorium).
- **`inventory` / `inventaris`**: Menangani sistem pergudangan, obat apotek, dan barang non-medis.
- **`fungsi`**: Pusat core logic dan *helper*, memuat koneksi DB, validasi input, template warna UI, hingga eksekusi query.

## 8. Alur Data Antar Modul
1. Pengguna berinteraksi melalui Form GUI (misal: `simrskhanza/DlgReg.java`).
2. Input divalidasi oleh method yang ada di file `src/fungsi/validasi.java`.
3. Setelah valid, GUI akan memanggil rutin eksekusi SQL melalui `src/fungsi/sekuel.java`.
4. `sekuel.java` akan meminta instance *connection* ke `src/fungsi/koneksiDB.java`.
5. Data ditulis ke MySQL (atau dibaca untuk dirender ke dalam `JTable` melalui event listener).
6. Alur untuk *bridging* akan mengonversi `ResultSet` menjadi JSON (via Jackson/Gson) dan ditembak melalui Apache HttpClient di dalam package `bridging`.

## 9. Tabel Database yang Digunakan
Database sangat besar (menggunakan schema hasil dari `sik.sql`). Beberapa tabel krusial:
- `pasien`: Menyimpan demografi/master pasien.
- `reg_periksa`: Menyimpan data pendaftaran/kunjungan (Rawat Inap/Jalan/IGD).
- `rawat_jl_dr`, `rawat_inap_dr`: Transaksi tindakan dokter di Ralan/Ranap.
- `kamar_inap`: Logik alokasi tempat tidur pasien.
- `resep_obat`, `detail_pemberian_obat`: Data farmasi & pengeluaran obat.
- `tagihan_sadewa`, `piutang_pasien`: Data keuangan/billing.

## 10. File Source Code Penting Beserta Fungsinya
- **`src/simrskhanza/frmUtama.java`**: Container utama / MDI Parent dari seluruh window aplikasi. Menangani *menu routing* navigasi ke seluruh modul. Berukuran sangat masif (~2.4MB kode).
- **`src/fungsi/koneksiDB.java`**: Menghandle *connection pooling* manual dan pembacaan XML database.
- **`src/fungsi/sekuel.java`**: *Wrapper* kelas utilitas eksekusi query (`insert`, `update`, `delete`, pencarian data spesifik) untuk meminimalisir duplikasi *boilerplate* JDBC.
- **`src/fungsi/validasi.java`**: File utilitas *all-in-one* untuk format teks, angka, validasi tanggal, auto-numbering, combo box filler, dll.

## 11. Cara Build dan Menjalankan Aplikasi
1. Buka project menggunakan **Apache NetBeans** (Rekomendasi v11 - v20+).
2. Pastikan JDK diset ke **JDK 8**.
3. Di tab *Projects* NetBeans, klik kanan pada project SIMRS-Khanza lalu pilih **Clean and Build** (ant akan mengeksekusi kompilasi berdasarkan `build.xml`).
4. Jalankan aplikasi dengan memilih **Run Project** (atau Shift+F6 pada `SIMRSKhanza.java`).
5. Alternatif eksekusi melalui *command line*: `java -jar dist/SIMRSKhanza-2026.jar`.

## 12. Area Kode yang Kompleks / Perlu Perhatian
1. **`frmUtama.java`**: File ini terhubung dengan hampir seluruh fitur di aplikasi. Modifikasi menu, navigasi, atau tab utama akan mempengaruhi file raksasa ini secara langsung. *Compile time* pada file ini akan sedikit lebih lama.
2. **Kriptografi & Bridging**: Header autentikasi untuk *bridging* (seperti `X-cons-id`, `X-Timestamp`, `X-Signature`) menggunakan dekripsi statik AES. Perubahan kunci (Secret Key/Cons ID) harus diperhatikan di dalam `setting/database.xml` dan dikelola menggunakan tool encryptor bawaan Khanza.
3. **Hardcoded UI Logics**: Mayoritas GUI dibangun via GUI Builder. Menambahkan komponen secara manual ke dalam kode (tanpa GUI Builder) bisa tertimpa oleh *form regeneration*. Selalu gunakan editor *Design* dari NetBeans IDE (file `.form`) bila memodifikasi antarmuka visual.
4. **Dependensi Database Global**: Karena sistem sangat minim abstraksi objek, struktur data mengandalkan string query statik (SQL murni). Setiap adanya *Alter Table* pada database harus diiringi *Find and Replace* kode SQL di puluhan file `.java` yang memanggil tabel tersebut.
