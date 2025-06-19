package com.satuhati.satuhatis.model;

import jakarta.persistence.*;
import lombok.*;

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
}
