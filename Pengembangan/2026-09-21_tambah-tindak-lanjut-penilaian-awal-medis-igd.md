# Penambahan Tindak Lanjut pada Pengkajian Awal Medis IGD
Tanggal: 2026-09-21
Status: Selesai

## Tujuan
Menambahkan opsi tindak lanjut pasien IGD berupa pilihan CheckBox: Pulang, Rawat, Pulang Paksa, dan Rujuk pada form Pengkajian Awal Medis IGD (`RMPenilaianAwalMedisIGD.java`).

## File yang Diubah/Ditambah
- `src/rekammedis/RMPenilaianAwalMedisIGD.form` — Komponen CheckBox (`chkPulang`, `chkRawat`, `chkPulangPaksa`, `chkRujuk`) ditambahkan oleh user via NetBeans GUI Builder, serta diatur `opaque=false` dan `focusPainted=false` agar transparan (hanya kotak dan teks).
- `src/rekammedis/RMPenilaianAwalMedisIGD.java` —
  - Pengaturan `setOpaque(false)` dan `setFocusPainted(false)` pada keempat CheckBox di constructor agar menyatu dengan latar belakang form.
  - Penambahan kolom `"Tindak Lanjut"` pada `tabMode` (total kolom dari 41 menjadi 42) dan pengaturan lebar kolom index 41.
  - Penambahan event listener mutual exclusion pada `chkPulang`, `chkRawat`, `chkPulangPaksa`, dan `chkRujuk` agar hanya satu opsi yang dapat tercentang.
  - Penyesuaian method `emptTeks()` untuk mereset status centang checkbox (`setSelected(false)`).
  - Penyesuaian method `getData()` untuk membaca status tindak lanjut dari tabel (kolom 41) ke form.
  - Penyesuaian query `tampil()` (SELECT query) dan pemetaan baris pada `tabMode.addRow()`.
  - Penyesuaian query UPDATE dan parameter pada `ganti()` (38 parameter).
  - Penyesuaian query INSERT dan parameter pada `simpan()` (37 parameter).
  - Penyesuaian laporan HTML `BtnPrintActionPerformed` untuk menambahkan kolom Tindak Lanjut.
  - Penyesuaian query report Jasper pada `MnPenilaianMedisActionPerformed`.
- `report/rptCetakPenilaianAwalMedisIGD.jrxml` — 
  - Menambahkan deklarasi field `no_surat`, `umurdaftar`, `sttsumur`, `tmp_lahir`, dan memperbarui `<queryString>` agar header identitas pasien terisi saat dicetak.
  - Menambahkan kolom/label Tindak Lanjut pada Band 13 (menyatu dengan area tanda tangan).
  - Memperbaiki kotak Band 12 (Tatalaksana) dengan menambahkan `isPrintWhenDetailOverflows="true"` pada garis kiri, kanan, serta menambahkan garis horizontal penutup atas agar ketika teks tatalaksana meluap ke halaman 2, teks tetap berada di dalam kotak tertutup dan rapi.
- `struktur.sql` — Penambahan kolom `tindak_lanjut` pada definisi tabel `penilaian_medis_igd`.

## Perubahan Database (jika ada)
- Tabel: `penilaian_medis_igd`
- Perubahan:
  ```sql
  ALTER TABLE `penilaian_medis_igd` 
  ADD COLUMN `tindak_lanjut` ENUM('Pulang','Rawat','Pulang Paksa','Rujuk','-') NOT NULL DEFAULT '-' AFTER `tata`;
  ```
- Disetujui oleh: user (dikonfirmasi pada 2026-09-21)

## Catatan/Risiko
- Modul lain yang mengakses tabel `penilaian_medis_igd` (`RMRiwayatPerawatan`, `DlgIGD`, `DlgKasirRalan`, `DlgReg`) tidak terpengaruh karena menggunakan penyebutan kolom eksplisit atau hanya mengubah `no_rawat`.
- Jika template cetak Jasper (`rptCetakPenilaianAwalMedisIGD.jrxml`) ingin menampilkan pilihan tindak lanjut pada lembar PDF cetak, perlu ditambahkan field `tindak_lanjut` ke dalam desain JRXML template tersebut.
