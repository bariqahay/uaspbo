package com.satuhati.satuhatis.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mahasiswa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mahasiswa {

    @ManyToOne
    @JoinColumn(name = "prodi_id")
    private Prodi prodi;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true, length = 50)
    private String nim;

    @Column(nullable = false, length = 255)
    private String nama;

    // Tambahin fields lain kalo ada...
}
