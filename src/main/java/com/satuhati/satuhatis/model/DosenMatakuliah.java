package com.satuhati.satuhatis.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dosen_matakuliah")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DosenMatakuliah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dosen_id", nullable = false)
    private Dosen dosen;

    @ManyToOne
    @JoinColumn(name = "matakuliah_id", nullable = false)
    private MataKuliah matakuliah;
}
