package com.satuhati.satuhatis.repository;


import com.satuhati.satuhatis.model.Kelas;
import com.satuhati.satuhatis.model.Mahasiswa;
import com.satuhati.satuhatis.model.KelasMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KelasMahasiswaRepository extends JpaRepository<KelasMahasiswa, Long> {
    List<KelasMahasiswa> findByMahasiswa(Mahasiswa mahasiswa);
    List<KelasMahasiswa> findByKelas(Kelas kelas);
    Optional<KelasMahasiswa> findByMahasiswaAndKelas(Mahasiswa mahasiswa, Kelas kelas);
    boolean existsByKelasAndMahasiswa(Kelas kelas, Mahasiswa mahasiswa);

    // ⬅️ Tambahin ini bro
    boolean existsByMahasiswaAndKelas(Mahasiswa mahasiswa, Kelas kelas);
}
