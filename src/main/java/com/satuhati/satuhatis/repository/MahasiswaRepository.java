package com.satuhati.satuhatis.repository;

import com.satuhati.satuhatis.model.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Long> {
    Optional<Mahasiswa> findByNim(String nim);
    Optional<Mahasiswa> findByUser_Id(Long userId);
    Optional<Mahasiswa> findByUser_Username(String username);
    boolean existsByNim(String nim);
}
