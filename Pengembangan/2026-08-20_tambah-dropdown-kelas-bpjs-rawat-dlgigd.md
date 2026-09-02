# Tambah Dropdown Kelas BPJS dan Kelas Rawat di DlgIGD
Tanggal: 2026-08-20
Status: Selesai

## Tujuan
Menambahkan komponen dropdown (ComboBox) dan Label teks untuk pilihan Kelas BPJS dan Kelas Rawat pada form IGD (DlgIGD.java), tepat di sebelah kanan field Asal Rujukan.

## File yang Diubah/Ditambah
- src/simrskhanza/DlgIGD.java —
  - Menginisialisasi CmbKelasBPJS, jLabelKelasBPJS, CmbKelasRawat, dan jLabelKelasRawat.
  - Menambahkan model pilihan VIP, Kelas 1, Kelas 2, Kelas 3.
  - Menambahkan keempat komponen ke layout absolute FormInput di sumbu X & Y (setBounds).
  - Mengupdate logic simpan (isRegistrasi) menjadi 21 parameter SQL array.
  - Mengupdate logic edit (ganti) dengan menambahkan parameter kelas_bpjs dan kelas_rawat.

## Perubahan Database
- Tidak ada ALTER TABLE karena kolom kelas_bpjs dan kelas_rawat sudah ditambahkan pengguna sebelumnya secara lokal.

## Catatan/Risiko
Perubahan UI di-inject manual ke initComponents karena file DlgIGD.form tidak ditemukan. Sumbu X digeser agar label tidak tertimpa combobox. Jika ke depannya form diregenerate, script ini berpotensi rusak.