package com.nursinglab.booking.component;

public class ResultComponent {

    private String waktu_mulai;
    private String waktu_selesai;
    private String tanggal;
    private String nama_dosen;
    private String nama_lab;
    private String nama_praktikum;
    private String nim_mahasiswa;
    private String kelas;
    private String action;
    private String id;

    public ResultComponent(String id, String action, String waktu_mulai, String waktu_selesai, String tanggal, String nama_dosen, String nama_lab, String nama_praktikum, String nim_mahasiswa, String kelas) {
        this.waktu_mulai = waktu_mulai;
        this.waktu_selesai = waktu_selesai;
        this.tanggal = tanggal;
        this.nama_dosen = nama_dosen;
        this.nama_lab = nama_lab;
        this.nama_praktikum = nama_praktikum;
        this.nim_mahasiswa = nim_mahasiswa;
        this.kelas = kelas;
        this.action = action;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAction() {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getWaktu_mulai() {
        return waktu_mulai;
    }

    public void setWaktu_mulai(String waktu_mulai) {
        this.waktu_mulai = waktu_mulai;
    }

    public String getWaktu_selesai() {
        return waktu_selesai;
    }

    public void setWaktu_selesai(String waktu_selesai) {
        this.waktu_selesai = waktu_selesai;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNama_dosen() {
        return nama_dosen;
    }

    public void setNama_dosen(String nama_dosen) {
        this.nama_dosen = nama_dosen;
    }

    public String getNama_lab() {
        return nama_lab;
    }

    public void setNama_lab(String nama_lab) {
        this.nama_lab = nama_lab;
    }

    public String getNama_praktikum() {
        return nama_praktikum;
    }

    public void setNama_praktikum(String nama_praktikum) {
        this.nama_praktikum = nama_praktikum;
    }

    public String getNim_mahasiswa() {
        return nim_mahasiswa;
    }

    public void setNim_mahasiswa(String nim_mahasiswa) {
        this.nim_mahasiswa = nim_mahasiswa;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }
}
