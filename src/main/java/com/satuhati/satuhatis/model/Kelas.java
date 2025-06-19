package com.satuhati.satuhatis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private MataKuliah matakuliah;

    @ManyToOne
    @JoinColumn(name = "dosen_id")
    private Dosen dosen;

    @Column(name = "nama_kelas", nullable = false, length = 100)
    private String namaKelas;

    @Column(nullable = false)
    private int kapasitas;

    // ✅ Tambahin relasi ke peserta (kelas_mahasiswa)
    @OneToMany(mappedBy = "kelas", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KelasMahasiswa> peserta;
}
