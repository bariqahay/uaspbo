package com.satuhati.satuhatis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "prodi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prodi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @ManyToOne
    @JoinColumn(name = "fakultas_id")
    private Fakultas fakultas;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "prodi_matakuliah",
        joinColumns = @JoinColumn(name = "prodi_id"),
        inverseJoinColumns = @JoinColumn(name = "matakuliah_id")
    )
    private List<MataKuliah> mataKuliahList;
}
