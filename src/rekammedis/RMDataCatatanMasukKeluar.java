/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rekammedis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.nio.file.Files;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import laporan.DlgBerkasRawat;
import laporan.DlgDiagnosaPenyakit;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.apache.hc.core5.http.io.entity.StringEntity;


/**
 *
 * @author perpustakaan
 */
public final class RMDataCatatanMasukKeluar extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps,ps2;
    private ResultSet rs,rs2;
    private int i=0;    
    private DlgCariDokter dokter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean ceksukses = false;
    private String kodekamar="",namakamar="",tglkeluar="",jamkeluar="",finger="",json;
    private ObjectMapper mapper= new ObjectMapper();
    private JsonNode root;
    
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMDataCatatanMasukKeluar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        WindowURLSertisign.setSize(570,100);
        WindowPhrase.setSize(320,100);
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.RM","Nama Pasien","Tgl.Masuk","Jam Masuk","Tgl.Keluar","Jam Keluar",
            "Diagnosa Awal","Diagnosa Akhir","Stts Pulang",
            "Masuk Melalui","Cara Masuk","Kd.Dokter Konsultan","Nama Dokter Konsultan",
            "Nama Tindakan/Operasi","Jenis Anastesi","Golongan Operasi",
            "Infeksi Nosokomial","Penyebab Infeksi","Pemeriksaan Fisik",
            "Hasil USG","Hasil EKG","Hasil Rontgen","Hasil Lab",
            "Imunisasi Masuk","Imunisasi Rawat","Transfusi Darah (cc)",
            "Rujukan Dari","Alamat Perujuk","Tempat Tugas Perujuk","Telp Perujuk",
            "Cara Rujukan","Catatan Rujukan","Keadaan Pulang","Ket Keadaan Pulang","Cara Pulang","Ket Cara Pulang","Catatan"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 38; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){ column.setPreferredWidth(120); }
            else if(i==1){ column.setPreferredWidth(80); }
            else if(i==2){ column.setPreferredWidth(150); }
            else if(i==3||i==5){ column.setPreferredWidth(80); }
            else if(i==4||i==6){ column.setPreferredWidth(70); }
            else if(i==7||i==8){ column.setPreferredWidth(150); }
            else if(i==9){ column.setPreferredWidth(100); }
            else if(i==10||i==11){ column.setPreferredWidth(130); }
            else if(i==12){ column.setPreferredWidth(80); }
            else if(i==13){ column.setPreferredWidth(150); }
            else if(i==27){ column.setPreferredWidth(130); }
            else if(i==28||i==29){ column.setPreferredWidth(150); }
            else if(i==30){ column.setPreferredWidth(90); }
            else if(i==31||i==32){ column.setPreferredWidth(120); }
            else if(i==33||i==34){ column.setPreferredWidth(100); }
            else if(i==35){ column.setPreferredWidth(150); }
            else if(i==36){ column.setPreferredWidth(100); }
            else if(i==37){ column.setPreferredWidth(150); }
            else{ column.setPreferredWidth(120); }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        DiagnosaAwal.setDocument(new batasInput((int)100).getKata(DiagnosaAwal));
        DiagnosaAkhir.setDocument(new batasInput((int)100).getKata(DiagnosaAkhir));
        StsPulang.setDocument(new batasInput((int)50).getKata(StsPulang));
        KdDokterKonsultan.setDocument(new batasInput((byte)20).getKata(KdDokterKonsultan));
        NamaTindakanOperasi.setDocument(new batasInput((int)2000).getKata(NamaTindakanOperasi));
        JenisAnastesi.setDocument(new batasInput((int)100).getKata(JenisAnastesi));
        GolonganOperasi.setDocument(new batasInput((int)100).getKata(GolonganOperasi));
        PenyebabInfeksi.setDocument(new batasInput((int)100).getKata(PenyebabInfeksi));
        PemeriksaanFisik.setDocument(new batasInput((int)2000).getKata(PemeriksaanFisik));
        HasilUSG.setDocument(new batasInput((int)2000).getKata(HasilUSG));
        HasilEKG.setDocument(new batasInput((int)2000).getKata(HasilEKG));
        HasilRontgen.setDocument(new batasInput((int)2000).getKata(HasilRontgen));
        HasilLab.setDocument(new batasInput((int)2000).getKata(HasilLab));
        ImunisasiMasuk.setDocument(new batasInput((int)50).getKata(ImunisasiMasuk));
        ImunisasiRawat.setDocument(new batasInput((int)50).getKata(ImunisasiRawat));
        TransfusiDarahCC.setDocument(new batasInput((int)10).getKata(TransfusiDarahCC));
        RujukanDari.setDocument(new batasInput((int)100).getKata(RujukanDari));
        AlamatPerujuk.setDocument(new batasInput((int)200).getKata(AlamatPerujuk));
        TempatTugasPerujuk.setDocument(new batasInput((int)100).getKata(TempatTugasPerujuk));
        TelpPerujuk.setDocument(new batasInput((int)30).getKata(TelpPerujuk));
        CatatanRM.setDocument(new batasInput((int)2000).getKata(CatatanRM));
        KetCaraPulang.setDocument(new batasInput((int)100).getKata(KetCaraPulang));
        
        TCari.setDocument(new batasInput((int)100).getKata(TCari));
        
        ChkInput.setSelected(false);
        isForm();
      
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnCetakLembarKeluarMasuk = new javax.swing.JMenuItem();
        WindowURLSertisign = new javax.swing.JDialog();
        internalFrame9 = new widget.InternalFrame();
        jLabel43 = new widget.Label();
        panelisi6 = new widget.panelisi();
        BtnCloseUrl = new widget.Button();
        BtnBukaURL = new widget.Button();
        jLabel44 = new widget.Label();
        URLSertisign = new widget.TextBox();
        BtnDownloadFile = new widget.Button();
        BtnDownloadBukaFile = new widget.Button();
        WindowPhrase = new javax.swing.JDialog();
        internalFrame8 = new widget.InternalFrame();
        jLabel45 = new widget.Label();
        panelisi5 = new widget.panelisi();
        BtnClosePhrase = new widget.Button();
        BtnSimpanTandaTangan = new widget.Button();
        jLabel46 = new widget.Label();
        Phrase = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        TNoRM = new widget.TextBox();
        jLabel5 = new widget.Label();
        label14 = new widget.Label();
        KdDokterKonsultan = new widget.TextBox();
        NmDokterKonsultan = new widget.TextBox();
        jLabel36 = new widget.Label();
        CaraPulang = new widget.ComboBox();
        jLabel16 = new widget.Label();
        MasukMelalui = new widget.TextBox();
        jLabel17 = new widget.Label();
        Keluar = new widget.TextBox();
        jLabel18 = new widget.Label();
        JamMasuk = new widget.TextBox();
        jLabel20 = new widget.Label();
        JamKeluar = new widget.TextBox();
        jLabel24 = new widget.Label();
        DiagnosaAwal = new widget.TextBox();
        PenyebabInfeksi = new widget.TextBox();
        jLabel25 = new widget.Label();
        DiagnosaAkhir = new widget.TextBox();
        jLabel38 = new widget.Label();
        jScrollPane1 = new javax.swing.JScrollPane();
        PemeriksaanFisik = new javax.swing.JTextArea();
        KetCaraPulang = new widget.TextBox();
        jLabel39 = new widget.Label();
        jLabel40 = new widget.Label();
        JenisAnastesi = new widget.TextBox();
        jLabel41 = new widget.Label();
        jLabel42 = new widget.Label();
        GolonganOperasi = new widget.TextBox();
        jLabel47 = new widget.Label();
        jScrollPane2 = new javax.swing.JScrollPane();
        NamaTindakanOperasi = new javax.swing.JTextArea();
        jLabel48 = new widget.Label();
        HasilUSG = new widget.TextBox();
        jLabel49 = new widget.Label();
        HasilEKG = new widget.TextBox();
        jLabel50 = new widget.Label();
        HasilRontgen = new widget.TextBox();
        jLabel51 = new widget.Label();
        HasilLab = new widget.TextBox();
        jLabel52 = new widget.Label();
        ImunisasiMasuk = new widget.TextBox();
        jLabel53 = new widget.Label();
        ImunisasiRawat = new widget.TextBox();
        jLabel54 = new widget.Label();
        CatatanRM = new widget.TextBox();
        jLabel55 = new widget.Label();
        TransfusiDarahCC = new widget.TextBox();
        jLabel56 = new widget.Label();
        RujukanDari = new widget.TextBox();
        jLabel57 = new widget.Label();
        AlamatPerujuk = new widget.TextBox();
        jLabel58 = new widget.Label();
        TelpPerujuk = new widget.TextBox();
        jLabel59 = new widget.Label();
        TempatTugasPerujuk = new widget.TextBox();
        InfeksiNosokomial1 = new widget.ComboBox();
        jLabel22 = new widget.Label();
        jLabel23 = new widget.Label();
        CaraMasuk = new widget.ComboBox();
        jLabel26 = new widget.Label();
        StsPulang = new widget.TextBox();
        jLabel60 = new widget.Label();
        CatatanRujukan = new widget.TextBox();
        jLabel61 = new widget.Label();
        KeadaanPulang = new widget.ComboBox();
        KetKeadaanPulang = new widget.TextBox();
        jLabel62 = new widget.Label();
        CaraRujukan = new widget.TextBox();
        Masuk1 = new widget.TextBox();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnCetakLembarKeluarMasuk.setBackground(new java.awt.Color(255, 255, 254));
        MnCetakLembarKeluarMasuk.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnCetakLembarKeluarMasuk.setForeground(new java.awt.Color(50, 50, 50));
        MnCetakLembarKeluarMasuk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnCetakLembarKeluarMasuk.setLabel("Cetak Lembar Keluar Masuk");
        MnCetakLembarKeluarMasuk.setName("MnCetakLembarKeluarMasuk"); // NOI18N
        MnCetakLembarKeluarMasuk.setPreferredSize(new java.awt.Dimension(250, 26));
        MnCetakLembarKeluarMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnCetakLembarKeluarMasukActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnCetakLembarKeluarMasuk);

        WindowURLSertisign.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowURLSertisign.setModal(true);
        WindowURLSertisign.setName("WindowURLSertisign"); // NOI18N
        WindowURLSertisign.setUndecorated(true);
        WindowURLSertisign.setResizable(false);

        internalFrame9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ URL File Hasil Tanda Tangan Sertisign ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame9.setName("internalFrame9"); // NOI18N
        internalFrame9.setLayout(new java.awt.BorderLayout());

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setText("%");
        jLabel43.setName("jLabel43"); // NOI18N
        internalFrame9.add(jLabel43, java.awt.BorderLayout.CENTER);

        panelisi6.setName("panelisi6"); // NOI18N
        panelisi6.setPreferredSize(new java.awt.Dimension(100, 44));
        panelisi6.setLayout(null);

        BtnCloseUrl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnCloseUrl.setMnemonic('T');
        BtnCloseUrl.setText("Tutup");
        BtnCloseUrl.setToolTipText("Alt+T");
        BtnCloseUrl.setName("BtnCloseUrl"); // NOI18N
        BtnCloseUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseUrlActionPerformed(evt);
            }
        });
        panelisi6.add(BtnCloseUrl);
        BtnCloseUrl.setBounds(450, 40, 100, 30);

        BtnBukaURL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnBukaURL.setMnemonic('B');
        BtnBukaURL.setText("Buka URL");
        BtnBukaURL.setToolTipText("Alt+B");
        BtnBukaURL.setName("BtnBukaURL"); // NOI18N
        BtnBukaURL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBukaURLActionPerformed(evt);
            }
        });
        panelisi6.add(BtnBukaURL);
        BtnBukaURL.setBounds(10, 40, 105, 30);

        jLabel44.setText("URL :");
        jLabel44.setName("jLabel44"); // NOI18N
        panelisi6.add(jLabel44);
        jLabel44.setBounds(0, 10, 40, 23);

        URLSertisign.setEditable(false);
        URLSertisign.setHighlighter(null);
        URLSertisign.setName("URLSertisign"); // NOI18N
        panelisi6.add(URLSertisign);
        URLSertisign.setBounds(44, 10, 505, 23);

        BtnDownloadFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnDownloadFile.setMnemonic('D');
        BtnDownloadFile.setText("Download File");
        BtnDownloadFile.setToolTipText("Alt+D");
        BtnDownloadFile.setName("BtnDownloadFile"); // NOI18N
        BtnDownloadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDownloadFileActionPerformed(evt);
            }
        });
        panelisi6.add(BtnDownloadFile);
        BtnDownloadFile.setBounds(125, 40, 130, 30);

        BtnDownloadBukaFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/preview-16x16.png"))); // NOI18N
        BtnDownloadBukaFile.setMnemonic('F');
        BtnDownloadBukaFile.setText("Download & Buka File");
        BtnDownloadBukaFile.setToolTipText("Alt+F");
        BtnDownloadBukaFile.setName("BtnDownloadBukaFile"); // NOI18N
        BtnDownloadBukaFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDownloadBukaFileActionPerformed(evt);
            }
        });
        panelisi6.add(BtnDownloadBukaFile);
        BtnDownloadBukaFile.setBounds(265, 40, 175, 30);

        internalFrame9.add(panelisi6, java.awt.BorderLayout.CENTER);

        WindowURLSertisign.getContentPane().add(internalFrame9, java.awt.BorderLayout.CENTER);

        WindowPhrase.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowPhrase.setModal(true);
        WindowPhrase.setName("WindowPhrase"); // NOI18N
        WindowPhrase.setUndecorated(true);
        WindowPhrase.setResizable(false);

        internalFrame8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ E-Sign / Tanda Tangan Elektronik ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame8.setName("internalFrame8"); // NOI18N
        internalFrame8.setLayout(new java.awt.BorderLayout());

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel45.setText("%");
        jLabel45.setName("jLabel45"); // NOI18N
        internalFrame8.add(jLabel45, java.awt.BorderLayout.CENTER);

        panelisi5.setName("panelisi5"); // NOI18N
        panelisi5.setPreferredSize(new java.awt.Dimension(100, 44));
        panelisi5.setLayout(null);

        BtnClosePhrase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnClosePhrase.setMnemonic('U');
        BtnClosePhrase.setText("Batal");
        BtnClosePhrase.setToolTipText("Alt+U");
        BtnClosePhrase.setName("BtnClosePhrase"); // NOI18N
        BtnClosePhrase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnClosePhraseActionPerformed(evt);
            }
        });
        panelisi5.add(BtnClosePhrase);
        BtnClosePhrase.setBounds(200, 40, 100, 30);

        BtnSimpanTandaTangan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpanTandaTangan.setMnemonic('S');
        BtnSimpanTandaTangan.setText("Simpan");
        BtnSimpanTandaTangan.setToolTipText("Alt+S");
        BtnSimpanTandaTangan.setName("BtnSimpanTandaTangan"); // NOI18N
        BtnSimpanTandaTangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanTandaTanganActionPerformed(evt);
            }
        });
        panelisi5.add(BtnSimpanTandaTangan);
        BtnSimpanTandaTangan.setBounds(10, 40, 100, 30);

        jLabel46.setText("Masukkan Passphrase :");
        jLabel46.setName("jLabel46"); // NOI18N
        panelisi5.add(jLabel46);
        jLabel46.setBounds(0, 10, 130, 23);

        Phrase.setHighlighter(null);
        Phrase.setName("Phrase"); // NOI18N
        panelisi5.add(Phrase);
        Phrase.setBounds(134, 10, 160, 23);

        internalFrame8.add(panelisi5, java.awt.BorderLayout.CENTER);

        WindowPhrase.getContentPane().add(internalFrame8, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Keluar Masuk Pasien Rawat Inap ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setMinimumSize(new java.awt.Dimension(550, 165));
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setPreferredSize(new java.awt.Dimension(550, 771));
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setAutoCreateRowSorter(true);
        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setComponentPopupMenu(jPopupMenu1);
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(LCount);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tgl.Rawat :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-08-2026" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-08-2026" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(310, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('3');
        BtnCari.setToolTipText("Alt+3");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnAll);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 448));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Input Data");
        ChkInput.setToolTipText("Alt+I");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        scrollInput.setName("scrollInput"); // NOI18N

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 100));
        FormInput.setLayout(null);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(104, 10, 141, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienKeyPressed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(361, 10, 450, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(247, 10, 112, 23);

        jLabel5.setText("No.Rawat :");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(0, 10, 100, 23);

        label14.setText("Dokter Konsultan :");
        label14.setName("label14"); // NOI18N
        label14.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label14);
        label14.setBounds(0, 40, 100, 23);

        KdDokterKonsultan.setEditable(false);
        KdDokterKonsultan.setName("KdDokterKonsultan"); // NOI18N
        KdDokterKonsultan.setPreferredSize(new java.awt.Dimension(80, 23));
        KdDokterKonsultan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdDokterKonsultanKeyPressed(evt);
            }
        });
        FormInput.add(KdDokterKonsultan);
        KdDokterKonsultan.setBounds(104, 40, 100, 23);

        NmDokterKonsultan.setEditable(false);
        NmDokterKonsultan.setName("NmDokterKonsultan"); // NOI18N
        NmDokterKonsultan.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NmDokterKonsultan);
        NmDokterKonsultan.setBounds(206, 40, 200, 23);

        jLabel36.setText("Golongan Operasi :");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(390, 220, 100, 23);

        CaraPulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Diizinkan Dokter", "Permintaan Pasien", "Pindah RS Lain", "Meninggal", "Pulang Paksa", "Lari", "Dirujuk Ke RS Lain" }));
        CaraPulang.setName("CaraPulang"); // NOI18N
        CaraPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CaraPulangKeyPressed(evt);
            }
        });
        FormInput.add(CaraPulang);
        CaraPulang.setBounds(110, 190, 140, 23);

        jLabel16.setText("Masuk Melalui :");
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 70, 100, 23);

        MasukMelalui.setEditable(false);
        MasukMelalui.setHighlighter(null);
        MasukMelalui.setName("MasukMelalui"); // NOI18N
        FormInput.add(MasukMelalui);
        MasukMelalui.setBounds(110, 70, 120, 23);

        jLabel17.setText("Tanggal Keluar :");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(0, 130, 100, 23);

        Keluar.setEditable(false);
        Keluar.setHighlighter(null);
        Keluar.setName("Keluar"); // NOI18N
        FormInput.add(Keluar);
        Keluar.setBounds(110, 130, 80, 23);

        jLabel18.setText("Jam Masuk :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(190, 100, 70, 23);

        JamMasuk.setEditable(false);
        JamMasuk.setHighlighter(null);
        JamMasuk.setName("JamMasuk"); // NOI18N
        FormInput.add(JamMasuk);
        JamMasuk.setBounds(270, 100, 70, 23);

        jLabel20.setText("Jam Keluar :");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(190, 130, 70, 23);

        JamKeluar.setEditable(false);
        JamKeluar.setHighlighter(null);
        JamKeluar.setName("JamKeluar"); // NOI18N
        FormInput.add(JamKeluar);
        JamKeluar.setBounds(270, 130, 70, 23);

        jLabel24.setText("Diagnosa Awal :");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(370, 100, 120, 23);

        DiagnosaAwal.setHighlighter(null);
        DiagnosaAwal.setName("DiagnosaAwal"); // NOI18N
        DiagnosaAwal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaAwalKeyPressed(evt);
            }
        });
        FormInput.add(DiagnosaAwal);
        DiagnosaAwal.setBounds(495, 100, 315, 23);

        PenyebabInfeksi.setHighlighter(null);
        PenyebabInfeksi.setName("PenyebabInfeksi"); // NOI18N
        PenyebabInfeksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PenyebabInfeksiKeyPressed(evt);
            }
        });
        FormInput.add(PenyebabInfeksi);
        PenyebabInfeksi.setBounds(760, 220, 140, 23);

        jLabel25.setText("Diagnosa Akhir :");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(370, 130, 120, 23);

        DiagnosaAkhir.setHighlighter(null);
        DiagnosaAkhir.setName("DiagnosaAkhir"); // NOI18N
        DiagnosaAkhir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaAkhirKeyPressed(evt);
            }
        });
        FormInput.add(DiagnosaAkhir);
        DiagnosaAkhir.setBounds(495, 130, 315, 23);

        jLabel38.setText("Cara Pulang :");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(0, 190, 100, 23);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        PemeriksaanFisik.setColumns(20);
        PemeriksaanFisik.setRows(5);
        PemeriksaanFisik.setName("PemeriksaanFisik"); // NOI18N
        jScrollPane1.setViewportView(PemeriksaanFisik);

        FormInput.add(jScrollPane1);
        jScrollPane1.setBounds(110, 300, 270, 60);

        KetCaraPulang.setHighlighter(null);
        KetCaraPulang.setName("KetCaraPulang"); // NOI18N
        KetCaraPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetCaraPulangKeyPressed(evt);
            }
        });
        FormInput.add(KetCaraPulang);
        KetCaraPulang.setBounds(260, 190, 125, 23);

        jLabel39.setText("Pemeriksaan Fisik :");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(0, 300, 100, 23);

        jLabel40.setText("Tindakan Operasi :");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput.add(jLabel40);
        jLabel40.setBounds(390, 190, 100, 23);

        JenisAnastesi.setHighlighter(null);
        JenisAnastesi.setName("JenisAnastesi"); // NOI18N
        JenisAnastesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JenisAnastesiKeyPressed(evt);
            }
        });
        FormInput.add(JenisAnastesi);
        JenisAnastesi.setBounds(500, 190, 140, 23);

        jLabel41.setText("Infeksi Nosokomial :");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(640, 190, 110, 23);

        jLabel42.setText("Penyebab Infeksi :");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(640, 220, 110, 23);

        GolonganOperasi.setHighlighter(null);
        GolonganOperasi.setName("GolonganOperasi"); // NOI18N
        GolonganOperasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GolonganOperasiKeyPressed(evt);
            }
        });
        FormInput.add(GolonganOperasi);
        GolonganOperasi.setBounds(500, 220, 140, 23);

        jLabel47.setText("Tindakan Operasi :");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(0, 230, 100, 23);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        NamaTindakanOperasi.setColumns(20);
        NamaTindakanOperasi.setRows(5);
        NamaTindakanOperasi.setName("NamaTindakanOperasi"); // NOI18N
        jScrollPane2.setViewportView(NamaTindakanOperasi);

        FormInput.add(jScrollPane2);
        jScrollPane2.setBounds(110, 230, 270, 60);

        jLabel48.setText("Hasil USG :");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(390, 250, 100, 23);

        HasilUSG.setHighlighter(null);
        HasilUSG.setName("HasilUSG"); // NOI18N
        HasilUSG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilUSGKeyPressed(evt);
            }
        });
        FormInput.add(HasilUSG);
        HasilUSG.setBounds(500, 250, 140, 23);

        jLabel49.setText("Hasil EKG :");
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput.add(jLabel49);
        jLabel49.setBounds(390, 280, 100, 23);

        HasilEKG.setHighlighter(null);
        HasilEKG.setName("HasilEKG"); // NOI18N
        HasilEKG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilEKGKeyPressed(evt);
            }
        });
        FormInput.add(HasilEKG);
        HasilEKG.setBounds(500, 280, 140, 23);

        jLabel50.setText("Hasil Rontgen :");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(650, 250, 100, 23);

        HasilRontgen.setHighlighter(null);
        HasilRontgen.setName("HasilRontgen"); // NOI18N
        HasilRontgen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilRontgenKeyPressed(evt);
            }
        });
        FormInput.add(HasilRontgen);
        HasilRontgen.setBounds(760, 250, 140, 23);

        jLabel51.setText("Hasil Lab :");
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput.add(jLabel51);
        jLabel51.setBounds(650, 280, 100, 23);

        HasilLab.setHighlighter(null);
        HasilLab.setName("HasilLab"); // NOI18N
        HasilLab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilLabKeyPressed(evt);
            }
        });
        FormInput.add(HasilLab);
        HasilLab.setBounds(760, 280, 140, 23);

        jLabel52.setText("Imunisasi Masuk :");
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput.add(jLabel52);
        jLabel52.setBounds(390, 310, 100, 23);

        ImunisasiMasuk.setHighlighter(null);
        ImunisasiMasuk.setName("ImunisasiMasuk"); // NOI18N
        ImunisasiMasuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ImunisasiMasukKeyPressed(evt);
            }
        });
        FormInput.add(ImunisasiMasuk);
        ImunisasiMasuk.setBounds(500, 310, 140, 23);

        jLabel53.setText("Imunisasi Rawat :");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(390, 340, 100, 23);

        ImunisasiRawat.setHighlighter(null);
        ImunisasiRawat.setName("ImunisasiRawat"); // NOI18N
        ImunisasiRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ImunisasiRawatKeyPressed(evt);
            }
        });
        FormInput.add(ImunisasiRawat);
        ImunisasiRawat.setBounds(500, 340, 140, 23);

        jLabel54.setText("Catatan RM :");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(650, 340, 100, 23);

        CatatanRM.setHighlighter(null);
        CatatanRM.setName("CatatanRM"); // NOI18N
        CatatanRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CatatanRMKeyPressed(evt);
            }
        });
        FormInput.add(CatatanRM);
        CatatanRM.setBounds(760, 340, 140, 23);

        jLabel55.setText("Transfusi Darah :");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(650, 310, 100, 23);

        TransfusiDarahCC.setHighlighter(null);
        TransfusiDarahCC.setName("TransfusiDarahCC"); // NOI18N
        TransfusiDarahCC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TransfusiDarahCCKeyPressed(evt);
            }
        });
        FormInput.add(TransfusiDarahCC);
        TransfusiDarahCC.setBounds(760, 310, 140, 23);

        jLabel56.setText("Rujukan Dari :");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(930, 190, 100, 23);

        RujukanDari.setHighlighter(null);
        RujukanDari.setName("RujukanDari"); // NOI18N
        RujukanDari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RujukanDariKeyPressed(evt);
            }
        });
        FormInput.add(RujukanDari);
        RujukanDari.setBounds(1040, 190, 140, 23);

        jLabel57.setText("Alamat Perujuk :");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(920, 220, 110, 23);

        AlamatPerujuk.setHighlighter(null);
        AlamatPerujuk.setName("AlamatPerujuk"); // NOI18N
        AlamatPerujuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlamatPerujukKeyPressed(evt);
            }
        });
        FormInput.add(AlamatPerujuk);
        AlamatPerujuk.setBounds(1040, 220, 140, 23);

        jLabel58.setText("Telp Perujuk :");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(940, 280, 90, 23);

        TelpPerujuk.setHighlighter(null);
        TelpPerujuk.setName("TelpPerujuk"); // NOI18N
        TelpPerujuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TelpPerujukKeyPressed(evt);
            }
        });
        FormInput.add(TelpPerujuk);
        TelpPerujuk.setBounds(1040, 280, 140, 23);

        jLabel59.setText("Temp. Tugas Perujuk :");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(910, 250, 120, 23);

        TempatTugasPerujuk.setHighlighter(null);
        TempatTugasPerujuk.setName("TempatTugasPerujuk"); // NOI18N
        TempatTugasPerujuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TempatTugasPerujukKeyPressed(evt);
            }
        });
        FormInput.add(TempatTugasPerujuk);
        TempatTugasPerujuk.setBounds(1040, 250, 140, 23);

        InfeksiNosokomial1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        InfeksiNosokomial1.setName("InfeksiNosokomial1"); // NOI18N
        InfeksiNosokomial1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                InfeksiNosokomial1KeyPressed(evt);
            }
        });
        FormInput.add(InfeksiNosokomial1);
        InfeksiNosokomial1.setBounds(760, 190, 90, 23);

        jLabel22.setText("Tanggal Masuk :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(0, 100, 100, 23);

        jLabel23.setText("Cara Masuk :");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(240, 70, 70, 23);

        CaraMasuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Datang Sendiri", "Dikirim Dokter/Bidan dari Luar", "Dari Puskesmas", "Dari RS Lain", "Instansi Lain", "Kasus Polisi" }));
        CaraMasuk.setName("CaraMasuk"); // NOI18N
        CaraMasuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CaraMasukKeyPressed(evt);
            }
        });
        FormInput.add(CaraMasuk);
        CaraMasuk.setBounds(320, 70, 120, 23);

        jLabel26.setText("Status Pulang :");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(370, 160, 120, 23);

        StsPulang.setHighlighter(null);
        StsPulang.setName("StsPulang"); // NOI18N
        StsPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StsPulangKeyPressed(evt);
            }
        });
        FormInput.add(StsPulang);
        StsPulang.setBounds(500, 160, 315, 23);

        jLabel60.setText("Catatan Rujukan :");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(910, 340, 120, 23);

        CatatanRujukan.setHighlighter(null);
        CatatanRujukan.setName("CatatanRujukan"); // NOI18N
        CatatanRujukan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CatatanRujukanKeyPressed(evt);
            }
        });
        FormInput.add(CatatanRujukan);
        CatatanRujukan.setBounds(1040, 340, 140, 23);

        jLabel61.setText("Keadaan Pulang :");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(0, 160, 100, 23);

        KeadaanPulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sembuh", "Membaik", "Belum Sembuh", "Exitus < 48 Jam", "Exitus > 48 Jam" }));
        KeadaanPulang.setName("KeadaanPulang"); // NOI18N
        KeadaanPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeadaanPulangKeyPressed(evt);
            }
        });
        FormInput.add(KeadaanPulang);
        KeadaanPulang.setBounds(110, 160, 140, 23);

        KetKeadaanPulang.setHighlighter(null);
        KetKeadaanPulang.setName("KetKeadaanPulang"); // NOI18N
        KetKeadaanPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetKeadaanPulangKeyPressed(evt);
            }
        });
        FormInput.add(KetKeadaanPulang);
        KetKeadaanPulang.setBounds(260, 160, 125, 23);

        jLabel62.setText("Cara Rujukan :");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput.add(jLabel62);
        jLabel62.setBounds(910, 310, 120, 23);

        CaraRujukan.setHighlighter(null);
        CaraRujukan.setName("CaraRujukan"); // NOI18N
        CaraRujukan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CaraRujukanKeyPressed(evt);
            }
        });
        FormInput.add(CaraRujukan);
        CaraRujukan.setBounds(1040, 310, 140, 23);

        Masuk1.setEditable(false);
        Masuk1.setHighlighter(null);
        Masuk1.setName("Masuk1"); // NOI18N
        FormInput.add(Masuk1);
        Masuk1.setBounds(110, 100, 80, 23);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
        }else{            
            Valid.pindah(evt,TCari,NamaTindakanOperasi);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt,TCari,BtnSimpan);
}//GEN-LAST:event_TPasienKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")||TNoRM.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(KdDokterKonsultan.getText().trim().equals("")||NmDokterKonsultan.getText().trim().equals("")){
            Valid.textKosong(KdDokterKonsultan,"Dokter Penanggung Jawab");
        }else{
            if(Sequel.menyimpantf("catatan_masuk_keluar","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 28, new String[]{
                TNoRw.getText(),
                MasukMelalui.getText().toString(),
                CaraMasuk.getSelectedItem().toString(),
                KdDokterKonsultan.getText(),
                NamaTindakanOperasi.getText(),
                JenisAnastesi.getText(),
                GolonganOperasi.getText(),
                InfeksiNosokomial1.getSelectedItem().toString(),
                PenyebabInfeksi.getText(),
                PemeriksaanFisik.getText(),
                HasilUSG.getText(),
                HasilEKG.getText(),
                HasilRontgen.getText(),
                HasilLab.getText(),
                ImunisasiMasuk.getText(),
                ImunisasiRawat.getText(),
                TransfusiDarahCC.getText(),
                RujukanDari.getText(),
                AlamatPerujuk.getText(),
                TempatTugasPerujuk.getText(),
                TelpPerujuk.getText(),
                CaraRujukan.getText(),
                CatatanRujukan.getText(),
                KeadaanPulang.getSelectedItem().toString(),
                KetKeadaanPulang.getText(),
                CaraPulang.getSelectedItem().toString(),
                KetCaraPulang.getText(),
                CatatanRM.getText()
            })==true){
                tabMode.addRow(new Object[]{
                    TNoRw.getText(), TNoRM.getText(), TPasien.getText(),
                    MasukMelalui.getText(), JamMasuk.getText(), Keluar.getText(), JamKeluar.getText(),
                    DiagnosaAwal.getText(), DiagnosaAkhir.getText(), StsPulang.getText(),
                    MasukMelalui.getText().toString(), CaraMasuk.getSelectedItem().toString(),
                    KdDokterKonsultan.getText(), NmDokterKonsultan.getText(),
                    NamaTindakanOperasi.getText(), JenisAnastesi.getText(), GolonganOperasi.getText(),
                    InfeksiNosokomial1.getSelectedItem().toString(), PenyebabInfeksi.getText(), PemeriksaanFisik.getText(),
                    HasilUSG.getText(), HasilEKG.getText(), HasilRontgen.getText(), HasilLab.getText(),
                    ImunisasiMasuk.getText(), ImunisasiRawat.getText(), TransfusiDarahCC.getText(),
                    RujukanDari.getText(), AlamatPerujuk.getText(), TempatTugasPerujuk.getText(), TelpPerujuk.getText(),
                    CaraRujukan.getText(), CatatanRujukan.getText(), KeadaanPulang.getSelectedItem().toString(), KetKeadaanPulang.getText(),
                    CaraPulang.getSelectedItem().toString(), KetCaraPulang.getText(), CatatanRM.getText()
                });
                LCount.setText(""+tabMode.getRowCount());
                emptTeks();
            }
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,CatatanRM,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
        ChkInput.setSelected(true);
        isForm(); 
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else{
                if(KdDokterKonsultan.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString())){
                    hapus();
                }else{
                    JOptionPane.showMessageDialog(null,"Hanya bisa dihapus oleh dokter yang bersangkutan..!!");
                }
            }
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }            
            
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TNoRw.getText().equals("")||TNoRM.getText().equals("")||TPasien.getText().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(KdDokterKonsultan.getText().equals("")||NmDokterKonsultan.getText().equals("")){
            Valid.textKosong(KdDokterKonsultan,"Dokter Penanggung Jawab");
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(KdDokterKonsultan.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString())){
                        ganti();
                    }else{
                        JOptionPane.showMessageDialog(null,"Hanya bisa diganti oleh dokter yang bersangkutan..!!");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(! TCari.getText().trim().equals("")){
            BtnCariActionPerformed(evt);
        }
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            Map<String, Object> param = new HashMap<>(); 
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            Valid.MyReportqry("rptDataResumePasienRanap.jasper","report","::[ Data Resume Pasien ]::",
                    "select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,catatan_masuk_keluar.kd_dokter,dokter.nm_dokter,reg_periksa.kd_dokter as kodepengirim,pengirim.nm_dokter as pengirim,"+
                    "reg_periksa.tgl_registrasi,reg_periksa.jam_reg,catatan_masuk_keluar.diagnosa_awal,catatan_masuk_keluar.alasan,catatan_masuk_keluar.keluhan_utama,catatan_masuk_keluar.pemeriksaan_fisik,"+
                    "catatan_masuk_keluar.jalannya_penyakit,catatan_masuk_keluar.pemeriksaan_penunjang,catatan_masuk_keluar.hasil_laborat,catatan_masuk_keluar.tindakan_dan_operasi,catatan_masuk_keluar.obat_di_rs,"+
                    "catatan_masuk_keluar.diagnosa_utama,catatan_masuk_keluar.kd_diagnosa_utama,catatan_masuk_keluar.diagnosa_sekunder,catatan_masuk_keluar.kd_diagnosa_sekunder,catatan_masuk_keluar.diagnosa_sekunder2,"+
                    "catatan_masuk_keluar.kd_diagnosa_sekunder2,catatan_masuk_keluar.diagnosa_sekunder3,catatan_masuk_keluar.kd_diagnosa_sekunder3,catatan_masuk_keluar.diagnosa_sekunder4,"+
                    "catatan_masuk_keluar.kd_diagnosa_sekunder4,catatan_masuk_keluar.prosedur_utama,catatan_masuk_keluar.kd_prosedur_utama,catatan_masuk_keluar.prosedur_sekunder,catatan_masuk_keluar.kd_prosedur_sekunder,"+
                    "catatan_masuk_keluar.prosedur_sekunder2,catatan_masuk_keluar.kd_prosedur_sekunder2,catatan_masuk_keluar.prosedur_sekunder3,catatan_masuk_keluar.kd_prosedur_sekunder3,catatan_masuk_keluar.alergi,"+
                    "catatan_masuk_keluar.diet,catatan_masuk_keluar.lab_belum,catatan_masuk_keluar.edukasi,catatan_masuk_keluar.cara_keluar,catatan_masuk_keluar.ket_keluar,catatan_masuk_keluar.keadaan,"+
                    "catatan_masuk_keluar.ket_keadaan,catatan_masuk_keluar.dilanjutkan,catatan_masuk_keluar.ket_dilanjutkan,catatan_masuk_keluar.kontrol,catatan_masuk_keluar.obat_pulang "+
                    "from catatan_masuk_keluar inner join reg_periksa on catatan_masuk_keluar.no_rawat=reg_periksa.no_rawat inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter on catatan_masuk_keluar.kd_dokter=dokter.kd_dokter inner join dokter as pengirim on reg_periksa.kd_dokter=pengirim.kd_dokter "+
                    "where reg_periksa.tgl_registrasi between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+
                    (TCari.getText().trim().equals("")?"":"and (reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                    "catatan_masuk_keluar.kd_dokter like '%"+TCari.getText().trim()+"%' or dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or catatan_masuk_keluar.keadaan like '%"+TCari.getText().trim()+"%' or "+
                    "catatan_masuk_keluar.kd_diagnosa_utama like '%"+TCari.getText().trim()+"%' or catatan_masuk_keluar.diagnosa_utama like '%"+TCari.getText().trim()+"%' or "+
                    "catatan_masuk_keluar.prosedur_utama like '%"+TCari.getText().trim()+"%' or reg_periksa.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                    "catatan_masuk_keluar.kd_prosedur_utama like '%"+TCari.getText().trim()+"%')")+"order by reg_periksa.tgl_registrasi,reg_periksa.status_lanjut",param);
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        runBackground(() ->tampil());
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        runBackground(() ->tampil());
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            runBackground(() ->tampil());
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
}//GEN-LAST:event_TNoRMKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SPACE){
                try {
                    ChkInput.setSelected(true);
                    isForm(); 
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void KdDokterKonsultanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDokterKonsultanKeyPressed
        Valid.pindah(evt,TCari,NamaTindakanOperasi);
    }//GEN-LAST:event_KdDokterKonsultanKeyPressed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void CaraPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CaraPulangKeyPressed
        Valid.pindah(evt,KetCaraPulang,PenyebabInfeksi);
    }//GEN-LAST:event_CaraPulangKeyPressed

    private void DiagnosaAwalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaAwalKeyPressed
        Valid.pindah(evt,TCari,CatatanRM);
    }//GEN-LAST:event_DiagnosaAwalKeyPressed

    private void PenyebabInfeksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PenyebabInfeksiKeyPressed
        Valid.pindah(evt,CaraPulang,KetCaraPulang);
    }//GEN-LAST:event_PenyebabInfeksiKeyPressed

    private void BtnCloseUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseUrlActionPerformed
        URLSertisign.setText("");
        WindowURLSertisign.dispose();
    }//GEN-LAST:event_BtnCloseUrlActionPerformed

    private void BtnBukaURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBukaURLActionPerformed
        try {
            Desktop.getDesktop().browse(new URI(URLSertisign.getText()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane,"File belum tersedia, silahkan tunggu beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnBukaURLActionPerformed

    private void BtnDownloadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDownloadFileActionPerformed
        try {
            URL url = new URL(URLSertisign.getText());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("Resume"+TNoRw.getText().trim().replaceAll("/","")+".pdf");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
            System.out.println("Download Selesai : " + "Resume"+TNoRw.getText().trim().replaceAll("/","")+".pdf");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane,"File belum tersedia, silahkan tunggu & ulangi beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnDownloadFileActionPerformed

    private void BtnDownloadBukaFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDownloadBukaFileActionPerformed
        try {
            URL url = new URL(URLSertisign.getText());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("Resume"+TNoRw.getText().trim().replaceAll("/","")+".pdf");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
            System.out.println("Download Selesai : " + "Resume"+TNoRw.getText().trim().replaceAll("/","")+".pdf");
            Desktop.getDesktop().browse(new File("Resume"+TNoRw.getText().trim().replaceAll("/","")+".pdf").toURI());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane,"File belum tersedia, silahkan tunggu & ulangi beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnDownloadBukaFileActionPerformed

    private void BtnClosePhraseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnClosePhraseActionPerformed
        Phrase.setText("");
        WindowPhrase.dispose();
    }//GEN-LAST:event_BtnClosePhraseActionPerformed

    private void BtnSimpanTandaTanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanTandaTanganActionPerformed
        if(Phrase.getText().equals("")){
            Valid.textKosong(Phrase,"Phrase");
        }else{
            if(tbObat.getSelectedRow()>-1){
                Map<String, Object> param = new HashMap<>();    
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());   
                param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                param.put("norawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                param.put("finger","#"); 
                try {
                    ps=koneksi.prepareStatement("select dpjp_ranap.kd_dokter,dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat=? and dpjp_ranap.kd_dokter<>?");
                    try {
                        ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                        ps.setString(2,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
                        rs=ps.executeQuery();
                        i=2;
                        while(rs.next()){
                           if(i==2){
                               finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("kd_dokter"));
                               param.put("finger2","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nm_dokter")+"\nID "+(finger.equals("")?rs.getString("kd_dokter"):finger)+"\n"+Valid.SetTgl3(Keluar.getText()));
                               param.put("namadokter2",rs.getString("nm_dokter")); 
                           }
                           if(i==3){
                               finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("kd_dokter"));
                               param.put("finger3","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nm_dokter")+"\nID "+(finger.equals("")?rs.getString("kd_dokter"):finger)+"\n"+Valid.SetTgl3(Keluar.getText()));
                               param.put("namadokter3",rs.getString("nm_dokter")); 
                           }
                           i++;
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : "+e);
                    } finally{
                        if(rs!=null){
                            rs.close();
                        }
                        if(ps!=null){
                            ps.close();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notif : "+e);
                }
                param.put("ruang","");
                param.put("tanggalkeluar",Valid.SetTgl3(Keluar.getText()));
                param.put("jamkeluar",JamKeluar.getText());
                Valid.MyReportPDF2("rptLaporanResumeRanap2.jasper","report","::[ Laporan Resume Pasien ]::",param);
                File f = new File("./report/rptLaporanResumeRanap2.pdf");  
                try {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpPost post = new HttpPost(koneksiDB.URLAKSESFILEESIGN());
                    post.setHeader("Content-Type", "application/json");
                    post.addHeader("username", koneksiDB.USERNAMEAPIESIGN());
                    post.addHeader("password", koneksiDB.PASSAPIESIGN());
                    post.addHeader("url", koneksiDB.URLAPIESIGN());
                    
                    byte[] fileContent = Files.readAllBytes(f.toPath());
                    
                    json="{" +
                             "\"file\":\""+Base64.getEncoder().encodeToString(fileContent)+"\"," +
                             "\"nik\":\""+Sequel.cariIsi("select pegawai.no_ktp from pegawai where pegawai.nik=?", akses.getkode())+"\"," +
                             "\"passphrase\":\""+Phrase.getText()+"\"," +
                             "\"tampilan\":\"visible\"," +
                             "\"image\":\"false\"," +
                             "\"linkQR\":\"Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+" dan ditandatangani secara elektronik oleh "+NmDokterKonsultan.getText()+" ID "+KdDokterKonsultan.getText()+" Tanggal "+Valid.SetTgl3(Keluar.getText())+"\"," +
                             "\"width\":\"55\"," +
                             "\"height\":\"55\"," +
                             "\"tag_koordinat\":\"#\"" +
                          "}";
                    
                    System.out.println("URL Akses file :"+koneksiDB.URLAKSESFILEESIGN());
                    System.out.println("JSON Dikirim :"+json);
                    post.setEntity(new StringEntity(json));
                    try (CloseableHttpResponse response = httpClient.execute(post)) {
                        System.out.println("Response Status : " + response.getCode());
                        json=EntityUtils.toString(response.getEntity());
                        root = mapper.readTree(json);
                        if (response.getCode() == 200) {
                            try (FileOutputStream fos = new FileOutputStream(new File("Resume"+TNoRw.getText().trim().replaceAll("/","")+"_"+TNoRM.getText().trim().replaceAll(" ","")+"_"+TPasien.getText().trim().replaceAll(" ","")+".pdf"))) {
                                byte[] fileBytes = Base64.getDecoder().decode(root.path("response").asText());
                                fos.write(fileBytes);
                                WindowPhrase.dispose();
                                JOptionPane.showMessageDialog(null,"Proses tanda tangan berhasil...");
                                Desktop.getDesktop().browse(new File("Resume"+TNoRw.getText().trim().replaceAll("/","")+"_"+TNoRM.getText().trim().replaceAll(" ","")+"_"+TPasien.getText().trim().replaceAll(" ","")+".pdf").toURI());
                            } catch (Exception e) {
                                WindowPhrase.dispose();
                                JOptionPane.showMessageDialog(null,"Gagal mengkonversi base64 ke file...");
                                System.out.println("Notif : " +e);
                            }
                        } else {
                            WindowPhrase.dispose();
                            JOptionPane.showMessageDialog(null,"Code : "+root.path("metadata").path("code").asText()+" Pesan : "+root.path("metadata").path("message").asText());
                        }
                    } catch (IOException a) {
                        System.out.println("Notifikasi : " + a);
                        WindowPhrase.dispose();
                        JOptionPane.showMessageDialog(null,""+a);
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : " + e);
                    WindowPhrase.dispose();
                    JOptionPane.showMessageDialog(null,""+e);
                }
            }
        }
    }//GEN-LAST:event_BtnSimpanTandaTanganActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        runBackground(() ->tampil());
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        runBackground(() ->tampil());
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        runBackground(() ->tampil());
                    }
                }
            });
        }
    }//GEN-LAST:event_formWindowOpened

    private void DiagnosaAkhirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaAkhirKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DiagnosaAkhirKeyPressed

    private void KetCaraPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetCaraPulangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KetCaraPulangKeyPressed

    private void JenisAnastesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JenisAnastesiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JenisAnastesiKeyPressed

    private void GolonganOperasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GolonganOperasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_GolonganOperasiKeyPressed

    private void HasilUSGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilUSGKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_HasilUSGKeyPressed

    private void HasilEKGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilEKGKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_HasilEKGKeyPressed

    private void HasilRontgenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilRontgenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_HasilRontgenKeyPressed

    private void HasilLabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilLabKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_HasilLabKeyPressed

    private void ImunisasiMasukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ImunisasiMasukKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ImunisasiMasukKeyPressed

    private void ImunisasiRawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ImunisasiRawatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ImunisasiRawatKeyPressed

    private void CatatanRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CatatanRMKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CatatanRMKeyPressed

    private void TransfusiDarahCCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TransfusiDarahCCKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TransfusiDarahCCKeyPressed

    private void RujukanDariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RujukanDariKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RujukanDariKeyPressed

    private void AlamatPerujukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlamatPerujukKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_AlamatPerujukKeyPressed

    private void TelpPerujukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TelpPerujukKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TelpPerujukKeyPressed

    private void TempatTugasPerujukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TempatTugasPerujukKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TempatTugasPerujukKeyPressed

    private void InfeksiNosokomial1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InfeksiNosokomial1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_InfeksiNosokomial1KeyPressed

    private void CaraMasukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CaraMasukKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CaraMasukKeyPressed

    private void StsPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StsPulangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_StsPulangKeyPressed

    private void CatatanRujukanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CatatanRujukanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CatatanRujukanKeyPressed

    private void KeadaanPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeadaanPulangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KeadaanPulangKeyPressed

    private void KetKeadaanPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetKeadaanPulangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KetKeadaanPulangKeyPressed

    private void CaraRujukanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CaraRujukanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CaraRujukanKeyPressed

    private void MnCetakLembarKeluarMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnCetakLembarKeluarMasukActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            param.put("logo",Sequel.cariGambar("select setting.logo from setting"));
            param.put("norawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            String nmDokterMenerima = "";
            String kdDokterMenerima = "";
            finger = "";
            try {
                java.sql.PreparedStatement psk = koneksi.prepareStatement("select reg_periksa.kd_dokter,dokter.nm_dokter from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter where reg_periksa.no_rawat=?");
                try {
                    psk.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    java.sql.ResultSet rsk = psk.executeQuery();
                    if(rsk.next()){
                        kdDokterMenerima = rsk.getString("kd_dokter");
                        nmDokterMenerima = rsk.getString("nm_dokter");
                        finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",kdDokterMenerima);
                        if(finger.equals("")){
                            finger=kdDokterMenerima;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notif : "+e);
                } finally {
                    if(psk!=null) psk.close();
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            }
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+nmDokterMenerima+"\nID "+finger+"\n"+Valid.SetTgl3(Keluar.getText()));
            try {
                ps=koneksi.prepareStatement("select dpjp_ranap.kd_dokter,dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat=? and dpjp_ranap.kd_dokter<>?");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    ps.setString(2,kdDokterMenerima);
                    rs=ps.executeQuery();
                    i=2;
                    while(rs.next()){
                        if(i==2){
                            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("kd_dokter"));
                            param.put("finger2","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nm_dokter")+"\nID "+(finger.equals("")?rs.getString("kd_dokter"):finger)+"\n"+Valid.SetTgl3(Keluar.getText()));
                            param.put("namadokter2",rs.getString("nm_dokter"));
                        }
                        if(i==3){
                            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("kd_dokter"));
                            param.put("finger3","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nm_dokter")+"\nID "+(finger.equals("")?rs.getString("kd_dokter"):finger)+"\n"+Valid.SetTgl3(Keluar.getText()));
                            param.put("namadokter3",rs.getString("nm_dokter"));
                        }
                        i++;
                    }
                } catch (Exception e) {
                    System.out.println("Notif : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(ps!=null){
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            }
            param.put("ruang","");
            param.put("tanggalkeluar",Valid.SetTgl3(Keluar.getText()));
            param.put("jamkeluar",JamKeluar.getText());
            param.put("no_rawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            Valid.MyReport("rptLembarKeluarMasuk3.jasper","report","::[ Lembar Keluar Masuk ]::",param);
        }
    }//GEN-LAST:event_MnCetakLembarKeluarMasukActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMDataCatatanMasukKeluar dialog = new RMDataCatatanMasukKeluar(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.TextBox AlamatPerujuk;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnBukaURL;
    private widget.Button BtnCari;
    private widget.Button BtnClosePhrase;
    private widget.Button BtnCloseUrl;
    private widget.Button BtnDownloadBukaFile;
    private widget.Button BtnDownloadFile;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.Button BtnSimpanTandaTangan;
    private widget.ComboBox CaraMasuk;
    private widget.ComboBox CaraPulang;
    private widget.TextBox CaraRujukan;
    private widget.TextBox CatatanRM;
    private widget.TextBox CatatanRujukan;
    private widget.CekBox ChkInput;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextBox DiagnosaAkhir;
    private widget.TextBox DiagnosaAwal;
    private widget.PanelBiasa FormInput;
    private widget.TextBox GolonganOperasi;
    private widget.TextBox HasilEKG;
    private widget.TextBox HasilLab;
    private widget.TextBox HasilRontgen;
    private widget.TextBox HasilUSG;
    private widget.TextBox ImunisasiMasuk;
    private widget.TextBox ImunisasiRawat;
    private widget.ComboBox InfeksiNosokomial1;
    private widget.TextBox JamKeluar;
    private widget.TextBox JamMasuk;
    private widget.TextBox JenisAnastesi;
    private widget.TextBox KdDokterKonsultan;
    private widget.ComboBox KeadaanPulang;
    private widget.TextBox Keluar;
    private widget.TextBox KetCaraPulang;
    private widget.TextBox KetKeadaanPulang;
    private widget.Label LCount;
    private widget.TextBox Masuk1;
    private widget.TextBox MasukMelalui;
    private javax.swing.JMenuItem MnCetakLembarKeluarMasuk;
    private javax.swing.JTextArea NamaTindakanOperasi;
    private widget.TextBox NmDokterKonsultan;
    private javax.swing.JPanel PanelInput;
    private javax.swing.JTextArea PemeriksaanFisik;
    private widget.TextBox PenyebabInfeksi;
    private widget.TextBox Phrase;
    private widget.TextBox RujukanDari;
    private widget.ScrollPane Scroll;
    private widget.TextBox StsPulang;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TelpPerujuk;
    private widget.TextBox TempatTugasPerujuk;
    private widget.TextBox TransfusiDarahCC;
    private widget.TextBox URLSertisign;
    private javax.swing.JDialog WindowPhrase;
    private javax.swing.JDialog WindowURLSertisign;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame8;
    private widget.InternalFrame internalFrame9;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel36;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel40;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel49;
    private widget.Label jLabel5;
    private widget.Label jLabel50;
    private widget.Label jLabel51;
    private widget.Label jLabel52;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel57;
    private widget.Label jLabel58;
    private widget.Label jLabel59;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel61;
    private widget.Label jLabel62;
    private widget.Label jLabel7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private widget.Label label14;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.panelisi panelisi5;
    private widget.panelisi panelisi6;
    private widget.ScrollPane scrollInput;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            String sql = "select reg_periksa.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, " +
                "kamar_inap.tgl_masuk, kamar_inap.jam_masuk, kamar_inap.tgl_keluar, kamar_inap.jam_keluar, " +
                "kamar_inap.diagnosa_awal, kamar_inap.diagnosa_akhir, kamar_inap.stts_pulang, " +
                "catatan_masuk_keluar.masuk_melalui, catatan_masuk_keluar.cara_masuk, " +
                "catatan_masuk_keluar.kd_dokter_konsultan, dokter_konsultan.nm_dokter as nm_dokter_konsultan, " +
                "catatan_masuk_keluar.nama_tindakan_operasi, catatan_masuk_keluar.jenis_anastesi, " +
                "catatan_masuk_keluar.golongan_operasi, catatan_masuk_keluar.infeksi_nosokomial, " +
                "catatan_masuk_keluar.penyebab_infeksi, catatan_masuk_keluar.pemeriksaan_fisik, " +
                "catatan_masuk_keluar.hasil_usg, catatan_masuk_keluar.hasil_ekg, " +
                "catatan_masuk_keluar.hasil_rontgen, catatan_masuk_keluar.hasil_lab, " +
                "catatan_masuk_keluar.imunisasi_masuk, catatan_masuk_keluar.imunisasi_rawat, " +
                "catatan_masuk_keluar.transfusi_darah_cc, catatan_masuk_keluar.rujukan_dari, " +
                "catatan_masuk_keluar.alamat_perujuk, catatan_masuk_keluar.tempat_tugas_perujuk, " +
                "catatan_masuk_keluar.telp_perujuk, catatan_masuk_keluar.cara_rujukan, catatan_masuk_keluar.catatan_rujukan, " +
                "catatan_masuk_keluar.keadaan_pulang, catatan_masuk_keluar.ket_keadaan_pulang, " +
                "catatan_masuk_keluar.cara_pulang, catatan_masuk_keluar.ket_cara_pulang, catatan_masuk_keluar.catatan " +
                "from catatan_masuk_keluar " +
                "inner join reg_periksa on catatan_masuk_keluar.no_rawat=reg_periksa.no_rawat " +
                "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                "inner join kamar_inap on catatan_masuk_keluar.no_rawat=kamar_inap.no_rawat " +
                "left join dokter as dokter_konsultan on catatan_masuk_keluar.kd_dokter_konsultan=dokter_konsultan.kd_dokter " +
                "where kamar_inap.tgl_masuk between ? and ? " +
                (TCari.getText().trim().equals("") ? "" :
                "and (reg_periksa.no_rawat like ? or reg_periksa.no_rkm_medis like ? or pasien.nm_pasien like ?) ") +
                "group by catatan_masuk_keluar.no_rawat " +
                "order by kamar_inap.tgl_masuk";
            ps = koneksi.prepareStatement(sql);
            ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem()+""));
            ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem()+""));
            if(!TCari.getText().trim().equals("")){
                ps.setString(3, "%"+TCari.getText()+"%");
                ps.setString(4, "%"+TCari.getText()+"%");
                ps.setString(5, "%"+TCari.getText()+"%");
            }
            rs = ps.executeQuery();
            while(rs.next()){
                tabMode.addRow(new Object[]{
                    rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"),
                    rs.getString("tgl_masuk"), rs.getString("jam_masuk"),
                    rs.getString("tgl_keluar"), rs.getString("jam_keluar"),
                    rs.getString("diagnosa_awal"), rs.getString("diagnosa_akhir"), rs.getString("stts_pulang"),
                    rs.getString("masuk_melalui"), rs.getString("cara_masuk"),
                    rs.getString("kd_dokter_konsultan"), rs.getString("nm_dokter_konsultan"),
                    rs.getString("nama_tindakan_operasi"), rs.getString("jenis_anastesi"),
                    rs.getString("golongan_operasi"), rs.getString("infeksi_nosokomial"),
                    rs.getString("penyebab_infeksi"), rs.getString("pemeriksaan_fisik"),
                    rs.getString("hasil_usg"), rs.getString("hasil_ekg"),
                    rs.getString("hasil_rontgen"), rs.getString("hasil_lab"),
                    rs.getString("imunisasi_masuk"), rs.getString("imunisasi_rawat"),
                    rs.getString("transfusi_darah_cc"), rs.getString("rujukan_dari"),
                    rs.getString("alamat_perujuk"), rs.getString("tempat_tugas_perujuk"),
                    rs.getString("telp_perujuk"), rs.getString("cara_rujukan"), rs.getString("catatan_rujukan"),
                    rs.getString("keadaan_pulang"), rs.getString("ket_keadaan_pulang"),
                    rs.getString("cara_pulang"), rs.getString("ket_cara_pulang"),
                    rs.getString("catatan")
                });
            }
        } catch (Exception e) {
            System.out.println("Notif tampil CatatanMasukKeluar: "+e);
        } finally {
            try{ if(rs!=null) rs.close(); if(ps!=null) ps.close(); }catch(Exception e){}
        }
        LCount.setText(""+tabMode.getRowCount());
    }

    public void emptTeks() {
        MasukMelalui.getText();
        CaraMasuk.setSelectedIndex(0);
        KdDokterKonsultan.setText("");
        NmDokterKonsultan.setText("");
        DiagnosaAwal.setText("");
        DiagnosaAkhir.setText("");
        NamaTindakanOperasi.setText("");
        JenisAnastesi.setText("");
        GolonganOperasi.setText("");
        InfeksiNosokomial1.setSelectedIndex(0);
        PenyebabInfeksi.setText("");
        PemeriksaanFisik.setText("");
        HasilUSG.setText("");
        HasilEKG.setText("");
        HasilRontgen.setText("");
        HasilLab.setText("");
        ImunisasiMasuk.setText("");
        ImunisasiRawat.setText("");
        TransfusiDarahCC.setText("");
        RujukanDari.setText("");
        AlamatPerujuk.setText("");
        TempatTugasPerujuk.setText("");
        TelpPerujuk.setText("");
        CaraRujukan.setText("");
        KeadaanPulang.setSelectedIndex(0);
        KetKeadaanPulang.setText("");
        CaraPulang.setSelectedIndex(0);
        KetCaraPulang.setText("");
        CatatanRM.setText("");
        CatatanRujukan.setText("");
        TNoRw.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow() != -1){
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            MasukMelalui.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            JamMasuk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            Keluar.setText(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            JamKeluar.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            DiagnosaAwal.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            DiagnosaAkhir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            StsPulang.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            MasukMelalui.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            CaraMasuk.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            KdDokterKonsultan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            NmDokterKonsultan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            NamaTindakanOperasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            JenisAnastesi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
            GolonganOperasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
            InfeksiNosokomial1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
            PenyebabInfeksi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
            PemeriksaanFisik.setText(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString());
            HasilUSG.setText(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
            HasilEKG.setText(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());
            HasilRontgen.setText(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());
            HasilLab.setText(tbObat.getValueAt(tbObat.getSelectedRow(),23).toString());
            ImunisasiMasuk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),24).toString());
            ImunisasiRawat.setText(tbObat.getValueAt(tbObat.getSelectedRow(),25).toString());
            TransfusiDarahCC.setText(tbObat.getValueAt(tbObat.getSelectedRow(),26).toString());
            RujukanDari.setText(tbObat.getValueAt(tbObat.getSelectedRow(),27).toString());
            AlamatPerujuk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),28).toString());
            TempatTugasPerujuk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),29).toString());
            TelpPerujuk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),30).toString());
            CaraRujukan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),31).toString());
            CatatanRujukan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),32).toString());
            KeadaanPulang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),33).toString());
            KetKeadaanPulang.setText(tbObat.getValueAt(tbObat.getSelectedRow(),34).toString());
            CaraPulang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),35).toString());
            KetCaraPulang.setText(tbObat.getValueAt(tbObat.getSelectedRow(),36).toString());
            CatatanRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),37).toString());
        }
    }
    
    private void isRawat() {
        try {
            // ── 1. Data identitas & kamar ──────────────────────────────────────────
            ps = koneksi.prepareStatement(
                "select reg_periksa.no_rkm_medis, pasien.nm_pasien, " +
                "kamar_inap.tgl_masuk, kamar_inap.jam_masuk, " +
                "kamar_inap.tgl_keluar, kamar_inap.jam_keluar, " +
                "kamar_inap.diagnosa_awal, kamar_inap.diagnosa_akhir, kamar_inap.stts_pulang, " +
                "CASE WHEN reg_periksa.kd_poli='IGDK' THEN 'UGD' ELSE poliklinik.nm_poli END AS masuk_melalui " +
                "from reg_periksa " +
                "inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis " +
                "inner join kamar_inap on kamar_inap.no_rawat=reg_periksa.no_rawat " +
                "inner join poliklinik on poliklinik.kd_poli=reg_periksa.kd_poli " +
                "where reg_periksa.no_rawat=? " +
                "order by kamar_inap.tgl_masuk desc limit 1");
            ps.setString(1, TNoRw.getText());
            rs = ps.executeQuery();
            if(rs.next()){
                TNoRM.setText(rs.getString("no_rkm_medis"));
                TPasien.setText(rs.getString("nm_pasien"));
                MasukMelalui.setText(rs.getString("tgl_masuk"));
                JamMasuk.setText(rs.getString("jam_masuk"));
                Keluar.setText(rs.getString("tgl_keluar"));
                JamKeluar.setText(rs.getString("jam_keluar"));
                DiagnosaAwal.setText(rs.getString("diagnosa_awal"));
                DiagnosaAkhir.setText(rs.getString("diagnosa_akhir"));
                StsPulang.setText(rs.getString("stts_pulang"));
                MasukMelalui.setText(rs.getString("masuk_melalui"));
            }
        } catch (Exception e) {
            System.out.println("Notif isRawat: " + e);
        } finally {
            try{ if(rs!=null) rs.close(); if(ps!=null) ps.close(); }catch(Exception e){}
        }

        // ── 2. Cara Masuk: cek apakah ada data rujukan BPJS / sisrute ────────────
        try {
            int jmlRujukan = Sequel.cariInteger(
                "select count(*) from reg_periksa " +
                "left join bridging_sep on bridging_sep.no_rawat=reg_periksa.no_rawat " +
                "where reg_periksa.no_rawat='" + TNoRw.getText() + "' and bridging_sep.jnsPelayanan is not null");
            if(jmlRujukan > 0){
                CaraMasuk.setSelectedItem("Dari RS Lain");
            } else {
                CaraMasuk.setSelectedItem("Datang Sendiri");
            }
        } catch (Exception e) {
            System.out.println("Notif isRawat CaraMasuk: " + e);
        }

        // ── 3. Dokter Konsultan: ambil dari dpjp_ranap ─────────────────────────
        try {
            String drMenerima = Sequel.cariIsi("select kd_dokter from reg_periksa where no_rawat=?", TNoRw.getText());
            ps = koneksi.prepareStatement(
                "select dpjp_ranap.kd_dokter, dokter.nm_dokter " +
                "from dpjp_ranap " +
                "inner join dokter on dokter.kd_dokter=dpjp_ranap.kd_dokter " +
                "where dpjp_ranap.no_rawat=? and dpjp_ranap.kd_dokter<>?");
            ps.setString(1, TNoRw.getText());
            ps.setString(2, drMenerima);
            rs = ps.executeQuery();
            if(rs.next()){
                if(KdDokterKonsultan.getText().trim().equals("")){
                    KdDokterKonsultan.setText(rs.getString("kd_dokter"));
                    NmDokterKonsultan.setText(rs.getString("nm_dokter"));
                }
            }
        } catch (Exception e) {
            System.out.println("Notif isRawat Dokter: " + e);
        } finally {
            try{ if(rs!=null) rs.close(); if(ps!=null) ps.close(); }catch(Exception e){}
        }

        // ── 4. Pemeriksaan Fisik: ambil dari penilaian_awal_ranap ─────────────────
        try {
            String pemFisik = Sequel.cariIsi(
                "select CONCAT_WS(', '," +
                "NULLIF(pemeriksaan_fisik,'')," +
                "NULLIF(CONCAT('TD:',td_sistol,'/',td_diastol),'TD:/')," +
                "NULLIF(CONCAT('N:',nadi),'N:')," +
                "NULLIF(CONCAT('RR:',pernafasan),'RR:')," +
                "NULLIF(CONCAT('T:',suhu),'T:')" +
                ") from penilaian_awal_ranap where no_rawat='" + TNoRw.getText() + "' limit 1");
            if(pemFisik != null && !pemFisik.trim().equals(""))
                PemeriksaanFisik.setText(pemFisik);
        } catch (Exception e) {
            System.out.println("Notif isRawat PemFisik: " + e);
        }

        // ── 5. Hasil Lab: concat semua hasil pemeriksaan lab ─────────────────────
        try {
            ps = koneksi.prepareStatement(
                "select GROUP_CONCAT(CONCAT(template_lab.nm_template,': ',detail_periksa_lab.nilai," +
                "' ',template_lab.satuan) ORDER BY detail_periksa_lab.tgl_periksa SEPARATOR '; ') as hasil " +
                "from detail_periksa_lab " +
                "inner join template_lab on template_lab.id_template=detail_periksa_lab.id_template " +
                "where detail_periksa_lab.no_rawat=?");
            ps.setString(1, TNoRw.getText());
            rs = ps.executeQuery();
            if(rs.next() && rs.getString("hasil") != null)
                HasilLab.setText(rs.getString("hasil"));
        } catch (Exception e) {
            System.out.println("Notif isRawat Lab: " + e);
        } finally {
            try{ if(rs!=null) rs.close(); if(ps!=null) ps.close(); }catch(Exception e){}
        }

        // ── 6. Hasil Radiologi: concat semua hasil ───────────────────────────────
        try {
            ps = koneksi.prepareStatement(
                "select GROUP_CONCAT(hasil ORDER BY tgl_periksa SEPARATOR '; ') as hasil_rad " +
                "from hasil_radiologi where no_rawat=?");
            ps.setString(1, TNoRw.getText());
            rs = ps.executeQuery();
            if(rs.next() && rs.getString("hasil_rad") != null)
                HasilRontgen.setText(rs.getString("hasil_rad"));
        } catch (Exception e) {
            System.out.println("Notif isRawat Radiologi: " + e);
        } finally {
            try{ if(rs!=null) rs.close(); if(ps!=null) ps.close(); }catch(Exception e){}
        }

        // ── 7. Tindakan Operasi: ambil dari tabel operasi ────────────────────────
        try {
            ps = koneksi.prepareStatement(
                "select GROUP_CONCAT(kamar_operasi.kd_kamar ORDER BY operasi.tgl_operasi SEPARATOR ', ') as tindakan," +
                "operasi.jenis_anasthesi, operasi.kategori " +
                "from operasi " +
                "inner join kamar_operasi on kamar_operasi.kode_paket=operasi.kode_paket " +
                "where operasi.no_rawat=? limit 1");
            ps.setString(1, TNoRw.getText());
            rs = ps.executeQuery();
            if(rs.next()){
                if(rs.getString("tindakan") != null)
                    NamaTindakanOperasi.setText(rs.getString("tindakan"));
                if(rs.getString("jenis_anasthesi") != null)
                    JenisAnastesi.setText(rs.getString("jenis_anasthesi"));
                if(rs.getString("kategori") != null)
                    GolonganOperasi.setText(rs.getString("kategori"));
            }
        } catch (Exception e) {
            System.out.println("Notif isRawat Operasi: " + e);
        } finally {
            try{ if(rs!=null) rs.close(); if(ps!=null) ps.close(); }catch(Exception e){}
        }
    }
    
    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        DTPCari2.setDate(tgl2);    
        isRawat();              
        ChkInput.setSelected(true);
        isForm();
        // try { CaraPulang.requestFocus(); } catch(Exception e) {}

        runBackground(() ->tampil());
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,this.getHeight()-122));
            scrollInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            scrollInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getdata_resume_pasien());
        BtnHapus.setEnabled(akses.getdata_resume_pasien());
        BtnEdit.setEnabled(akses.getdata_resume_pasien());
        BtnPrint.setEnabled(akses.getdata_resume_pasien());      
        if(akses.getjml2()>=1){
            KdDokterKonsultan.setEditable(false);
            // BtnDokterKonsultan.setEnabled(false);
            KdDokterKonsultan.setText(akses.getkode());
            NmDokterKonsultan.setText(Sequel.CariDokter(KdDokterKonsultan.getText()));
            if(NmDokterKonsultan.getText().equals("")){
                KdDokterKonsultan.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan dokter...!!");
            }
        }            
    }

    private void ganti() {
        if(Sequel.mengedittf("catatan_masuk_keluar","no_rawat=?","masuk_melalui=?,cara_masuk=?,kd_dokter_konsultan=?,nama_tindakan_operasi=?,jenis_anastesi=?,golongan_operasi=?,infeksi_nosokomial=?," +
            "penyebab_infeksi=?,pemeriksaan_fisik=?,hasil_usg=?,hasil_ekg=?,hasil_rontgen=?,hasil_lab=?,imunisasi_masuk=?,imunisasi_rawat=?,transfusi_darah_cc=?," +
            "rujukan_dari=?,alamat_perujuk=?,tempat_tugas_perujuk=?,telp_perujuk=?,cara_rujukan=?,catatan_rujukan=?,keadaan_pulang=?,ket_keadaan_pulang=?,cara_pulang=?,ket_cara_pulang=?,catatan=?", 28, new String[]{
            MasukMelalui.getText().toString(), CaraMasuk.getSelectedItem().toString(), KdDokterKonsultan.getText(),
            NamaTindakanOperasi.getText(), JenisAnastesi.getText(), GolonganOperasi.getText(), InfeksiNosokomial1.getSelectedItem().toString(),
            PenyebabInfeksi.getText(), PemeriksaanFisik.getText(), HasilUSG.getText(), HasilEKG.getText(), HasilRontgen.getText(), HasilLab.getText(),
            ImunisasiMasuk.getText(), ImunisasiRawat.getText(), TransfusiDarahCC.getText(), RujukanDari.getText(), AlamatPerujuk.getText(),
            TempatTugasPerujuk.getText(), TelpPerujuk.getText(), CaraRujukan.getText(), CatatanRujukan.getText(), KeadaanPulang.getSelectedItem().toString(), KetKeadaanPulang.getText(),
            CaraPulang.getSelectedItem().toString(), KetCaraPulang.getText(), CatatanRM.getText(), tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        })==true){
            tabMode.setValueAt(MasukMelalui.getText().toString(),tbObat.getSelectedRow(),10);
            tabMode.setValueAt(CaraMasuk.getSelectedItem().toString(),tbObat.getSelectedRow(),11);
            tabMode.setValueAt(KdDokterKonsultan.getText(),tbObat.getSelectedRow(),12);
            tabMode.setValueAt(NmDokterKonsultan.getText(),tbObat.getSelectedRow(),13);
            tabMode.setValueAt(NamaTindakanOperasi.getText(),tbObat.getSelectedRow(),14);
            tabMode.setValueAt(JenisAnastesi.getText(),tbObat.getSelectedRow(),15);
            tabMode.setValueAt(GolonganOperasi.getText(),tbObat.getSelectedRow(),16);
            tabMode.setValueAt(InfeksiNosokomial1.getSelectedItem().toString(),tbObat.getSelectedRow(),17);
            tabMode.setValueAt(PenyebabInfeksi.getText(),tbObat.getSelectedRow(),18);
            tabMode.setValueAt(PemeriksaanFisik.getText(),tbObat.getSelectedRow(),19);
            tabMode.setValueAt(HasilUSG.getText(),tbObat.getSelectedRow(),20);
            tabMode.setValueAt(HasilEKG.getText(),tbObat.getSelectedRow(),21);
            tabMode.setValueAt(HasilRontgen.getText(),tbObat.getSelectedRow(),22);
            tabMode.setValueAt(HasilLab.getText(),tbObat.getSelectedRow(),23);
            tabMode.setValueAt(ImunisasiMasuk.getText(),tbObat.getSelectedRow(),24);
            tabMode.setValueAt(ImunisasiRawat.getText(),tbObat.getSelectedRow(),25);
            tabMode.setValueAt(TransfusiDarahCC.getText(),tbObat.getSelectedRow(),26);
            tabMode.setValueAt(RujukanDari.getText(),tbObat.getSelectedRow(),27);
            tabMode.setValueAt(AlamatPerujuk.getText(),tbObat.getSelectedRow(),28);
            tabMode.setValueAt(TempatTugasPerujuk.getText(),tbObat.getSelectedRow(),29);
            tabMode.setValueAt(TelpPerujuk.getText(),tbObat.getSelectedRow(),30);
            tabMode.setValueAt(CaraRujukan.getText(),tbObat.getSelectedRow(),31);
            tabMode.setValueAt(CatatanRujukan.getText(),tbObat.getSelectedRow(),32);
            tabMode.setValueAt(KeadaanPulang.getSelectedItem().toString(),tbObat.getSelectedRow(),33);
            tabMode.setValueAt(KetKeadaanPulang.getText(),tbObat.getSelectedRow(),34);
            tabMode.setValueAt(CaraPulang.getSelectedItem().toString(),tbObat.getSelectedRow(),35);
            tabMode.setValueAt(KetCaraPulang.getText(),tbObat.getSelectedRow(),36);
            tabMode.setValueAt(CatatanRM.getText(),tbObat.getSelectedRow(),37);
            emptTeks();
        }
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from catatan_masuk_keluar where no_rawat=?",1,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        })==true){
            tabMode.removeRow(tbObat.getSelectedRow());
            LCount.setText(""+tabMode.getRowCount());
            emptTeks();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }

    private void runBackground(Runnable task) {
        if (ceksukses) return;
        if (executor.isShutdown() || executor.isTerminated()) return;
        if (!isDisplayable()) return;

        ceksukses = true;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            executor.submit(() -> {
                try {
                    task.run();
                } finally {
                    ceksukses = false;
                    SwingUtilities.invokeLater(() -> {
                        if (isDisplayable()) {
                            setCursor(Cursor.getDefaultCursor());
                        }
                    });
                }
            });
        } catch (RejectedExecutionException ex) {
            ceksukses = false;
        }
    }
    
    @Override
    public void dispose() {
        executor.shutdownNow();
        super.dispose();
    }
}
