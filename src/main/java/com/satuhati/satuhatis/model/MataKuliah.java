package com.satuhati.satuhatis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "matakuliah")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MataKuliah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kode", nullable = false, unique = true, length = 20)
    private String kode;

    @Column(name = "nama", nullable = false, length = 255)
    private String nama;

    @Column(name = "sks", nullable = false)
    private Integer sks;

    @ManyToMany(mappedBy = "matakuliahList")
    @JsonIgnore
    private List<Dosen> dosenList;

    @OneToMany(mappedBy = "matakuliah")
    @JsonIgnore
    private List<Kelas> kelasList;

    @ManyToOne
    @JoinColumn(name = "prodi_id", nullable = false)
    private Prodi prodi;

    // ✅ Custom constructor for seeding or manual instantiation (no dosenList & kelasList)
    public MataKuliah(Long id, String kode, String nama, Integer sks, Prodi prodi) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.sks = sks;
        this.prodi = prodi;
    }
}
