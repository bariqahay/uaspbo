package com.satuhati.satuhatis.model;

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
    @JoinColumn(name = "prodi_id")
    private Prodi prodi;

    @Column(nullable = false, unique = true, length = 50)
    private String nip;

    @Column(nullable = false, length = 100)
    private String nama;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
