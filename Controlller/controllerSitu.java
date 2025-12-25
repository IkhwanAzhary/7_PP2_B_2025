/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.7_B_PP2_2025.Controlller;

import id.ac.unpas.7_PP2_B_2025.Model.entitas.*;
import id.ac.unpas.7_PP2_B_2025.Model.koneksiDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
