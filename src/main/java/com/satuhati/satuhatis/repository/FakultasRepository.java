package com.satuhati.satuhatis.repository;

import com.satuhati.satuhatis.model.Fakultas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FakultasRepository extends JpaRepository<Fakultas, Long> {
    Optional<Fakultas> findByNama(String nama); // ⬅️ ini yang dibutuhkan
}