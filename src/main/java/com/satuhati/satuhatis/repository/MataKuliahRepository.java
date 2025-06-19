package com.satuhati.satuhatis.repository;

import com.satuhati.satuhatis.model.MataKuliah;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MataKuliahRepository extends JpaRepository<MataKuliah, Long> {
    Optional<MataKuliah> findByKode(String kode);
    boolean existsByKode(String kode);
}
