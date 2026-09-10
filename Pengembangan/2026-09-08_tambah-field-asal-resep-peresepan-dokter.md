# Tambah Field Asal Resep di Form Peresepan Dokter
Tanggal: 2026-09-08
Status: Selesai

## Tujuan
Menambahkan field "Asal Resep" (label + combobox) di form DlgPeresepanDokter agar dokter bisa mencatat asal ruangan resep, dan menyimpannya ke tabel resep_obat.

## File yang Diubah/Ditambah
- `src/inventory/DlgPeresepanDokter.java` — penambahan komponen UI + perbaikan bug

## Perubahan Database
- Tabel: `resep_obat`
- Kolom baru: `asal_resep VARCHAR(50) NOT NULL DEFAULT "-"`
- Query:
  ALTER TABLE resep_obat ADD COLUMN asal_resep VARCHAR(50) NOT NULL DEFAULT "-";

## Ringkasan Perubahan Kode
Komponen ditambahkan manual oleh developer di dalam blok initComponents:
- `jLabelAsalResep` — label "Asal Resep:" di FormInput, bounds (680,70,72,23)
- `cmbAsalResep` — combobox isi: -, HCU, ICU, Isolasi, Marwah, Mina, Multazam, Musdalifah, NICU, Observasi IGD, Perinatologi, PICU, Raudah, Shafa, Tan''im, VK, OK

Perbaikan bug (dilakukan AI):
1. SQL INSERT (7x): cmbDtk1 dikembalikan sebagai detik jam; cmbAsalResep jadi param ke-11; 10->11
2. setNoRm(): cmbAsalResep->cmbDtk1 untuk setSelectedItem(detik)
3. jam() method: cmbAsalResep->cmbDtk1 untuk getSelectedIndex dan setSelectedItem
4. TANGGALMUNDUR block: cmbAsalResep->cmbDtk1 di setEnabled(false)
5. Keyboard nav: cmbMnt->cmbDtk1, cmbDtk1->cmbAsalResep, cmbAsalResep prev=cmbDtk1

## Flow Keyboard
DTPBeri -> cmbJam -> cmbMnt -> cmbDtk1 -> cmbAsalResep -> NoResep -> KdDokter

## Catatan/Risiko
- cmbDtk di-rename jadi cmbDtk1 oleh developer — konsistensi perlu dijaga
- Jalankan ALTER TABLE di DB lokal dulu sebelum testing
- Test end-to-end: pilih asal resep -> simpan -> cek tabel resep_obat kolom asal_resep
