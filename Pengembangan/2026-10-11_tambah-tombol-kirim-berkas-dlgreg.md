# Tambah Tombol Kirim Berkas di Registrasi (DlgReg)
Tanggal: 2026-10-11
Status: Selesai

## Tujuan
Mempercepat proses mutasi berkas rekam medis dari bagian pendaftaran/registrasi rawat jalan tanpa harus membuka web companion/jendela monitoring berkas secara terpisah.

## File yang Diubah/Ditambah
- `src/simrskhanza/DlgReg.java` — Menambahkan tombol `BtnKirimBerkas` di panel bawah (`panelGlass6`) dan event handler untuk memproses mutasi berkas pasien terpilih (`status = 'Sudah Dikirim'`, `dikirim = now()`) ke tabel `mutasi_berkas`.

## Perubahan Database (jika ada)
- Tidak ada perubahan skema database (menggunakan tabel existing `mutasi_berkas`).

## Catatan/Risiko
- Tombol mengeksekusi operasi upsert ke tabel `mutasi_berkas`.
- Memerlukan baris pasien yang dipilih di tabel registrasi (`TNoRw` tidak boleh kosong).
