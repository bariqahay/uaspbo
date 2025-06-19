package com.satuhati.satuhatis.model;

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
    private Mahasiswa mahasiswa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kelas_id")
    private Kelas kelas;
}
