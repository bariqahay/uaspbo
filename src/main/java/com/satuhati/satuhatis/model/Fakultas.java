package com.satuhati.satuhatis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "fakultas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fakultas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @JsonIgnore
    @OneToMany(mappedBy = "fakultas")
    private List<Prodi> prodiList;
}
