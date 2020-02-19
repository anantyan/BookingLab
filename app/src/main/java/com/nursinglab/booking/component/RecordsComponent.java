package com.nursinglab.booking.component;

public class RecordsComponent {

    private String id;
    private String username;
    private String users_level_id;
    private String nim;
    private String nama_mahasiswa;
    private String foto_file;
    private String created_at;

    public RecordsComponent(String id, String username, String users_level_id, String nim, String nama_mahasiswa, String foto_file, String created_at) {
        this.id = id;
        this.username = username;
        this.users_level_id = users_level_id;
        this.nim = nim;
        this.nama_mahasiswa = nama_mahasiswa;
        this.foto_file = foto_file;
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsersLevelId() {
        return users_level_id;
    }

    public void setUsersLevelId(String users_level_id) {
        this.users_level_id = users_level_id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNamaMahasiswa() {
        return nama_mahasiswa;
    }

    public void setNamaMahasiswa(String nama_mahasiswa) {
        this.nama_mahasiswa = nama_mahasiswa;
    }

    public String getFotoFile() {
        return foto_file;
    }

    public void setFotoFile(String foto_file) {
        this.foto_file = foto_file;
    }


}
