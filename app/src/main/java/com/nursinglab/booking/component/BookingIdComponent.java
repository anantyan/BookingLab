package com.nursinglab.booking.component;

public class BookingIdComponent {

    String nama_dosen;
    String nama;
    String id;

    public BookingIdComponent(String nama_dosen, String nama, String id) {
        this.nama_dosen = nama_dosen;
        this.nama = nama;
        this.id = id;
    }

    public String getNama_dosen() {
        return nama_dosen;
    }

    public void setNama_dosen(String nama_dosen) {
        this.nama_dosen = nama_dosen;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
