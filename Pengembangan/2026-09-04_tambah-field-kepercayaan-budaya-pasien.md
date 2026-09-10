# Tambah Field Kepercayaan/Budaya/Nilai Khusus di Form Pasien
Tanggal: 2026-09-04
Status: Selesai

## Tujuan
Menambahkan field "Kepercayaan / Budaya / Nilai-nilai khusus yang perlu diperhatikan" di bawah field Cacat Fisik pada form Input Pasien, sesuai permintaan user untuk kelengkapan data sosio-kultural pasien.

## File yang Diubah/Ditambah
- `src/simrskhanza/DlgPasien.java` --- Tambah:
  - Inisialisasi cmbKepercayaan (JComboBox), txtKepercayaanKet (TextBox), jLabelKepercayaan (Label)
  - Layout UI di FormInput (y=372, di bawah Cacat Fisik y=342)
  - FormInput.setPreferredSize diubah dari (560, 168) ke (560, 400)
  - Nilai resize dinamis di formWindowActivated: 530->560, 380->410, threshold 640->670, 530->560
  - Reset di emptTeks(): cmbKepercayaan.setSelectedIndex(0), txtKepercayaanKet.setText("")
  - Load data di getData(), getDataTni(), getDataPolri() via query SELECT terpisah per no_rkm_medis
  - Semua 5 instance menyimpantf/menyimpantf2 pasien: 36 -> 38 kolom, tambah 2 nilai baru di akhir

## Perubahan Database
- Tabel: pasien
- Perubahan: 2 kolom baru
- Query:
  ALTER TABLE pasien
    ADD COLUMN kepercayaan_budaya VARCHAR(50) NOT NULL DEFAULT 'Tidak Ada' AFTER cacat_fisik,
    ADD COLUMN kepercayaan_budaya_ket VARCHAR(200) NOT NULL DEFAULT '' AFTER kepercayaan_budaya;
- Disetujui oleh: user (konfirmasi "proses" pada 2026-09-04)

## Catatan/Risiko
- Field kepercayaan_budaya menggunakan dropdown statis: "Tidak Ada" | "Ada"
- Field kepercayaan_budaya_ket adalah free-text keterangan tambahan
- Load data menggunakan query terpisah (bukan dari kolom tabel tbPasien) untuk ketiga mode: umum, TNI, Polri
- ALTER TABLE harus dijalankan sebelum testing tanpa itu INSERT/UPDATE akan error
- File ini tidak menyentuh frmUtama.java
