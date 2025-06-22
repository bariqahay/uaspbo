package com.satuhati.satuhatis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "kelas_mahasiswa", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"mahasiswa_id", "kelas_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KelasMahasiswa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mahasiswa_id")
    @JsonIgnore
    private Mahasiswa mahasiswa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kelas_id")
    @JsonIgnore
    private Kelas kelas;

    @ManyToMany
    @JoinTable(
        name = "prodi_matakuliah",
        joinColumns = @JoinColumn(name = "prodi_id"),
        inverseJoinColumns = @JoinColumn(name = "matakuliah_id")
    )
    @JsonIgnore // <--- Biar aman waktu return Prodi
    private List<MataKuliah> mataKuliahList;
}
