/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.7_B_PP2_2025.Model;

/**
 *
 * @author NAJRAN AL-FARESY
 */
public class entitas {
     public static class Mahasiswa {
        public String npm, nama, jurusan;
        public Mahasiswa(String n, String m, String j) { this.npm = n; this.nama = m; this.jurusan = j; }
    }

    public static class Dosen {
        public String nidn, nama, email;
        public Dosen(String n, String m, String e) { this.nidn = n; this.nama = m; this.email = e; }
    }

    public static class MataKuliah {
        public String kode, nama; public int sks;
        public MataKuliah(String k, String n, int s) { this.kode = k; this.nama = n; this.sks = s; }
    }
}