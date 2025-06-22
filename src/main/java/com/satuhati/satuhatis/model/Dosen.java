package com.satuhati.satuhatis.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dosen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dosen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prodi_id", nullable = false)
    private Prodi prodi;

    @Column(nullable = false, unique = true, length = 50)
    private String nip;

    @Column(nullable = false, length = 100)
    private String nama;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "dosen_matakuliah",
        joinColumns = @JoinColumn(name = "dosen_id"),
        inverseJoinColumns = @JoinColumn(name = "matakuliah_id")
    )
    private List<MataKuliah> matakuliahList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
