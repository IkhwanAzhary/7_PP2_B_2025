/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.TubesSituKami.View;

import id.ac.unpas.TubesSituKami.Controlller.controllerSitu;
import id.ac.unpas.TubesSituKami.Model.entitas.*;
import id.ac.unpas.TubesSituKami.Model.koneksiDB;
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
}
