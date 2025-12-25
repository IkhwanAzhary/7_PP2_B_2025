/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.Kelompok7_PP2_B_2025.Controlller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import id.ac.unpas.Kelompok7_PP2_B_2025.Model.entitas.*;
import id.ac.unpas.Kelompok7_PP2_B_2025.Model.koneksiDB;
import java.io.File;
import java.io.FileOutputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;

public class controllerSitu {
    // ============================================================
    // 1. FITUR MAHASISWA (CREATE, READ, UPDATE, DELETE, SEARCH)
    // ============================================================
    
    public List<Mahasiswa> getAllMahasiswa() throws SQLException {
        List<Mahasiswa> list = new ArrayList<>();
        Connection conn = koneksiDB.configDB();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM mahasiswa");
        while(rs.next()) {
            list.add(new Mahasiswa(rs.getString("npm"), rs.getString("nama"), rs.getString("jurusan")));
        }
        return list;
    }

    public void tambahMahasiswa(Mahasiswa m) throws SQLException {
        Connection conn = koneksiDB.configDB();
        // Validasi Duplikat NPM
        PreparedStatement cek = conn.prepareStatement("SELECT npm FROM mahasiswa WHERE npm=?");
        cek.setString(1, m.npm);
        if (cek.executeQuery().next()) throw new SQLException("NPM sudah terdaftar!");

        PreparedStatement ps = conn.prepareStatement("INSERT INTO mahasiswa VALUES (?,?,?)");
        ps.setString(1, m.npm); ps.setString(2, m.nama); ps.setString(3, m.jurusan);
        ps.execute();
    }

    public void ubahMahasiswa(Mahasiswa m) throws SQLException {
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement("UPDATE mahasiswa SET nama=?, jurusan=? WHERE npm=?");
        ps.setString(1, m.nama); ps.setString(2, m.jurusan); ps.setString(3, m.npm);
        ps.executeUpdate();
    }

    public void hapusMahasiswa(String npm) throws SQLException {
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM mahasiswa WHERE npm=?");
        ps.setString(1, npm);
        ps.execute();
    }

    public List<Mahasiswa> cariMahasiswa(String key) throws SQLException {
        List<Mahasiswa> list = new ArrayList<>();
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM mahasiswa WHERE nama LIKE ? OR npm LIKE ?");
        ps.setString(1, "%" + key + "%"); ps.setString(2, "%" + key + "%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            list.add(new Mahasiswa(rs.getString("npm"), rs.getString("nama"), rs.getString("jurusan")));
        }
        return list;
    }
    
       // ============================================================
    // 2. FITUR DOSEN (CREATE, READ, UPDATE, DELETE, SEARCH)
    // ============================================================

    public List<Dosen> getAllDosen() throws SQLException {
        List<Dosen> list = new ArrayList<>();
        Connection conn = koneksiDB.configDB();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM dosen");
        while(rs.next()) {
            list.add(new Dosen(rs.getString("nidn"), rs.getString("nama"), rs.getString("email")));
        }
        return list;
    }

    public void tambahDosen(Dosen d) throws SQLException {
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO dosen VALUES (?,?,?)");
        ps.setString(1, d.nidn); ps.setString(2, d.nama); ps.setString(3, d.email);
        ps.execute();
    }

    public void ubahDosen(Dosen d) throws SQLException {
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement("UPDATE dosen SET nama=?, email=? WHERE nidn=?");
        ps.setString(1, d.nama); ps.setString(2, d.email); ps.setString(3, d.nidn);
        ps.executeUpdate();
    }

    public void hapusDosen(String nidn) throws SQLException {
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM dosen WHERE nidn=?");
        ps.setString(1, nidn);
        ps.execute();
    }

    public List<Dosen> cariDosen(String key) throws SQLException {
        List<Dosen> list = new ArrayList<>();
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM dosen WHERE nama LIKE ? OR nidn LIKE ?");
        ps.setString(1, "%" + key + "%"); ps.setString(2, "%" + key + "%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            list.add(new Dosen(rs.getString("nidn"), rs.getString("nama"), rs.getString("email")));
        }
        return list;
    }
    
     // 3. FITUR MATA KULIAH (CREATE, READ, UPDATE, DELETE, SEARCH)
    

    public List<MataKuliah> getAllMK() throws SQLException {
        List<MataKuliah> list = new ArrayList<>();
        Connection conn = koneksiDB.configDB();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM matakuliah");
        while(rs.next()) {
            list.add(new MataKuliah(rs.getString("kode_mk"), rs.getString("nama_mk"), rs.getInt("sks")));
        }
        return list;
    }

    public void tambahMK(MataKuliah mk) throws SQLException {
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO matakuliah VALUES (?,?,?)");
        ps.setString(1, mk.kode); ps.setString(2, mk.nama); ps.setInt(3, mk.sks);
        ps.execute();
    }

    public void ubahMK(MataKuliah mk) throws SQLException {
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement("UPDATE matakuliah SET nama_mk=?, sks=? WHERE kode_mk=?");
        ps.setString(1, mk.nama); ps.setInt(2, mk.sks); ps.setString(3, mk.kode);
        ps.executeUpdate();
    }

    public void hapusMK(String kode) throws SQLException {
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM matakuliah WHERE kode_mk=?");
        ps.setString(1, kode);
        ps.execute();
    }

    public List<MataKuliah> cariMK(String key) throws SQLException {
        List<MataKuliah> list = new ArrayList<>();
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM matakuliah WHERE nama_mk LIKE ? OR kode_mk LIKE ?");
        ps.setString(1, "%" + key + "%"); ps.setString(2, "%" + key + "%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            list.add(new MataKuliah(rs.getString("kode_mk"), rs.getString("nama_mk"), rs.getInt("sks")));
        }
        return list;
    }
    
    // ============================================================
    // FITUR EXPORT PDF
    // ============================================================
    
    /**
     * Export data dari JTable ke format PDF
     * @param table JTable yang akan di-export
     * @param filename Nama file PDF yang akan dibuat
     * @return true jika berhasil, false jika gagal
     */
    public boolean exportPDF(JTable table, String filename) {
        try {
            // Membuat dokumen PDF dengan ukuran A4 landscape untuk tabel yang lebar
            Document doc = new Document(PageSize.A4.rotate());
            
            // Membuat file di direktori user
            String userHome = System.getProperty("user.home");
            String filePath = userHome + File.separator + "Downloads" + File.separator + filename;
            
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();
            
            // Menambahkan judul
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("LAPORAN DATA - SITU2 UNPAS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            doc.add(title);
            
            // Menambahkan tanggal export
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
            Paragraph date = new Paragraph("Tanggal Export: " + sdf.format(new Date()), dateFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(20);
            doc.add(date);
            
            // Membuat tabel PDF
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100);
            
            // Styling untuk header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            
            // Menambahkan header
            for (int i = 0; i < table.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i), headerFont));
                cell.setBackgroundColor(new BaseColor(41, 128, 185)); // Warna biru
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                pdfTable.addCell(cell);
            }
            
            // Font untuk data
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            
            // Menambahkan data dari tabel
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object value = table.getValueAt(row, col);
                    PdfPCell cell = new PdfPCell(new Phrase(value != null ? value.toString() : "", dataFont));
                    cell.setPadding(5);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    
                    // Warna bergantian untuk baris
                    if (row % 2 == 0) {
                        cell.setBackgroundColor(new BaseColor(236, 240, 241)); // Abu-abu muda
                    }
                    
                    pdfTable.addCell(cell);
                }
            }
            
            doc.add(pdfTable);
            
            // Menambahkan footer
            Paragraph footer = new Paragraph("\n\nTotal Data: " + table.getRowCount() + " record(s)", dateFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            doc.add(footer);
            
            doc.close();
            
            System.out.println("PDF berhasil dibuat di: " + filePath);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error saat membuat PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Export PDF dengan lokasi yang bisa dipilih user
     */
    public boolean exportPDFWithPath(JTable table, String fullPath) {
        try {
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, new FileOutputStream(fullPath));
            doc.open();
            
            // Judul
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("LAPORAN DATA - SITU2 UNPAS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            doc.add(title);
            
            // Tanggal
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
            Paragraph date = new Paragraph("Tanggal Export: " + sdf.format(new Date()), dateFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(20);
            doc.add(date);
            
            // Tabel
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100);
            
            // Header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            for (int i = 0; i < table.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i), headerFont));
                cell.setBackgroundColor(new BaseColor(41, 128, 185));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                pdfTable.addCell(cell);
            }
            
            // Data
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object value = table.getValueAt(row, col);
                    PdfPCell cell = new PdfPCell(new Phrase(value != null ? value.toString() : "", dataFont));
                    cell.setPadding(5);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    if (row % 2 == 0) {
                        cell.setBackgroundColor(new BaseColor(236, 240, 241));
                    }
                    pdfTable.addCell(cell);
                }
            }
            
            doc.add(pdfTable);
            
            // Footer
            Paragraph footer = new Paragraph("\n\nTotal Data: " + table.getRowCount() + " record(s)", dateFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            doc.add(footer);
            
            doc.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error saat membuat PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
