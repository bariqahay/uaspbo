package com.satuhati.satuhatis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "kelas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kelas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "matakuliah_id", nullable = false)
    @JsonIgnoreProperties({"dosenList", "kelasList", "prodi"}) // pilih yg bikin loop
    private MataKuliah matakuliah;

    @ManyToOne
    @JoinColumn(name = "fakultas_id")
    private Fakultas fakultas;

    @ManyToOne
    @JoinColumn(name = "prodi_id")
    private Prodi prodi;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "dosen_id")
    private Dosen dosen;

    @Column(name = "nama_kelas", nullable = false, length = 100)
    private String namaKelas;

    @Column(nullable = false)
    private int kapasitas;

    private String ruangan;

    // ✅ Tambahin relasi ke peserta (kelas_mahasiswa)
    @OneToMany(mappedBy = "kelas", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KelasMahasiswa> peserta;
}
