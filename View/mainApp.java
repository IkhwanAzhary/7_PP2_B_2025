/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.Kelompok7_PP2_B_2025.View;

import id.ac.unpas.Kelompok7_PP2_B_2025.Controlller.controllerSitu;
import id.ac.unpas.Kelompok7_PP2_B_2025.Model.entitas.*;
import id.ac.unpas.Kelompok7_PP2_B_2025.Model.koneksiDB;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class mainApp extends JFrame {
    // Memanggil Controller
    private controllerSitu control = new controllerSitu();
    
    // Komponen Global
    private JTabbedPane tabs = new JTabbedPane();

    // Komponen Fitur 1: Mahasiswa
    private JTextField tNpm = new JTextField(), tNamaM = new JTextField(), tCariM = new JTextField(15);
    private JComboBox<String> cbJurusan = new JComboBox<>(new String[]{
        "Teknik Industri", "Teknik Pangan", "Teknik Mesin", 
        "Teknik Informatika", "Teknik Lingkungan", "PWK"
    });
    private JTable tblMhs = new JTable();
    private DefaultTableModel modMhs = new DefaultTableModel(new Object[]{"NPM", "Nama", "Jurusan"}, 0);
    
      // Komponen Fitur 2: Dosen
    private JTextField tNidn = new JTextField(), tNamaD = new JTextField(), tEmail = new JTextField(), tCariD = new JTextField(15);
    private JTable tblDos = new JTable();
    private DefaultTableModel modDos = new DefaultTableModel(new Object[]{"NIDN", "Nama", "Email"}, 0);
    
    // Komponen Fitur 3: Mata Kuliah
    private JTextField tKode = new JTextField(), tNamaK = new JTextField(), tSks = new JTextField(), tCariK = new JTextField(15);
    private JTable tblMK = new JTable();
    private DefaultTableModel modMK = new DefaultTableModel(new Object[]{"Kode MK", "Nama MK", "SKS"}, 0);
    
    public mainApp() {
        setTitle("SITU2 UNPAS - Sistem Informasi Akademik");
        setSize(900, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menambahkan Tab ke Frame
        tabs.addTab("Data Mahasiswa", panelMahasiswa());
        tabs.addTab("Data Dosen", panelDosen());
        tabs.addTab("Mata Kuliah", panelMK());
        add(tabs);

        // Load data awal saat aplikasi dibuka
        loadMhs("");
        loadDos("");
        loadMK("");
    }

    // ==========================================
    // METODE VALIDASI INPUT MAHASISWA
    // ==========================================
    private boolean validateMahasiswaInput() {
        String npm = tNpm.getText().trim();
        String nama = tNamaM.getText().trim();
        String jurusan = (String) cbJurusan.getSelectedItem();

        // Validasi NPM dan Nama tidak boleh kosong
        if (npm.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "NPM dan Nama wajib diisi!", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validasi NPM harus berupa angka
        if (!npm.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, 
                "NPM harus berupa angka!\nContoh: 123456789", 
                "Error Validasi NPM", 
                JOptionPane.ERROR_MESSAGE);
            tNpm.requestFocus();
            return false;
        }

        // Validasi Nama harus berupa huruf (boleh dengan spasi)
        if (!nama.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, 
                "Nama harus berupa huruf (tidak boleh mengandung angka atau karakter khusus)!\nContoh: Budi Santoso", 
                "Error Validasi Nama", 
                JOptionPane.ERROR_MESSAGE);
            tNamaM.requestFocus();
            return false;
        }

        // Validasi Jurusan harus dipilih
        if (jurusan == null || jurusan.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Jurusan harus dipilih!", 
                "Error Validasi Jurusan", 
                JOptionPane.ERROR_MESSAGE);
            cbJurusan.requestFocus();
            return false;
        }

        return true;
    }
    
      // ==========================================
    // METODE VALIDASI INPUT DOSEN
    // ==========================================
    private boolean validateDosenInput() {
        String nidn = tNidn.getText().trim();
        String nama = tNamaD.getText().trim();
        String email = tEmail.getText().trim();

        // Validasi NIDN, Nama, dan Email tidak boleh kosong
        if (nidn.isEmpty() || nama.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "NIDN, Nama, dan Email wajib diisi!", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validasi NIDN harus berupa angka
        if (!nidn.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, 
                "NIDN harus berupa angka Contoh: 0412345678", 
                "Error Validasi NIDN", 
                JOptionPane.ERROR_MESSAGE);
            tNidn.requestFocus();
            return false;
        }

        // Validasi Nama harus berupa huruf (boleh dengan spasi)
        if (!nama.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, 
                "Nama harus berupa huruf (tidak boleh mengandung angka atau karakter khusus)Contoh: Dr Ahmad Santoso", 
                "Error Validasi Nama", 
                JOptionPane.ERROR_MESSAGE);
            tNamaD.requestFocus();
            return false;
        }

        // Validasi Email harus mengandung @
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, 
                "Email harus mengandung karakter '@' Contoh: dosen@unpas.ac.id", 
                "Error Validasi Email", 
                JOptionPane.ERROR_MESSAGE);
            tEmail.requestFocus();
            return false;
        }

        // Validasi format email lebih detail (opsional tapi direkomendasikan)
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, 
                "Format email tidak valid! Contoh: dosen@unpas.ac.id atau nama.dosen@gmail.com", 
                "Error Validasi Email", 
                JOptionPane.ERROR_MESSAGE);
            tEmail.requestFocus();
            return false;
        }

        return true;
    }
    
      // METODE VALIDASI INPUT MATA KULIAH
    
    private boolean validateMataKuliahInput() {
        String kode = tKode.getText().trim();
        String nama = tNamaK.getText().trim();
        String sks = tSks.getText().trim();

        // Validasi Kode MK, Nama MK, dan SKS tidak boleh kosong
        if (kode.isEmpty() || nama.isEmpty() || sks.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Kode MK, Nama MK, dan SKS wajib diisi!", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validasi Nama MK harus berupa huruf (boleh dengan spasi)
        if (!nama.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, 
                "Nama Mata Kuliah harus berupa huruf (tidak boleh mengandung angka atau karakter khusus)!\nContoh: Pemrograman Berorientasi Objek", 
                "Error Validasi Nama MK", 
                JOptionPane.ERROR_MESSAGE);
            tNamaK.requestFocus();
            return false;
        }

        // Validasi SKS harus berupa angka
        if (!sks.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, 
                "SKS harus berupa angka!\nContoh: 3 atau 4", 
                "Error Validasi SKS", 
                JOptionPane.ERROR_MESSAGE);
            tSks.requestFocus();
            return false;
        }

        // Validasi SKS harus dalam rentang wajar (1-6)
        int sksValue = Integer.parseInt(sks);
        if (sksValue < 1 || sksValue > 6) {
            JOptionPane.showMessageDialog(this, 
                "SKS harus dalam rentang 1-6!\nContoh: 2, 3, atau 4", 
                "Error Validasi SKS", 
                JOptionPane.ERROR_MESSAGE);
            tSks.requestFocus();
            return false;
        }

        return true;
    }
    
    // ==========================================
    // UI & LOGIKA PANEL MAHASISWA
    // ==========================================
    private JPanel panelMahasiswa() {
        JPanel main = new JPanel(new BorderLayout());

        // Panel Cari
        JPanel pCari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton bCari = new JButton("Cari");
        pCari.add(new JLabel("Cari Nama/NPM:")); pCari.add(tCariM); pCari.add(bCari);

        // Panel Form
        JPanel pForm = new JPanel(new GridLayout(3, 2, 5, 5));
        pForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pForm.add(new JLabel("NPM:")); pForm.add(tNpm);
        pForm.add(new JLabel("Nama:")); pForm.add(tNamaM);
        pForm.add(new JLabel("Jurusan:")); pForm.add(cbJurusan);

        // Panel Tombol
        JPanel pBtn = new JPanel();
        JButton bSim = new JButton("Simpan"), bUpd = new JButton("Update"), bHap = new JButton("Hapus"), 
                bPdf = new JButton("Export PDF"), bClr = new JButton("Clear");
        pBtn.add(bSim); pBtn.add(bUpd); pBtn.add(bHap); pBtn.add(bPdf); pBtn.add(bClr);

        // Tata Letak Atas
        JPanel pNorth = new JPanel(new BorderLayout());
        pNorth.add(pCari, BorderLayout.NORTH);
        pNorth.add(pForm, BorderLayout.CENTER);
        pNorth.add(pBtn, BorderLayout.SOUTH);

        tblMhs.setModel(modMhs);
        main.add(pNorth, BorderLayout.NORTH);
        main.add(new JScrollPane(tblMhs), BorderLayout.CENTER);

        // --- Event Listeners Mahasiswa ---
        bSim.addActionListener(e -> {
            if (!validateMahasiswaInput()) {
                return;
            }
            
            try {
                String jurusan = (String) cbJurusan.getSelectedItem();
                control.tambahMahasiswa(new Mahasiswa(tNpm.getText().trim(), tNamaM.getText().trim(), jurusan));
                JOptionPane.showMessageDialog(this, "Data Mahasiswa Berhasil Disimpan");
                loadMhs(""); clearMhs();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        bUpd.addActionListener(e -> {
            if (!validateMahasiswaInput()) {
                return;
            }
            
            try {
                String jurusan = (String) cbJurusan.getSelectedItem();
                control.ubahMahasiswa(new Mahasiswa(tNpm.getText().trim(), tNamaM.getText().trim(), jurusan));
                JOptionPane.showMessageDialog(this, "Data Mahasiswa Berhasil Diupdate");
                loadMhs(""); clearMhs();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        bHap.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Hapus", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                try {
                    control.hapusMahasiswa(tNpm.getText().trim());
                    JOptionPane.showMessageDialog(this, "Data Mahasiswa Berhasil Dihapus");
                    loadMhs(""); clearMhs();
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
                }
            }
        });

         bCari.addActionListener(e -> loadMhs(tCariM.getText()));
        
        bClr.addActionListener(e -> clearMhs());

        tblMhs.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblMhs.getSelectedRow();
                tNpm.setText(modMhs.getValueAt(r, 0).toString());
                tNamaM.setText(modMhs.getValueAt(r, 1).toString());
                cbJurusan.setSelectedItem(modMhs.getValueAt(r, 2).toString());
                tNpm.setEditable(false);
            }
        });

        return main;
}

        private void loadMhs(String key) {
        modMhs.setRowCount(0);
        try {
            List<Mahasiswa> list = key.isEmpty() ? control.getAllMahasiswa() : control.cariMahasiswa(key);
            for(Mahasiswa m : list) modMhs.addRow(new Object[]{m.npm, m.nama, m.jurusan});
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void clearMhs() {
        tNpm.setText(""); 
        tNamaM.setText(""); 
        cbJurusan.setSelectedIndex(0); 
        tCariM.setText("");
        tNpm.setEditable(true);
    }
    // ==========================================
    // LOGIKA PANEL DOSEN
    // ==========================================
    private JPanel panelDosen() {
        JPanel main = new JPanel(new BorderLayout());

        // Panel Cari
        JPanel pCari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton bCari = new JButton("Cari");
        pCari.add(new JLabel("Cari Nama/NIDN:")); 
        pCari.add(tCariD); 
        pCari.add(bCari);

        // Panel Form
        JPanel pForm = new JPanel(new GridLayout(3, 2, 5, 5));
        pForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pForm.add(new JLabel("NIDN:")); pForm.add(tNidn);
        pForm.add(new JLabel("Nama:")); pForm.add(tNamaD);
        pForm.add(new JLabel("Email:")); pForm.add(tEmail);

        // Panel Tombol
        JPanel pBtn = new JPanel();
        JButton bSim = new JButton("Simpan"), bUpd = new JButton("Update"), bHap = new JButton("Hapus"), 
                bPdf = new JButton("Export PDF"), bClr = new JButton("Clear");
        pBtn.add(bSim); pBtn.add(bUpd); pBtn.add(bHap); pBtn.add(bPdf); pBtn.add(bClr);

        // Tata Letak Atas
        JPanel pNorth = new JPanel(new BorderLayout());
        pNorth.add(pCari, BorderLayout.NORTH);
        pNorth.add(pForm, BorderLayout.CENTER);
        pNorth.add(pBtn, BorderLayout.SOUTH);

        tblDos.setModel(modDos);
        main.add(pNorth, BorderLayout.NORTH);
        main.add(new JScrollPane(tblDos), BorderLayout.CENTER);

        bSim.addActionListener(e -> {
            if (!validateDosenInput()) {
                return;
            }
            
            try {
                control.tambahDosen(new Dosen(tNidn.getText().trim(), tNamaD.getText().trim(), tEmail.getText().trim()));
                JOptionPane.showMessageDialog(this, "Data Dosen Berhasil Disimpan");
                loadDos(""); 
                clearDosen();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        bUpd.addActionListener(e -> {
            if (!validateDosenInput()) {
                return;
            }
            
            try {
                control.ubahDosen(new Dosen(tNidn.getText().trim(), tNamaD.getText().trim(), tEmail.getText().trim()));
                JOptionPane.showMessageDialog(this, "Data Dosen Berhasil Diupdate");
                loadDos(""); 
                clearDosen();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });
        
        bHap.addActionListener(e -> { 
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Hapus", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                try { 
                    control.hapusDosen(tNidn.getText().trim()); 
                    JOptionPane.showMessageDialog(this, "Data Dosen Berhasil Dihapus");
                    loadDos(""); 
                    clearDosen();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bCari.addActionListener(e -> loadDos(tCariD.getText()));
        
        bClr.addActionListener(e -> clearDosen());

        tblDos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblDos.getSelectedRow();
                tNidn.setText(modDos.getValueAt(r, 0).toString());
                tNamaD.setText(modDos.getValueAt(r, 1).toString());
                tEmail.setText(modDos.getValueAt(r, 2).toString());
                tNidn.setEditable(false);
            }
        });

        return main;
    }
 

    private void loadDos(String key) {
        modDos.setRowCount(0);
        try {
            List<Dosen> list = key.isEmpty() ? control.getAllDosen() : control.cariDosen(key);
            for(Dosen d : list) modDos.addRow(new Object[]{d.nidn, d.nama, d.email});
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void clearDosen() {
        tNidn.setText(""); 
        tNamaD.setText(""); 
        tEmail.setText(""); 
        tCariD.setText("");
        tNidn.setEditable(true);
    }
      // LOGIKA PANEL MATA KULIAH
   
    private JPanel panelMK() {
        JPanel main = new JPanel(new BorderLayout());
        
        // Panel Cari
        JPanel pCari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton bCariMK = new JButton("Cari");
        pCari.add(new JLabel("Cari Nama/Kode MK:")); 
        pCari.add(tCariK); 
        pCari.add(bCariMK);
        
        // Panel Form
        JPanel pForm = new JPanel(new GridLayout(3, 2, 5, 5));
        pForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pForm.add(new JLabel("Kode MK:")); pForm.add(tKode);
        pForm.add(new JLabel("Nama MK:")); pForm.add(tNamaK);
        pForm.add(new JLabel("SKS:")); pForm.add(tSks);

        // Panel Tombol
        JPanel pBtn = new JPanel();
        JButton bSim = new JButton("Simpan"), bUpd = new JButton("Update"), 
                bHap = new JButton("Hapus"), bPdf = new JButton("Export PDF"), bClr = new JButton("Clear");
        pBtn.add(bSim); pBtn.add(bUpd); pBtn.add(bHap); pBtn.add(bPdf); pBtn.add(bClr);

        // Tata Letak
        JPanel pNorth = new JPanel(new BorderLayout());
        pNorth.add(pCari, BorderLayout.NORTH);
        pNorth.add(pForm, BorderLayout.CENTER);
        pNorth.add(pBtn, BorderLayout.SOUTH);

        tblMK.setModel(modMK);
        main.add(pNorth, BorderLayout.NORTH);
        main.add(new JScrollPane(tblMK), BorderLayout.CENTER);

        bSim.addActionListener(e -> {
            if (!validateMataKuliahInput()) {
                return;
            }
            
            try {
                control.tambahMK(new MataKuliah(tKode.getText().trim(), tNamaK.getText().trim(), Integer.parseInt(tSks.getText().trim())));
                JOptionPane.showMessageDialog(this, "Data Mata Kuliah Berhasil Disimpan");
                loadMK(""); 
                clearMK();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        bUpd.addActionListener(e -> {
            if (!validateMataKuliahInput()) {
                return;
            }
            
            try {
                control.ubahMK(new MataKuliah(tKode.getText().trim(), tNamaK.getText().trim(), Integer.parseInt(tSks.getText().trim())));
                JOptionPane.showMessageDialog(this, "Data Mata Kuliah Berhasil Diupdate");
                loadMK(""); 
                clearMK();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        bHap.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Hapus", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                try {
                    control.hapusMK(tKode.getText().trim());
                    JOptionPane.showMessageDialog(this, "Data Mata Kuliah Berhasil Dihapus");
                    loadMK(""); 
                    clearMK();
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
                }
            }
        });

        bCariMK.addActionListener(e -> loadMK(tCariK.getText())); 
        bClr.addActionListener(e -> clearMK());

        tblMK.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblMK.getSelectedRow();
                tKode.setText(modMK.getValueAt(r, 0).toString());
                tNamaK.setText(modMK.getValueAt(r, 1).toString());
                tSks.setText(modMK.getValueAt(r, 2).toString());
                tKode.setEditable(false);
            }
        });

        return main;
    }

    private void loadMK(String key) {
        modMK.setRowCount(0);
        try {
            List<MataKuliah> list = key.isEmpty() ? control.getAllMK() : control.cariMK(key);
            for(MataKuliah mk : list) modMK.addRow(new Object[]{mk.kode, mk.nama, mk.sks});
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void clearMK() {
        tKode.setText(""); 
        tNamaK.setText(""); 
        tSks.setText(""); 
        tCariK.setText("");
        tKode.setEditable(true);
    }
  }

